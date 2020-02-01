package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import static com.spikes2212.frc2020.statemachines.IntakeStateMachine.IntakeState;
import static com.spikes2212.frc2020.statemachines.FeederStateMachine.FeederState;

public class IntakeFeederStateMachine extends StateMachine <IntakeFeederStateMachine.IntakeFeederState> {
    @Override
    protected void generateTransformations() {
        addTransformation(IntakeFeederState.CLOSE_INTAKE_OPEN_FEEDER, new ParallelCommandGroup(IntakeStateMachine.getInstance().getTransformationFor(IntakeState.CLOSE), FeederStateMachine.getInstance().getTransformationFor(FeederState.OPEN)));
        addTransformation(IntakeFeederState.CLOSE_INTAKE_CLOSE_FEEDER, new ParallelCommandGroup(IntakeStateMachine.getInstance().getTransformationFor(IntakeState.CLOSE), FeederStateMachine.getInstance().getTransformationFor(FeederState.CLOSE)));
        addTransformation(IntakeFeederState.OPEN_INTAKE_OPEN_FEEDER, new ParallelCommandGroup(IntakeStateMachine.getInstance().getTransformationFor(IntakeState.OPEN), FeederStateMachine.getInstance().getTransformationFor(FeederState.OPEN)));;

    }

    public enum IntakeFeederState {
        CLOSE_INTAKE_CLOSE_FEEDER,
        CLOSE_INTAKE_OPEN_FEEDER,
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
}
