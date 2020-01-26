package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.util.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpiutil.math.MathUtil;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {
    private static final double DEGREES_TO_PULSES = 4096*Math.PI/180 * 9/11;

    public static final Namespace turretNamespace = new RootNamespace("Turret");

    public static final Namespace PID = turretNamespace.addChild("PID");

    public static final Supplier<Double> TEST_SPEED = turretNamespace.addConstantDouble("Test Speed", 0.9);
    public static final Supplier<Double> MAX_SPEED = turretNamespace.addConstantDouble("Max Speed", 1);
    public static final Supplier<Double> MIN_SPEED = turretNamespace.addConstantDouble("Min Speed", -1);
    public static final Supplier<Double> MIN_ANGLE = turretNamespace.addConstantDouble("Min Angle", 30);
    public static final Supplier<Double> MAX_ANGLE = turretNamespace.addConstantDouble("Max Angle", 330);
    public static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> TOLERANCE = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> WAIT_TIME = PID.addConstantDouble("Wait Time", 0);
    public static final Supplier<Double> SETPOINT = PID.addConstantDouble("setpoint", 90);
    public static final Supplier<Integer> TIMEOUT = PID.addConstantInt("timeout", 30);
    public static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, TOLERANCE, WAIT_TIME);

    private static Turret instance;

    public static Turret getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
            DigitalInput endLimit = new DigitalInput(RobotMap.DIO.END_LIMIT);
            DigitalInput startLimit = new DigitalInput(RobotMap.DIO.START_LIMIT);
            instance = new Turret(motor, endLimit, startLimit);
        }
        return instance;
    }

    private WPI_TalonSRX motor;
    private TalonEncoder encoder;

    private DigitalInput endLimit;
    private DigitalInput startLimit;

    private Turret(WPI_TalonSRX motor, DigitalInput endLimit, DigitalInput startLimit) {
        super(MIN_SPEED, MAX_SPEED);
        this.motor = motor;
        this.endLimit = endLimit;
        this.startLimit = startLimit;
        encoder = new TalonEncoder(this.motor);
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return (speed > 0 && !endLimit.get()) || (speed < 0 && !startLimit.get());
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public boolean atStart(){
        return startLimit.get();
    }

    public boolean atEnd(){
        return endLimit.get();
    }

    public void setDistancePerPulse(double distancePerPulse) {
        encoder.setDistancePerPulse(distancePerPulse);
    }

    public double getDistancePerPulse() {
        return encoder.getDistancePerPulse();
    }

    @Override
    public void configureDashboard(){
        turretNamespace.putData("rotate", new MoveTalonSubsystem(this, SETPOINT, WAIT_TIME));
        turretNamespace.putData("move with no pid", new MoveGenericSubsystem(this, TEST_SPEED));
        turretNamespace.putNumber("encoder value", encoder::getPosition);
    }

    @Override
    public void periodic() {
        ((RootNamespace)turretNamespace).update();
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT.get());
        motor.setSelectedSensorPosition(0,0, TIMEOUT.get());

        motor.configNominalOutputForward(0, TIMEOUT.get());
        motor.configNominalOutputReverse(0, TIMEOUT.get());
        motor.configPeakOutputForward(MAX_SPEED.get(), TIMEOUT.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), TIMEOUT.get());

        motor.configAllowableClosedloopError(0,0,TIMEOUT.get());
        motor.config_kP(0, kP.get(), TIMEOUT.get());
        motor.config_kI(0, kI.get(), TIMEOUT.get());
        motor.config_kD(0, kD.get(), TIMEOUT.get());
    }

    @Override
    public void pidSet(double setpoint) {
        setpoint = MathUtil.clamp(setpoint % 360, MIN_ANGLE.get(), MAX_ANGLE.get());

        setpoint *= DEGREES_TO_PULSES;
        turretNamespace.putNumber("target", setpoint);

        motor.configPeakOutputForward(MAX_SPEED.get(), TIMEOUT.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), TIMEOUT.get());

        motor.config_kP(0, kP.get(), TIMEOUT.get());
        motor.config_kI(0, kI.get(), TIMEOUT.get());
        motor.config_kD(0, kD.get(), TIMEOUT.get());

        motor.set(ControlMode.Position, setpoint);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        setpoint = MathUtil.clamp(setpoint % 360, MIN_ANGLE.get(), MAX_ANGLE.get());

        setpoint *= DEGREES_TO_PULSES;

        double tolerance = TOLERANCE.get() * DEGREES_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.getMotorOutputPercent()) || Math.abs(setpoint - position) < tolerance;
    }
}
