package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederStateMachine extends StateMachine<FeederStateMachine.FeederState> {
    enum FeederState {
        CLOSE,OPEN,
    }

    private static FeederStateMachine instance;

    public static FeederStateMachine getInstance() {
        if (instance == null)
            instance = new FeederStateMachine();
        return instance;
    }

    private FeederStateMachine() {
        super(FeederState.CLOSE);

    }

    @Override
    protected void generateTransformations() {
        addTransformation(FeederState.CLOSE, new InstantCommand(Feeder.getInstance()::close));
        addTransformation(FeederState.OPEN, new InstantCommand(Feeder.getInstance()::open));
    }

}