package com.spikes2212.frc2020.subsystems;

import com.spikes2212.frc2020.ColorDetector;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.VictorSP;

import java.util.function.Supplier;

public class Roller extends GenericSubsystem {

    public static Namespace rollerNamespace = new RootNamespace("roller");

    public static Supplier<Double> MIN_SPEED = rollerNamespace.addConstantDouble("min speed", -1);
    public static Supplier<Double> MAX_SPEED = rollerNamespace.addConstantDouble("max speed", 1);

    private VictorSP motor;
    private ColorDetector detector;

    private static Roller instance;

    public static Roller getInstance() {
        if (instance == null) {
            VictorSP motor = new VictorSP(RobotMap.PWM.ROLLER_MOTOR);
            ColorDetector colorDetector = new ColorDetector(I2C.Port.kOnboard);
            instance = new Roller(MIN_SPEED, MAX_SPEED, motor, colorDetector);
        }
        return instance;
    }

    private Roller(Supplier<Double> minSpeed, Supplier<Double> maxSpeed, VictorSP motor, ColorDetector detector) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.detector = detector;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }
}
