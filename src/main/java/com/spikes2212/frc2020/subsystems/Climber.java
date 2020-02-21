package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;

import java.util.function.Supplier;

public class Climber extends GenericSubsystem {

    public static RootNamespace climberNamespace = new RootNamespace("climber");

    private Supplier<Double> minSpeed = climberNamespace.addConstantDouble("min speed", 0);
    private Supplier<Double> maxSpeed = climberNamespace.addConstantDouble("max speed", 0);

    private Supplier<Double> climbSpeed = climberNamespace.addConstantDouble("climb speed", 0);

    private static Climber instance;

    public static Climber getInstance() {
        if (instance == null) {
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.CLIMBER_TALON);
            instance = new Climber(motor);
        }
        return instance;
    }

    private WPI_TalonSRX motor;

    private boolean enabled;

    public Climber(WPI_TalonSRX motor) {
        this.motor = motor;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return enabled;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void periodic() {
        climberNamespace.update();
    }

    @Override
    public void configureDashboard() {
        climberNamespace.putData("move", new MoveGenericSubsystem(this, climbSpeed));
    }
}