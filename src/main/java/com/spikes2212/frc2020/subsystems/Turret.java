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
import com.spikes2212.lib.util.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem implements TalonSubsystem {

    private static final double DEGREES_TO_PULSES = 11*4096*Math.PI/9/180;

    public static final Namespace turretNamespace = new RootNamespace("Turret");

    public static final Namespace PID = turretNamespace.addChild("PID");

    public static final Supplier<Double> MAX_SPEED = turretNamespace.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> MIN_SPEED = turretNamespace.addConstantDouble("Min Speed", -0.6);

    public static final Supplier<Double> K_P = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> K_I = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> K_D = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> TOLERANCE = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> WAIT_TIME = PID.addConstantDouble("Wait Time", 0);
    public static final Supplier<Double> SETPOINT = PID.addConstantDouble("setpoint", 90);
    public static final Supplier<Integer> TIMEOUT = PID.addConstantInt("timeout", 30);

    PIDSettings pidSettings = new PIDSettings(K_P, K_I, K_D, TOLERANCE, WAIT_TIME);

    private WPI_TalonSRX motor;
    private TalonEncoder encoder;
    private DigitalInput firstLimit;
    private DigitalInput secondLimit;

    private Turret(WPI_TalonSRX motor, DigitalInput firstLimit, DigitalInput secondLimit) {
        super(MIN_SPEED, MAX_SPEED);
        this.motor = motor;
        this.firstLimit = firstLimit;
        this.secondLimit = secondLimit;
        encoder = new TalonEncoder(this.motor);
    }

    private static Turret instance;

    public static Turret getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
            DigitalInput firstLimit = new DigitalInput(RobotMap.DIO.START_LIMIT);
            DigitalInput secondLimit = new DigitalInput(RobotMap.DIO.END_LIMIT);
            instance = new Turret(motor, firstLimit, secondLimit);
        }

        return instance;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return (speed > 0 && !firstLimit.get()) || (speed < 0 && !secondLimit.get());
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public boolean isLeft(){
        return secondLimit.get();
    }

    public boolean isRight(){
        return firstLimit.get();
    }

    public void setDistancePerPulse(double distancePerPulse) {
        encoder.setDistancePerPulse(distancePerPulse);
    }

    public double getDistancePerPulse() {
        return encoder.getDistancePerPulse();
    }

    @Override
    public void configureDashboard(){
        turretNamespace.putData("rotate", new MoveTalonSubsystem(this, SETPOINT, WAIT_TIME));
    }

    @Override
    public void configureLoop() {
        motor.configFactoryDefault();
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TIMEOUT.get());

        motor.configNominalOutputForward(0, TIMEOUT.get());
        motor.configNominalOutputReverse(0, TIMEOUT.get());
        motor.configPeakOutputForward(MAX_SPEED.get(), TIMEOUT.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), TIMEOUT.get());

        motor.configAllowableClosedloopError(0,0,TIMEOUT.get());
        motor.config_kP(0, K_P.get(), TIMEOUT.get());
        motor.config_kI(0, K_I.get(), TIMEOUT.get());
        motor.config_kD(0, K_D.get(), TIMEOUT.get());
    }

    @Override
    public void pidSet(double setpoint) {
        if(setpoint < 30) setpoint = 30;
        if(setpoint > 330) setpoint = 330;

        setpoint *= DEGREES_TO_PULSES;

        motor.configPeakOutputForward(MAX_SPEED.get(), TIMEOUT.get());
        motor.configPeakOutputReverse(MIN_SPEED.get(), TIMEOUT.get());

        motor.config_kP(0, K_P.get(), TIMEOUT.get());
        motor.config_kI(0, K_I.get(), TIMEOUT.get());
        motor.config_kD(0, K_D.get(), TIMEOUT.get());

        motor.set(ControlMode.Current, setpoint);
    }

    @Override
    public void finish() {
        stop();
    }

    @Override
    public boolean onTarget(double setpoint) {
        if(setpoint < 30) setpoint = 30;
        if(setpoint > 330) setpoint = 330;

        setpoint *= DEGREES_TO_PULSES;

        double tolerance = TOLERANCE.get() * DEGREES_TO_PULSES;
        int position = motor.getSelectedSensorPosition();

        return !canMove(motor.get()) || Math.abs(setpoint - position) < tolerance;
    }
}
