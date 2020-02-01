package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;

public class ShooterStateMachine extends StateMachine<ShooterStateMachine.ShooterState> {
    enum ShooterState {
        OFF, ON,
    }

    private static ShooterStateMachine instance;

    public static ShooterStateMachine getInstance() {
        if (instance == null)
            instance = new ShooterStateMachine();
        return instance;
    }

    private ShooterStateMachine() {
        super(ShooterState.OFF);

    }

    @Override
    protected void generateTransformations() {
        addTransformation(ShooterState.OFF, new MoveGenericSubsystem(Shooter.getInstance(), 0));
        addTransformation(ShooterState.ON, new MoveGenericSubsystem(Shooter.getInstance(), Shooter.shootSpeed));
    }

}