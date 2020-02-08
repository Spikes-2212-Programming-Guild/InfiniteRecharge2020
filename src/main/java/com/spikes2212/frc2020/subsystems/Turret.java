package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.statemachines.IntakeStateMachine;
import com.spikes2212.frc2020.statemachines.ShooterStateMachine;
import com.spikes2212.frc2020.statemachines.TurretStateMachine;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpiutil.math.MathUtil;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {

    public static RootNamespace turretNamespace = new RootNamespace("turret");
    public static Namespace PID = turretNamespace.addChild("PID");

    public static Supplier<Double> maxSpeed = turretNamespace.addConstantDouble("Max Speed", 0.6);
    public static Supplier<Double> minSpeed = turretNamespace.addConstantDouble("Min Speed", -0.6);
    public static Supplier<Double> minAngle = turretNamespace.addConstantDouble("Min Angle", 30);
    public static Supplier<Double> maxAngle = turretNamespace.addConstantDouble("Max Angle", 330);

    public static Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    public static Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public static Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    public static Supplier<Double> setpoint = PID.addConstantDouble("setpoint", 90);
    public static Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    private static final double DEGREES_TO_PULSES = 4096 * Math.PI / 180 * 11 / 9;

    private static Turret instance;

    public static Turret getInstance() {
        if(instance == null) {
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

    private boolean enabled;

    private Turret(WPI_TalonSRX motor, DigitalInput endLimit, DigitalInput startLimit) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.endLimit = endLimit;
        this.startLimit = startLimit;
        enabled = true;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return (speed > 0 && !endLimit.get()) || (speed < 0 && !startLimit.get()) && enabled;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public boolean atStart() {
        return startLimit.get();
    }

    public boolean atEnd() {
        return endLimit.get();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void periodic() {
        turretNamespace.update();
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeout.get());

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

    @Override
    public void configureDashboard() {
        turretNamespace.putData("absolute",
                (Sendable) TurretStateMachine.getInstance().getTransformationFor(TurretStateMachine.TurretState.ABSOLUTE));
        turretNamespace.putData("manual",
                (Sendable) TurretStateMachine.getInstance().getTransformationFor(TurretStateMachine.TurretState.MANUAL));
        turretNamespace.putData("vision",
                (Sendable) TurretStateMachine.getInstance().getTransformationFor(TurretStateMachine.TurretState.VISION));
        turretNamespace.putData("off", (Sendable) TurretStateMachine.getInstance().getTransformationFor(TurretStateMachine.TurretState.OFF));
        turretNamespace.putString("state", TurretStateMachine.getInstance().getState()::name);
        turretNamespace.putData("rotate", new MoveTalonSubsystem(this, setpoint, waitTime));
    }
}
