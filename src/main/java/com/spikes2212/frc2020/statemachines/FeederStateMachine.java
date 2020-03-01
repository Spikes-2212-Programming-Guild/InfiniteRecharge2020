package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederStateMachine extends StateMachine<FeederStateMachine.FeederState> {

    public enum FeederState {
        FEED_TO_SHOOTER, FEED_TO_LVL_1, OFF

    }

    private static FeederStateMachine instance;

    public static FeederStateMachine getInstance() {
        if (instance == null) instance = new FeederStateMachine();
        return instance;
    }

    private static Feeder feeder = Feeder.getInstance();

    private FeederStateMachine() {
        super(FeederState.FEED_TO_SHOOTER);
    }

    @Override
    protected void generateTransformations() {
        addTransformation(FeederState.FEED_TO_SHOOTER, new InstantCommand(feeder::close, feeder));
        addTransformation(FeederState.FEED_TO_LVL_1, new InstantCommand(feeder::open, feeder));
        addTransformation(FeederState.OFF, new InstantCommand(() -> feeder.setEnabled(false), feeder));
    }
}
