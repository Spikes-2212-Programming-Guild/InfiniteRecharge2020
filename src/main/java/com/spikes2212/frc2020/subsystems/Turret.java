package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.PIDLoop;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Turret extends GenericSubsystem {

    public static final Namespace NAMESPACE = new RootNamespace("Turret");


    public static final Namespace PID = NAMESPACE.addChild("PID");

    public static final Supplier<Double> MAX_SPEED = NAMESPACE.addConstantDouble("Max Speed", 0.6);
    public static final Supplier<Double> MIN_SPEED = NAMESPACE.addConstantDouble("Min Speed", -0.6);

    public static final Supplier<Double> K_P = PID.addConstantDouble("kP", 0);
    public static final Supplier<Double> K_I = PID.addConstantDouble("kI", 0);
    public static final Supplier<Double> K_D = PID.addConstantDouble("kD", 0);
    public static final Supplier<Double> TOLERANCE = PID.addConstantDouble("Tolerance", 0);
    public static final Supplier<Double> WAIT_TIME = PID.addConstantDouble("Wait Time", 0);

    private static Turret instance;

    public PIDLoop pidLoop;

    PIDSettings pidSettings = new PIDSettings(K_P, K_I, K_D, TOLERANCE, WAIT_TIME);

    private WPI_TalonSRX motor;
    private DigitalInput firstLimit;
    private DigitalInput secondLimit;

    private Turret(WPI_TalonSRX motor, DigitalInput firstLimit, DigitalInput secondLimit) {
        super(MIN_SPEED, MAX_SPEED);
        this.motor = motor;
        this.firstLimit = firstLimit;
        this.secondLimit = secondLimit;

//        pidLoop = new TalonPIDLoop(motor, pidSettings, MAX_SPEED);
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
        if (speed > 0)
            return !firstLimit.get();
        if (speed < 0)
            return !secondLimit.get();

        return true;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }
}
