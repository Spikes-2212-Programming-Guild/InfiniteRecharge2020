package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Climbing {
    private static final Namespace climbingNamespace = new RootNamespace("Climbing");
    private static final Namespace pidNamespace = climbingNamespace.addChild("PID");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    public static final PIDSettings PID_SETTINGS = new PIDSettings(kP, kI, kD);
    private static Climbing instance;
    private WPI_TalonSRX leftMotor;
    private WPI_TalonSRX rightMotor;
    private Encoder leftEncoder;
    private Encoder rightEncoder;

    private Climbing(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, Encoder leftEncoder, Encoder rightEncoder) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
    }

    public static Climbing getInstance() {
        if (instance == null) {
            WPI_TalonSRX leftMotor = new WPI_TalonSRX(RobotMap.CAN.CLIMBING_TALON_LEFT);
            WPI_TalonSRX rightMotor = new WPI_TalonSRX(RobotMap.CAN.CLIMBING_TALON_RIGHT);
            Encoder leftEncoder = new Encoder(RobotMap.DIO.CLIMBING_ENCODER_LEFT_A, RobotMap.DIO.CLIMBING_ENCODER_LEFT_B);
            Encoder rightEncoder = new Encoder(RobotMap.DIO.CLIMBING_ENCODER_RIGHT_A, RobotMap.DIO.CLIMBING_ENCODER_RIGHT_B);
            instance = new Climbing(leftMotor, rightMotor, leftEncoder, rightEncoder);
        }
        return instance;
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public void configureDashboard() {
        climbingNamespace.putNumber("left encoder", leftEncoder::get);
        climbingNamespace.putNumber("right encoder", rightEncoder::get);
    }
}