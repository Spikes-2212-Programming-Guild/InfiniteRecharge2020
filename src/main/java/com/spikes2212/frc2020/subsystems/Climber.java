package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;

import java.util.function.Supplier;

public class Climber extends GenericSubsystem {

    public static RootNamespace climberNamespace = new RootNamespace("climber");

    public static final Supplier<Double> upSpeed = climberNamespace.addConstantDouble("climb speed", 1);
    public static final Supplier<Double> downSpeed = climberNamespace.addConstantDouble("unclimb speed", -1);

    private static Climber instance;

    public static Climber getInstance() {
        if (instance == null) {
            WPI_VictorSPX motor = new WPI_VictorSPX(RobotMap.CAN.CLIMBER_TALON);
            motor.setNeutralMode(NeutralMode.Brake);
            instance = new Climber(motor);
        }
        return instance;
    }

    private WPI_VictorSPX motor;

    public Climber(WPI_VictorSPX motor) {
        this.motor = motor;
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

    @Override
    public void periodic() {
        climberNamespace.update();
    }

    @Override
    public void configureDashboard() {
        climberNamespace.putData("move up", new MoveGenericSubsystem(this, upSpeed));
        climberNamespace.putData("move down", new MoveGenericSubsystem(this, downSpeed));
    }
}