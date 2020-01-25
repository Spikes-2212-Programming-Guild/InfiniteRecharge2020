package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class Shooter extends GenericSubsystem implements TalonSubsystem {
    public static final Namespace namespace = new RootNamespace("Shooter");
    public static final Namespace PID = namespace.addChild("PID");

    public static final Supplier<Double> maxSpeed = namespace.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> minSpeed = namespace.addConstantDouble("Min Speed", 0);

    public static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> kF = PID.addConstantDouble("kF", 0);
    public static final Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);
    public static final Supplier<Integer> loop = PID.addConstantInt("Loop", 0);
    public static final Supplier<Integer> timeout = PID.addConstantInt("Timeout", 30);

    private static Shooter instance;

    private PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private WPI_TalonSRX master;
    private WPI_TalonSRX slave;

    private double lastTimeNotOnTarget;

    private Shooter(WPI_TalonSRX master, WPI_TalonSRX slave) {
        super(minSpeed, maxSpeed);
        this.master = master;
        this.slave = slave;
    }

    public static Shooter getInstance() {
        if(instance == null) {
            WPI_TalonSRX master = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_MASTER);
            WPI_TalonSRX slave = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_SLAVE);
            slave.follow(master);

            instance = new Shooter(master, slave);
        }

        return instance;
    }

    @Override
    public void apply(double speed) {
        master.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return speed >= 0;
    }

    @Override
    public void stop() {
        master.stopMotor();
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

        master.set(ControlMode.Velocity, setpoint);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        if(!inPosition(setpoint)) {
            lastTimeNotOnTarget = Timer.getFPGATimestamp();
        }

        return Timer.getFPGATimestamp() - lastTimeNotOnTarget >= pidSettings.getWaitTime()
                || !canMove(master.getMotorOutputPercent());
    }

    private boolean inPosition(double setpoint) {
        return Math.abs(setpoint - master.getSelectedSensorPosition(loop.get())) < pidSettings.getTolerance();
    }
}
