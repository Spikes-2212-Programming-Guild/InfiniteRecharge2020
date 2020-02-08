package com.spikes2212.frc2020.statemachines;

import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import static com.spikes2212.frc2020.statemachines.FeederStateMachine.FeederState;
import static com.spikes2212.frc2020.statemachines.IntakeStateMachine.IntakeState;

public class IntakeFeederStateMachine extends StateMachine<IntakeFeederStateMachine.IntakeFeederState> {

    public static RootNamespace intakeFeederStateNamespace = new RootNamespace("intake feeder state");

    public enum IntakeFeederState {
        OFF, FEED_TO_LEVEL_1, COLLECT, FEED_TO_SHOOTER
    }

    private static IntakeFeederStateMachine instance;

    public static IntakeFeederStateMachine getInstance() {
        if (instance == null) {
            instance = new IntakeFeederStateMachine();
        }

        return instance;
    }

    private static IntakeStateMachine intakeFSM = IntakeStateMachine.getInstance();
    private static FeederStateMachine feederFSM = FeederStateMachine.getInstance();

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

    public void configureDashboard() {
        intakeFeederStateNamespace.putString("state", getState()::name);
        intakeFeederStateNamespace.putData("turn off", (Sendable) getTransformationFor(IntakeFeederState.OFF));
        intakeFeederStateNamespace.putData("feed to level 1", (Sendable) getTransformationFor(IntakeFeederState.FEED_TO_LEVEL_1));
        intakeFeederStateNamespace.putData("feed to shooter", (Sendable) getTransformationFor(IntakeFeederState.FEED_TO_SHOOTER));
        intakeFeederStateNamespace.putData("collect", (Sendable) getTransformationFor(IntakeFeederState.COLLECT));
    }
}
