package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.utils.HallEffectCounter;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Elevator extends GenericSubsystem {
    private static final Namespace elevator = new RootNamespace("elevator");
    private static final Namespace pidNamespace = elevator.addChild("PID");
    private static final Namespace feedForwardNamespace = elevator.addChild("feed forward");
    public static final Supplier<Double> distancePerPulse = elevator.addConstantDouble("distance per pulse", 0);
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    public static final PIDSettings PID_SETTINGS = new PIDSettings(kP, kI, kD);
    private static final Supplier<Double> kS = feedForwardNamespace.addConstantDouble("kS", 0);
    private static final Supplier<Double> kG = feedForwardNamespace.addConstantDouble("kG", 0);
    public static final FeedForwardSettings FEED_FORWARD_SETTINGS = new FeedForwardSettings(kS, () -> 0.0, () -> 0.0, kG);
    public static final Supplier<Integer> NUM_OF_MAGNETS = elevator.addConstantInt("num of magnets", 0);

    private static Elevator instance;
    private WPI_TalonSRX motor;
    private Encoder encoder;
    private DigitalInput bottomHallEffect;
    private HallEffectCounter hallEffectCounter;

    public static Elevator getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.ELEVATOR_TALON);
            Encoder encoder = new Encoder(RobotMap.DIO.ELEVATOR_ENCODER_POS, RobotMap.DIO.ELEVATOR_ENCODER_NEG);
            DigitalInput bottomHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_BOTTOM_SWITCH);
            DigitalInput topHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_TOP_SWITCH);
            instance = new Elevator(motor, encoder, bottomHallEffect, topHallEffect);
        }
        return instance;
    }

    private Elevator(WPI_TalonSRX motor, Encoder encoder, DigitalInput bottomHallEffect, DigitalInput hallEffectCounter) {
        this.motor = motor;
        this.encoder = encoder;
        this.bottomHallEffect = bottomHallEffect;
        this.hallEffectCounter = new HallEffectCounter(hallEffectCounter);
    }


    @Override
    public void periodic() {
        hallEffectCounter.update(motor.get());
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
        return !((bottomHallEffect.get() && speed < 0) &&
                !(hallEffectCounter.atTop(NUM_OF_MAGNETS.get()) &&
                        speed > 0));
    }

    public Supplier<Integer> getCurrentMagnet() {
        return hallEffectCounter::getCurrentMagnet;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void configureDashboard() {
        elevator.putNumber("encoder", encoder::get);
        elevator.putBoolean("bottom limit switch", bottomHallEffect::get);
        elevator.putNumber("top limit switch", hallEffectCounter::getCurrentMagnet);
    }
}
