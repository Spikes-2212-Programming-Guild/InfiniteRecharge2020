package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederStateMachine extends StateMachine<FeederStateMachine.FeederState> {
    enum FeederState {
        OFF, ON, OPEN
    }

    private static FeederStateMachine instance;

    public static FeederStateMachine getInstance() {
        if (instance == null)
            instance = new FeederStateMachine();
        return instance;
    }

    private FeederStateMachine() {
        super(FeederState.OFF);

    }

    @Override
    protected void generateTransformations() {
        addTransformation(FeederState.OFF, new MoveGenericSubsystem(Feeder.getInstance(), 0));
        addTransformation(FeederState.ON, new MoveGenericSubsystem(Feeder.getInstance(), Feeder.speed));
        addTransformation(FeederState.OPEN, new InstantCommand(Feeder.getInstance()::open));
    }

}