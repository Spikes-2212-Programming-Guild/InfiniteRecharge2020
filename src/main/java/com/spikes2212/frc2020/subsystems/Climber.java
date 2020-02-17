package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.Supplier;

public class Climber extends SubsystemBase {

    private static final Namespace climbingNamespace = new RootNamespace("climber");
    private static final Namespace pidNamespace = climbingNamespace.addChild("PID");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);

    public static final PIDSettings pidSettings = new PIDSettings(kP, kI, kD);

    private static Climber instance;

    public static Climber getInstance() {
        if(instance == null) {
            WPI_TalonSRX leftMotor = new WPI_TalonSRX(RobotMap.CAN.CLIMBER_TALON_LEFT);
            WPI_TalonSRX rightMotor = new WPI_TalonSRX(RobotMap.CAN.CLIMBER_TALON_RIGHT);
            Encoder leftEncoder = new Encoder(RobotMap.DIO.CLIMBER_ENCODER_LEFT_POS,
                    RobotMap.DIO.CLIMBER_ENCODER_LEFT_NEG);
            Encoder rightEncoder = new Encoder(RobotMap.DIO.CLIMBER_ENCODER_RIGHT_POS,
                    RobotMap.DIO.CLIMBER_ENCODER_RIGHT_NEG);
            instance = new Climber(leftMotor, rightMotor, leftEncoder, rightEncoder);
        }
        return instance;
    }

    private WPI_TalonSRX leftMotor;
    private WPI_TalonSRX rightMotor;
    private Encoder leftEncoder;
    private Encoder rightEncoder;

    private Climber(WPI_TalonSRX leftMotor, WPI_TalonSRX rightMotor, Encoder leftEncoder,
                    Encoder rightEncoder) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
    }

    public double getLeftDistance() {
        return leftEncoder.getDistance();
    }

    public double getRightDistance() {
        return rightEncoder.getDistance();
    }

    public void setLeft(double speed) {
        leftMotor.set(speed);
    }

    public void setRight(double speed) {
        rightMotor.set(speed);
    }

    public void stopLeft() {
        leftMotor.stopMotor();
    }

    public void stopRight() {
        rightMotor.stopMotor();
    }

    public void stop() {
        stopLeft();
        stopRight();
    }

    public void configureDashboard() {
        climbingNamespace.putNumber("left encoder", leftEncoder::get);
        climbingNamespace.putNumber("right encoder", rightEncoder::get);
    }
}
