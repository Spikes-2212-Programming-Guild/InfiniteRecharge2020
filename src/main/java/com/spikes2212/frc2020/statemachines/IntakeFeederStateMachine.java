package com.spikes2212.frc2020.statemachines;

import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import static com.spikes2212.frc2020.statemachines.FeederStateMachine.FeederState;
import static com.spikes2212.frc2020.statemachines.IntakeStateMachine.IntakeState;

public class IntakeFeederStateMachine extends StateMachine <IntakeFeederStateMachine.IntakeFeederState> {

    public enum IntakeFeederState {
        CLOSE_INTAKE_CLOSE_FEEDER,
        CLOSE_INTAKE_OPEN_FEEDER,
        OPEN_INTAKE_CLOSE_FEEDER,
        OPEN_INTAKE_OPEN_FEEDER;
    }

    private static IntakeFeederStateMachine instance;

    public static IntakeFeederStateMachine getInstance() {
        if (instance == null)
            instance = new IntakeFeederStateMachine();
        return instance;
    }

    private IntakeFeederStateMachine() {
        super(IntakeFeederState.CLOSE_INTAKE_CLOSE_FEEDER);
    }

    private IntakeStateMachine intakeFSM = IntakeStateMachine.getInstance();
    private FeederStateMachine feederFSM = FeederStateMachine.getInstance();

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeFeederState.CLOSE_INTAKE_OPEN_FEEDER,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.CLOSE),
                        feederFSM.getTransformationFor(FeederState.FEED_TO_LVL_1)));
        addTransformation(IntakeFeederState.CLOSE_INTAKE_CLOSE_FEEDER,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.CLOSE),
                        feederFSM.getTransformationFor(FeederState.FEEDS_TO_SHOOTER)));
        addTransformation(IntakeFeederState.OPEN_INTAKE_OPEN_FEEDER,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.OPEN),
                        feederFSM.getTransformationFor(FeederState.FEED_TO_LVL_1)));;
        addTransformation(IntakeFeederState.OPEN_INTAKE_CLOSE_FEEDER,
                new ParallelCommandGroup(intakeFSM.getTransformationFor(IntakeState.OPEN),
                        feederFSM.getTransformationFor(FeederState.FEEDS_TO_SHOOTER)));
    }
}
