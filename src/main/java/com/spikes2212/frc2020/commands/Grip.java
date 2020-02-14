package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import static com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine.IntakeFeederState;

public class Grip extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private IntakeFeederStateMachine intakeFeederFSM = IntakeFeederStateMachine.getInstance();

    public Grip() {
        addCommands(
                intakeFeederFSM.getTransformationFor(IntakeFeederState.COLLECT),
                new MoveGenericSubsystem(intake, intake.getGripSpeed()).withInterrupt
                        (() -> intake.getSuppliedCurrent() - intake.getStatorCurrent() >= intake.getCurrentLimit()),
                intakeFeederFSM.getTransformationFor(IntakeFeederState.FEED_TO_SHOOTER),
                ((new MoveGenericSubsystem(feeder, feeder::getProvidedSpeed)).deadlineWith
                        (new MoveGenericSubsystem(intake, intake.getGripSpeed()))).withTimeout(feeder.getFeedTime()),
                intakeFeederFSM.getTransformationFor(IntakeFeederState.OFF));
    }

}
