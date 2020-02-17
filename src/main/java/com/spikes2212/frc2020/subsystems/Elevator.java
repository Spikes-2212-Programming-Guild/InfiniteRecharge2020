package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.utils.HallEffectCounter;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Elevator extends GenericSubsystem {

    private static final RootNamespace elevatorNamspace = new RootNamespace("elevator");
    private static final Namespace pidNamespace = elevatorNamspace.addChild("PID");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private static final Supplier<Double> kS = pidNamespace.addConstantDouble("kS", 0);
    private static final Supplier<Double> kG = pidNamespace.addConstantDouble("kG", 0);

    public static final PIDSettings PID_SETTINGS = new PIDSettings(kP, kI, kD);

    public static final FeedForwardSettings FEED_FORWARD_SETTINGS =
            new FeedForwardSettings(kS, () -> 0.0, () -> 0.0, kG);

    public static final Supplier<Double> distancePerPulse = elevatorNamspace
            .addConstantDouble("distance per pulse", 0);

    public static final Supplier<Integer> NUM_OF_MAGNETS = elevatorNamspace
            .addConstantInt("num of magnets", 0);

    private static Elevator instance;

    public static Elevator getInstance() {
        if(instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.ELEVATOR_TALON);
            Encoder encoder = new Encoder(RobotMap.DIO.ELEVATOR_ENCODER_POS,
                    RobotMap.DIO.ELEVATOR_ENCODER_NEG);
            //DigitalInput bottomHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_BOTTOM_SWITCH);
           // DigitalInput topHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_TOP_SWITCH);
            //instance = new Elevator(motor, encoder, bottomHallEffect, topHallEffect);
        }

        return instance;
    }

    private WPI_TalonSRX motor;
    private Encoder encoder;
    private DigitalInput bottomHallEffect;
    private HallEffectCounter hallEffectCounter;

    private Elevator(WPI_TalonSRX motor, Encoder encoder, DigitalInput bottomHallEffect,
                     DigitalInput hallEffectCounter) {
        this.motor = motor;
        this.encoder = encoder;
        this.bottomHallEffect = bottomHallEffect;
        this.hallEffectCounter = new HallEffectCounter(hallEffectCounter);
    }

    @Override
    public void periodic() {
        hallEffectCounter.update(motor.get());
        elevatorNamspace.update();
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
        return !((bottomHallEffect.get() && speed < 0)
                && !(hallEffectCounter.atTop(NUM_OF_MAGNETS.get()) && speed > 0));
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
        elevatorNamspace.putNumber("encoder", encoder::get);
        elevatorNamspace.putBoolean("bottom limit switch", bottomHallEffect::get);
        elevatorNamspace.putNumber("top limit switch", hallEffectCounter::getCurrentMagnet);
    }
}
