package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.commands.MoveTurretToFieldRelativeAngle;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpiutil.math.MathUtil;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {

    private static final RootNamespace turretNamespace = new RootNamespace("turret");

    private static final Namespace PID = turretNamespace.addChild("PID");

    private static final Supplier<Double> maxSpeed = turretNamespace.addConstantDouble("Max Speed", 0.6);

    private static final Supplier<Double> turnSpeed = turretNamespace.addConstantDouble("turn speed", 0.3);

    private static final Supplier<Double> minSpeed = turretNamespace.addConstantDouble("Min Speed", -0.6);
    private static final Supplier<Integer> minAngle = turretNamespace.addConstantInt("Min Angle", 30);
    private static final Supplier<Integer> maxAngle = turretNamespace.addConstantInt("Max Angle", 330);

    private static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);

    private static final Supplier<Double> kS = PID.addConstantDouble("kS", 0);

    private static final Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    private static final Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    private static final Supplier<Double> setpoint = PID.addConstantDouble("setpoint", 90);
    private static final Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    private static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private static final double DEGREES_TO_PULSES = 4096 * 28 / 8.5 / 360;

    private static Turret instance;

    public static Turret getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
            DigitalInput endLimit = new DigitalInput(RobotMap.DIO.TURRET_END_LIMIT);
            DigitalInput startLimit = new DigitalInput(RobotMap.DIO.TURRET_START_LIMIT);
            motor.setNeutralMode(NeutralMode.Brake);
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
        return ((speed > 0 && !endLimit.get()) || (speed < 0 && !startLimit.get())) && enabled;
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

    @Override
    public void periodic() {
        turretNamespace.update();
        if (startLimit.get())
            motor.setSelectedSensorPosition((int) (minAngle.get() * DEGREES_TO_PULSES));
        if (endLimit.get())
            motor.setSelectedSensorPosition((int) (maxAngle.get() * DEGREES_TO_PULSES));
    }

    @Override
    public void configureDashboard() {
//        setAutomaticDefaultCommand();
//        turretNamespace.putBoolean("on target", () -> onTarget(setpoint.get()));
        turretNamespace.putBoolean("turret limit", startLimit::get);
        turretNamespace.putNumber("turret angle", () -> motor.getSelectedSensorPosition());
        turretNamespace.putNumber("speed controller values", motor::getMotorOutputPercent);
        turretNamespace.putData("rotate with pid", new MoveTalonSubsystem(this, setpoint, waitTime));
        turretNamespace.putData("rotate with speed", new MoveGenericSubsystem(this, turnSpeed));
        turretNamespace.putData("rotate with image processing", new InstantCommand(() -> TurretStateMachine.getInstance().getTransformationFor(TurretStateMachine.TurretState.AUTOMATIC)));
    }

    public void setAutomaticDefaultCommand() {
        setDefaultCommand(new MoveTurretToFieldRelativeAngle().perpetually());
    }

    public void setSpeedDefaultCommand() {
        setDefaultCommand(new MoveGenericSubsystem(this, turnSpeed));
    }

    public void setManualDefaultCommand() {
        setDefaultCommand(new MoveTalonSubsystem(this, Robot.oi::getControllerRightAngle, () -> 0.0).perpetually());
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

        double error = setpoint - motor.getSelectedSensorPosition();
        motor.set(ControlMode.Position, setpoint, DemandType.ArbitraryFeedForward, kS.get()  * Math.signum(error));
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {

        setpoint = MathUtil.clamp(setpoint % 360, minAngle.get(), maxAngle.get());

        setpoint *= DEGREES_TO_PULSES;

        double tolerance = Turret.tolerance.get() * DEGREES_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.getMotorOutputPercent()) || Math.abs(setpoint - position) <= tolerance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}