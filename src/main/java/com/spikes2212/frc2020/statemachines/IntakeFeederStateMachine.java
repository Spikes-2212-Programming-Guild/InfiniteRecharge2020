package com.spikes2212.frc2020.statemachines;

import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import static com.spikes2212.frc2020.statemachines.FeederStateMachine.FeederState;
import static com.spikes2212.frc2020.statemachines.IntakeStateMachine.IntakeState;

public class IntakeFeederStateMachine extends StateMachine <IntakeFeederStateMachine.IntakeFeederState> {
    private IntakeStateMachine intake = IntakeStateMachine.getInstance();
    private FeederStateMachine feeder = FeederStateMachine.getInstance();

    public enum IntakeFeederState {
        CLOSE_INTAKE_CLOSE_FEEDER,
        CLOSE_INTAKE_OPEN_FEEDER,
        OPEN_INTAKE_CLOSE_FEEDER,
        OPEN_INTAKE_OPEN_FEEDER
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

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeFeederState.CLOSE_INTAKE_OPEN_FEEDER,
                new ParallelCommandGroup(intake.getTransformationFor(IntakeState.CLOSE),
                        feeder.getTransformationFor(FeederState.OPEN)));
        addTransformation(IntakeFeederState.CLOSE_INTAKE_CLOSE_FEEDER,
                new ParallelCommandGroup(intake.getTransformationFor(IntakeState.CLOSE),
                        feeder.getTransformationFor(FeederState.CLOSE)));
        addTransformation(IntakeFeederState.OPEN_INTAKE_OPEN_FEEDER,
                new ParallelCommandGroup(intake.getTransformationFor(IntakeState.OPEN),
                        feeder.getTransformationFor(FeederState.OPEN)));;
        addTransformation(IntakeFeederState.OPEN_INTAKE_CLOSE_FEEDER,
                new ParallelCommandGroup(intake.getTransformationFor(IntakeState.OPEN),
                        feeder.getTransformationFor(FeederState.CLOSE)));
    }
}
