package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeStateMachine extends StateMachine<IntakeStateMachine.IntakeState> {
    enum IntakeState {
        CLOSE, OPEN
    }

    private static IntakeStateMachine instance;

    public static IntakeStateMachine getInstance() {
        if (instance == null)
            instance = new IntakeStateMachine();
        return instance;
    }

    private IntakeStateMachine() {
        super(IntakeState.CLOSE);

    }

    @Override
    protected void generateTransformations() {
        addTransformation(IntakeState.CLOSE, new InstantCommand(Intake.getInstance() :: close));
        addTransformation(IntakeState.OPEN, new InstantCommand(Intake.getInstance() :: open));
    }

}