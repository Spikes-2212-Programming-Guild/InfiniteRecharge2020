package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ShooterStateMachine extends StateMachine<ShooterStateMachine.FeederState> {
    enum FeederState {
        OFF, ON,
    }

    private static ShooterStateMachine instance;

    public static ShooterStateMachine getInstance() {
        if (instance == null)
            instance = new ShooterStateMachine();
        return instance;
    }

    private ShooterStateMachine() {
        super(FeederState.OFF);

    }

    @Override
    protected void generateTransformations() {
        addTransformation(FeederState.OFF, new MoveGenericSubsystem(Shooter.getInstance(), 0));
        addTransformation(FeederState.ON, new MoveGenericSubsystem(Shooter.getInstance(), Shooter.shootSpeed));
    }

}