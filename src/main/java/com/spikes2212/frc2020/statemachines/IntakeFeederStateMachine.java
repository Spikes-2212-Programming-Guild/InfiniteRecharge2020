package com.spikes2212.frc2020.statemachines;

import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import static com.spikes2212.frc2020.statemachines.FeederStateMachine.FeederState;
import static com.spikes2212.frc2020.statemachines.IntakeStateMachine.IntakeState;

public class IntakeFeederStateMachine extends StateMachine<IntakeFeederStateMachine.IntakeFeederState> {



    public enum IntakeFeederState {
        OFF, FEED_TO_LEVEL_1, COLLECT, FEED_TO_SHOOTER;
    }

    private static IntakeFeederStateMachine instance;
    public static IntakeFeederStateMachine getInstance() {
        if (instance == null) {
            instance = new IntakeFeederStateMachine();
        }

        return instance;
    }

    private IntakeStateMachine intakeFSM = IntakeStateMachine.getInstance();
    private FeederStateMachine feederFSM = FeederStateMachine.getInstance();

    private IntakeFeederStateMachine() {
        super(IntakeFeederState.OFF);
    }

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeFeederState.OFF,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.CLOSE),
                        feederFSM.getTransformationFor(FeederState.OFF)
                ));
        addTransformation(IntakeFeederState.FEED_TO_LEVEL_1,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.CLOSE),
                        feederFSM.getTransformationFor(FeederState.FEED_TO_LVL_1)));
        addTransformation(IntakeFeederState.FEED_TO_SHOOTER,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.CLOSE),
                        feederFSM.getTransformationFor(FeederState.FEED_TO_SHOOTER)));
        addTransformation(IntakeFeederState.COLLECT,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.OPEN),
                        feederFSM.getTransformationFor(FeederState.FEED_TO_SHOOTER)));
    }
}
