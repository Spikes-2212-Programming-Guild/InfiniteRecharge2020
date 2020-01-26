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
    public static final Namespace shooterNamespace = new RootNamespace("Shooter");
    public static final Namespace PID = shooterNamespace.addChild("PID");

    public static final Supplier<Double> MAX_SPEED = shooterNamespace.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> MIN_SPEED = shooterNamespace.addConstantDouble("Min Speed", -0.6);

    public static final Supplier<Double> K_P = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> K_I = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> K_D = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> TOLERANCE = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> WAIT_TIME = PID.addConstantDouble("Wait Time", 0);

    private static Shooter instance;

    private PIDLoop pidForSpeed;

    private PIDSettings pidSettings = new PIDSettings(K_P, K_I, K_D, TOLERANCE, WAIT_TIME);

    private WPI_TalonSRX master;
    private WPI_TalonSRX slave;

    private Shooter(WPI_TalonSRX master, WPI_TalonSRX slave) {
        super(MIN_SPEED, MAX_SPEED);
        this.master = master;
        this.slave = slave;

        this.pidForSpeed = new TalonPIDLoop(master, pidSettings, MAX_SPEED, ControlMode.Velocity);
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
    public void periodic() {
        ((RootNamespace)shooterNamespace).update();
    }

    public PIDLoop getPIDForSpeed() {
        return pidForSpeed;
    }
}
