package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.lib.control.PIDLoop;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.control.TalonPIDLoop;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.util.TalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem {

    public static final Namespace turretNamespace = new RootNamespace("Turret");


    public static final Namespace PID = turretNamespace.addChild("PID");

    public static final Supplier<Double> MAX_SPEED = turretNamespace.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> MIN_SPEED = turretNamespace.addConstantDouble("Min Speed", -0.6);

    public static final Supplier<Double> K_P = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> K_I = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> K_D = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> TOLERANCE = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> WAIT_TIME = PID.addConstantDouble("Wait Time", 0);
    public static final Supplier<Double> SETPOINT = PID.addConstantDouble("setpoint", 0);
    private static Turret instance;

    public PIDLoop pidLoop;

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
        pidLoop = new TalonPIDLoop(motor, pidSettings, MAX_SPEED);
    }

    public static Turret getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.TURRET_TALON);
            DigitalInput firstLimit = new DigitalInput(RobotMap.DIO.TURRET_FIRST_LIMIT);
            DigitalInput secondLimit = new DigitalInput(RobotMap.DIO.TURRET_SECOND_LIMIT);
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

    public void initDashboardTesting(){
        turretNamespace.putData("move turret to setpoint", new MoveGenericSubsystemWithPID(this, pidLoop, SETPOINT));
    }

}
