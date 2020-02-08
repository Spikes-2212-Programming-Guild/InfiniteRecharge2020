package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.utils.HallEffectCounter;
import com.spikes2212.lib.command.genericsubsystem.TalonSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Elevator extends GenericSubsystem implements TalonSubsystem {

    private static final RootNamespace elevator = new RootNamespace("elevator");

    private static final Namespace pidNamespace = elevator.addChild("PID");
    private static final Supplier<Double> kP = pidNamespace.addConstantDouble("kP", 0);
    private static final Supplier<Double> kI = pidNamespace.addConstantDouble("kI", 0);
    private static final Supplier<Double> kD = pidNamespace.addConstantDouble("kD", 0);
    private static final Supplier<Double> waitTime = pidNamespace.addConstantDouble("wait time", 0);
    private static final Supplier<Integer> timeout = pidNamespace.addConstantInt("timeout", 0);
    public static Supplier<Double> setpoint = pidNamespace.addConstantDouble("setpoint", 0);
    public static final PIDSettings PID_SETTINGS = new PIDSettings(kP, kI, kD);

    private static final Namespace feedForwardNamespace = elevator.addChild("feed forward");
    private static final Supplier<Double> kS = feedForwardNamespace.addConstantDouble("kS", 0);
    private static final Supplier<Double> kG = feedForwardNamespace.addConstantDouble("kG", 0);
    public static final FeedForwardSettings FEED_FORWARD_SETTINGS = new FeedForwardSettings(kS, () -> 0.0, () -> 0.0, kG);

    public static final Supplier<Double> distancePerPulse = elevator.addConstantDouble("distance per pulse", 0);

    public static final Supplier<Integer> NUM_OF_MAGNETS = elevator.addConstantInt("num of magnets", 0);

    private static Elevator instance;

    public static Elevator getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.ELEVATOR_TALON);
            DigitalInput bottomHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_BOTTOM_SWITCH);
            DigitalInput topHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_TOP_SWITCH);
            instance = new Elevator(motor, bottomHallEffect, topHallEffect);
        }
        return instance;
    }

    private WPI_TalonSRX motor;
    private DigitalInput bottomHallEffect;

    private HallEffectCounter hallEffectCounter;

    private double lastTimeNotOnTarget = 0;

    private Elevator(WPI_TalonSRX motor, DigitalInput bottomHallEffect, DigitalInput hallEffectCounter) {
        this.motor = motor;
        this.bottomHallEffect = bottomHallEffect;
        this.hallEffectCounter = new HallEffectCounter(hallEffectCounter);
    }


    @Override
    public void periodic() {
        hallEffectCounter.update(motor.get());
        elevator.update();
    }

    public double getPosition() {
        return motor.getSelectedSensorPosition();
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return !(bottomHallEffect.get() && speed < 0 || hallEffectCounter.atTop(NUM_OF_MAGNETS.get() - 1)
                && speed > 0);
    }

    public int getCurrentMagnet() {
        return hallEffectCounter.getCurrentMagnet();
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void configureDashboard() {
        elevator.putNumber("encoder", motor::getSelectedSensorPosition);
        elevator.putBoolean("bottom limit switch", bottomHallEffect::get);
        elevator.putNumber("current magnet", this::getCurrentMagnet);
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, timeout.get());
        motor.setSelectedSensorPosition(0, 0, timeout.get());

        motor.configNominalOutputForward(0, timeout.get());
        motor.configNominalOutputReverse(0, timeout.get());
        motor.configPeakOutputForward(1, timeout.get());
        motor.configPeakOutputReverse(-1, timeout.get());

        motor.configAllowableClosedloopError(0, 0, timeout.get());
        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());
    }

    @Override
    public void pidSet(double setpoint) {

        motor.config_kP(0, kP.get(), timeout.get());
        motor.config_kI(0, kI.get(), timeout.get());
        motor.config_kD(0, kD.get(), timeout.get());

        motor.set(ControlMode.Position, setpoint);


    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        return !canMove(motor.getMotorOutputPercent()) || setpoint == motor.getSelectedSensorPosition();
    }
}
