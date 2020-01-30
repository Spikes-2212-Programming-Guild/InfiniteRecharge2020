package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpiutil.math.MathUtil;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {

    public static final RootNamespace turretNamespace = new RootNamespace("Turret");

    public static final Namespace PID = turretNamespace.addChild("PID");

    public static final Supplier<Double> maxSpeed = turretNamespace.addConstantDouble("Max Speed", 0.6);

    public static final Supplier<Double> minSpeed = turretNamespace.addConstantDouble("Min Speed", -0.6);
    public static final Supplier<Double> minAngle = turretNamespace.addConstantDouble("Min Angle", 30);
    public static final Supplier<Double> maxAngle = turretNamespace.addConstantDouble("Max Angle", 330);
    public static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);

    public static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    public static final Supplier<Double> setpoint = PID.addConstantDouble("setpoint", 90);
    public static final Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    public static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private static final double DEGREES_TO_PULSES = 4096 * Math.PI / 180 * 11 / 9;

    private static Turret instance;

    public enum TurretState {
        OFF, VISION, MANUAL, ABSOLUTE
    }

    public static Turret getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
            motor.setInverted(true);
            DigitalInput endLimit = new DigitalInput(RobotMap.DIO.END_LIMIT);
            DigitalInput startLimit = new DigitalInput(RobotMap.DIO.START_LIMIT);
            instance = new Turret(motor, endLimit, startLimit);
        }
        return instance;
    }


    private WPI_TalonSRX motor;

    private DigitalInput endLimit;

    private DigitalInput startLimit;

    private TurretState state;

    private Turret(WPI_TalonSRX motor, DigitalInput endLimit, DigitalInput startLimit) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.endLimit = endLimit;
        this.startLimit = startLimit;
        state = TurretState.OFF;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return (((speed > 0 && !endLimit.get()) || (speed < 0 && !startLimit.get())) && state != TurretState.OFF)
                || (state == TurretState.OFF && speed == 0);
    }

    @Override
    public void stop() {
        setState(TurretState.OFF);
        motor.stopMotor();
    }

    public TurretState getState() {
        return state;
    }

    public void setState(TurretState state) {
        if(StateMachineSync.validate(Intake.getInstance().getState(), Feeder.getInstance().getState(), Shooter.getInstance().getState(), state))
            this.state = state;
    }

    public boolean atStart() {
        return startLimit.get();
    }

    public boolean atEnd() {
        return endLimit.get();
    }

    @Override
    public void periodic() {
        turretNamespace.update();
    }

    @Override
    public void configureDashboard() {
        turretNamespace.putData("rotate", new MoveTalonSubsystem(this, setpoint, waitTime));
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeout.get());
        if(tolerance.get() == 0)
            setState(TurretState.ABSOLUTE);
        else
            setState(TurretState.VISION);

        motor.configNominalOutputForward(0, timeout.get());
        motor.configNominalOutputReverse(0, timeout.get());
        motor.configPeakOutputForward(maxSpeed.get(), timeout.get());
        motor.configPeakOutputReverse(minSpeed.get(), timeout.get());

        motor.configAllowableClosedloopError(0, 0, timeout.get());
        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());
    }

    @Override
    public void pidSet(double setpoint) {
        setpoint = MathUtil.clamp(setpoint % 360, minAngle.get(), maxAngle.get());

        setpoint *= DEGREES_TO_PULSES;

        motor.configPeakOutputForward(maxSpeed.get(), timeout.get());
        motor.configPeakOutputReverse(minSpeed.get(), timeout.get());

        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());

        motor.set(ControlMode.Position, setpoint);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        setpoint = MathUtil.clamp(setpoint % 360, maxAngle.get(), minAngle.get());

        setpoint *= DEGREES_TO_PULSES;

        double tolerance = Turret.tolerance.get() * DEGREES_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.getMotorOutputPercent()) || Math.abs(setpoint - position) < tolerance;
    }
}
