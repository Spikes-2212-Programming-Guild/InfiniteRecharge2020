package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederStateMachine extends StateMachine<FeederStateMachine.FeederState> {

    enum FeederState {
        SHOOTER, LEVEL_1, OFF,
    }

    private static FeederStateMachine instance;
    public static FeederStateMachine getInstance() {
        if (instance == null)
            instance = new FeederStateMachine();
        return instance;
    }

    private FeederStateMachine() {
        super(FeederState.SHOOTER);
    }

    private Feeder feeder = Feeder.getInstance();

    @Override
    protected void generateTransformations() {
        addTransformation(FeederState.SHOOTER, new InstantCommand(feeder::close, feeder));
        addTransformation(FeederState.LEVEL_1, new InstantCommand(feeder::open, feeder));
        addTransformation(FeederState.OFF, new InstantCommand(()-> feeder.setEnabled(false)));
    }

}