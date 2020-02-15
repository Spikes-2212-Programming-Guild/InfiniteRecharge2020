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
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Shooter extends GenericSubsystem implements TalonSubsystem {

    public static final double distancePerPulse = 2 * 0.0254 * Math.PI / 2048;

    private static RootNamespace shooterNamespace = new RootNamespace("shooter");
    private static Namespace PID = shooterNamespace.addChild("PID");
    private static Supplier<Double> maxSpeed = shooterNamespace.addConstantDouble("Max Speed", 0.6);
    private static Supplier<Double> minSpeed = shooterNamespace.addConstantDouble("Min Speed", 0);
    private static Supplier<Double> shootSpeed =
      shooterNamespace.addConstantDouble("Shooting Speed", 0.6);
    private static Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private static Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private static Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    private static Supplier<Double> kF = PID.addConstantDouble("kF", 0);
    private static Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    private static Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    private static Supplier<Integer> loop = PID.addConstantInt("Loop", 0);
    private static Supplier<Integer> timeout = PID.addConstantInt("Timeout", 30);

    private static Shooter instance;

    public static Shooter getInstance() {
        if(instance == null) {
            WPI_TalonSRX master = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_MASTER);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.CAN.PCM, RobotMap.PCM.SHOOTER_FORWARD, RobotMap.PCM.SHOOTER_BACKWARD);
            instance = new Shooter(master, solenoid);
        }

        return instance;
    }

    private PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
    private WPI_TalonSRX master;
    private DoubleSolenoid solenoid;

    private boolean enabled;

    private Shooter(WPI_TalonSRX master, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.master = master;
        this.solenoid = solenoid;
        enabled = true;
    }

    @Override
    public void apply(double speed) {
        master.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return speed >= 0 && enabled;
    }

    @Override
    public void stop() {
        master.stopMotor();
    }

    @Override
    public void periodic() {
        shooterNamespace.update();
    }

    @Override
    public void configureLoop() {
        master.configFactoryDefault();
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, loop.get(), timeout.get());

        master.configNominalOutputForward(0, timeout.get());
        master.configNominalOutputReverse(0, timeout.get());
        master.configPeakOutputForward(maxSpeed.get(), timeout.get());
        master.configPeakOutputReverse(minSpeed.get(), timeout.get());

        master.configAllowableClosedloopError(loop.get(), 0, timeout.get());
        master.config_kI(loop.get(), pidSettings.getkI(), timeout.get());
        master.config_kP(loop.get(), pidSettings.getkP(), timeout.get());
        master.config_kD(loop.get(), pidSettings.getkD(), timeout.get());
        master.config_kF(loop.get(), kF.get(), timeout.get());
    }

    @Override
    public void pidSet(double setpoint) {
        master.configPeakOutputForward(maxSpeed.get(), timeout.get());
        master.configPeakOutputReverse(minSpeed.get(), timeout.get());

        master.config_kI(loop.get(), pidSettings.getkI(), timeout.get());
        master.config_kP(loop.get(), pidSettings.getkP(), timeout.get());
        master.config_kD(loop.get(), pidSettings.getkD(), timeout.get());
        master.config_kF(loop.get(), kF.get(), timeout.get());

        master.set(ControlMode.Velocity, setpoint / distancePerPulse);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        return Math.abs(setpoint - master.getSelectedSensorPosition(loop.get())) < pidSettings.getTolerance()
                || !canMove(master.getMotorOutputPercent());
    }

    public void openHood() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void closeHood() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void configureDashboard() {
        shooterNamespace.putData("shoot", new MoveTalonSubsystem(this, shootSpeed, pidSettings::getWaitTime));
        shooterNamespace.putData("open", new InstantCommand(this::openHood, this));
        shooterNamespace.putData("close", new InstantCommand(this::closeHood, this));
    }
}
