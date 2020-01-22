package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.*;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;

import java.util.function.Supplier;

public class Shooter extends GenericSubsystem {
    public static final Namespace namespace = new RootNamespace("Shooter");
    public static final Namespace PID = namespace.addChild("PID");

    public static final Supplier<Double> maxSpeed = namespace.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> minSpeed = namespace.addConstantDouble("Min Speed", -0.6);

    public static final Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);

    private static Shooter instance;

    private PIDLoop pidForSpeed;

    private PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private WPI_TalonSRX master;
    private WPI_TalonSRX slave;

    private Shooter(WPI_TalonSRX master, WPI_TalonSRX slave) {
        super(minSpeed, maxSpeed);
        this.master = master;
        this.slave = slave;

        this.pidForSpeed = new TalonPIDLoop(master, pidSettings, maxSpeed, ControlMode.Velocity);
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

    public PIDLoop getPIDForSpeed() {
        return pidForSpeed;
    }
}
