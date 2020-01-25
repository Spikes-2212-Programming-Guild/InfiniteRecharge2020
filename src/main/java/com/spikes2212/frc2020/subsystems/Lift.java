package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.dashboard.Namespace;
import com.spikes2212.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Lift extends GenericSubsystem {
    private static final Namespace liftNamespace = new RootNamespace("Lift");
    public static final Supplier<Double> distancePerPulse = liftNamespace.addConstantDouble("Distance Per Pulse", 0);
    private static final Namespace pidNamespace = liftNamespace.addChild("PID");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    public static final PIDSettings PID_SETTINGS = new PIDSettings(kP, kI, kD);
    private static final Namespace feedForwardNamespace = liftNamespace.addChild("Feed Forward");
    private static final Supplier<Double> kS = feedForwardNamespace.addConstantDouble("kS", 0);
    private static final Supplier<Double> kG = feedForwardNamespace.addConstantDouble("kG", 0);
    public static final FeedForwardSettings FEED_FORWARD_SETTINGS = new FeedForwardSettings(kS, () -> 0.0, () -> 0.0, kG);

    private static Lift instance;
    private WPI_TalonSRX motor;
    private Encoder encoder;
    private DigitalInput bottomLimitSwitch;
    private DigitalInput topLimitSwitch;

    private Lift(WPI_TalonSRX motor, Encoder encoder, DigitalInput bottomLimitSwitch, DigitalInput topLimitSwitch) {
        this.motor = motor;
        this.encoder = encoder;
        this.bottomLimitSwitch = bottomLimitSwitch;
        this.topLimitSwitch = topLimitSwitch;
    }

    public static Lift getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.ELEVATOR_TALON);
            Encoder encoder = new Encoder(RobotMap.DIO.ELEVATOR_ENCODER_A, RobotMap.DIO.ELEVATOR_ENCODER_B);
            DigitalInput bottomLimitSwitch = new DigitalInput(RobotMap.DIO.ELEVATOR_BOTTOM_SWITCH);
            DigitalInput topLimitSwitch = new DigitalInput(RobotMap.DIO.ELEVATOR_TOP_SWITCH);
            instance = new Lift(motor, encoder, bottomLimitSwitch, topLimitSwitch);
        }
        return instance;
    }

    public double getDistance() {
        return encoder.getDistance();
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return !(bottomLimitSwitch.get() && speed < 0) && !(topLimitSwitch.get() && speed > 0);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public void initDashboard() {
        liftNamespace.putNumber("encoder", encoder::get);
        liftNamespace.putBoolean("bottom limit switch", bottomLimitSwitch::get);
        liftNamespace.putBoolean("top limit switch", topLimitSwitch::get);
    }
}
