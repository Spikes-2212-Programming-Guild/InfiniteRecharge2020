package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeStateMachine extends StateMachine<IntakeStateMachine.IntakeState> {

    public enum IntakeState {
        DISABLED, ENABLED
    }

    private static IntakeStateMachine instance;
    public static IntakeStateMachine getInstance() {
        if(instance == null) {
            instance = new IntakeStateMachine();
        }

        return instance;
    }

    private Intake intake = Intake.getInstance();

    private IntakeStateMachine() {
        super(IntakeState.DISABLED);
    }

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeState.ENABLED, new MoveGenericSubsystem(intake, intake.intakeVoltage.get()));
        addTransformation(IntakeState.DISABLED, new MoveGenericSubsystem(intake, 0));
    }
}
