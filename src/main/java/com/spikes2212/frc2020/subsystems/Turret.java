package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.commands.MoveTurretToFieldRelativeAngle;
import com.spikes2212.frc2020.commands.OrientTurretToPowerPort;
import com.spikes2212.frc2020.commands.ResetTurret;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {

    public static final RootNamespace turretNamespace = new RootNamespace("turret");

    private static final Namespace PID = turretNamespace.addChild("PID");

    private static final Supplier<Double> maxSpeed = turretNamespace.addConstantDouble("Max Speed", 0.6);

    private static final Supplier<Double> turnSpeed = turretNamespace.addConstantDouble("turn speed", 0.3);

    private static final Supplier<Double> minSpeed = turretNamespace.addConstantDouble("Min Speed", -0.6);
    private static final Supplier<Integer> minAngle = turretNamespace.addConstantInt("Min Angle", 30);
    private static final Supplier<Integer> maxAngle = turretNamespace.addConstantInt("Max Angle", 330);

    public static final Supplier<Double> frontAngle = turretNamespace.addConstantDouble("low target angle", 140);
    public static final Supplier<Double> climbingAngle = turretNamespace.addConstantDouble("climbing angle", 140);

    private static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);

    private static final Supplier<Double> kS = PID.addConstantDouble("kS", 0);

    private static final Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    private static final Supplier<Double> setpoint = PID.addConstantDouble("setpoint", 90);
    private static final Supplier<Integer> timeout = PID.addConstantInt("timeout", 30);

    private static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private static final double DEGREES_TO_PULSES = 4096 * 28 / 8.5 / 360;

    private static final Turret instance = new Turret();

    private WPI_TalonSRX motor;

    private DigitalInput endLimit;

    private DigitalInput startLimit;

    private boolean enabled;

    public static Turret getInstance() {
        return instance;
    }

    private Turret() {
        super(minSpeed, maxSpeed);
        motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
        endLimit = new DigitalInput(RobotMap.DIO.TURRET_END_LIMIT);
        startLimit = new DigitalInput(RobotMap.DIO.TURRET_START_LIMIT);
        enabled = true;
    }

    public boolean atStart() {
        return startLimit.get();
    }

    public boolean atEnd() {
        return endLimit.get();
    }

    public void setAutomaticDefaultCommand() {
        setDefaultCommand(new MoveTurretToFieldRelativeAngle().perpetually());
    }

    public void setManualDefaultCommand() {
        setDefaultCommand(new MoveTalonSubsystem(this, Robot.oi::getControllerRightAngle, () -> 100.0).perpetually());
    }

    public double getYaw() {
        return motor.getSelectedSensorPosition() / DEGREES_TO_PULSES;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return ((speed > 0 && !atEnd()) || (speed < 0 && !atStart()));
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void periodic() {
        turretNamespace.update();
        if (startLimit.get())
            motor.setSelectedSensorPosition((int) (minAngle.get() * DEGREES_TO_PULSES));
//        if (endLimit.get())
//            motor.setSelectedSensorPosition((int) (maxAngle.get() * DEGREES_TO_PULSES));
    }

    @Override
    public void configureDashboard() {
//        setManualDefaultCommand();
        VisionService vision = VisionService.getInstance();
        turretNamespace.putBoolean("turret limit", startLimit::get);
        turretNamespace.putNumber("turret angle", this::getYaw);
        turretNamespace.putNumber("speed controller values", motor::getMotorOutputPercent);
        turretNamespace.putData("rotate with pid", new MoveTalonSubsystem(this, setpoint, waitTime));
        turretNamespace.putData("rotate with speed", new MoveGenericSubsystem(this, turnSpeed));
        turretNamespace.putData("orient with vision", new OrientTurretToPowerPort());
        turretNamespace.putData("reset turret", new ResetTurret());

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

    private double validateSetpoint(double setpoint) {
        return ((setpoint % 360) + 360) % 360;
    }

    @Override
    public void pidSet(double setpoint) {
        setpoint = validateSetpoint(setpoint);
        setpoint *= DEGREES_TO_PULSES;
        motor.configPeakOutputForward(maxSpeed.get(), timeout.get());
        motor.configPeakOutputReverse(minSpeed.get(), timeout.get());

        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());

        double error = setpoint - motor.getSelectedSensorPosition();
        motor.set(ControlMode.Position, setpoint, DemandType.ArbitraryFeedForward, kS.get() * Math.signum(error));
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {

        setpoint = validateSetpoint(setpoint);

        setpoint *= DEGREES_TO_PULSES;

        double tolerance = Turret.tolerance.get() * DEGREES_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return Math.abs(setpoint - position) <= tolerance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
