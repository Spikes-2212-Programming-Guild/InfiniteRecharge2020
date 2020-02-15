package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeStateMachine extends StateMachine<IntakeStateMachine.IntakeState> {

    enum IntakeState {
        CLOSE, OPEN,
    }

    private static IntakeStateMachine instance;

    public static IntakeStateMachine getInstance() {
        if(instance == null) {
            instance = new IntakeStateMachine();
        }

        return instance;
    }

    private IntakeStateMachine() {
        super(IntakeState.CLOSE);
    }

    private Intake intake = Intake.getInstance();

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeState.CLOSE, new InstantCommand(intake::close, intake));
        addTransformation(IntakeState.OPEN, new InstantCommand(intake::open, intake));
    }
}
