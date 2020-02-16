package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

import static com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine.IntakeFeederState;

public class Grip extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private IntakeFeederStateMachine intakeFeederFSM = IntakeFeederStateMachine.getInstance();

    private Supplier<Double> intakeVoltage = Intake.intakeVoltage;
    private Supplier<Double> intakeCurrentLimit = Intake.intakeCurrentLimit;

    private Supplier<Double> feederSpeed = Feeder.speed;
    private Supplier<Double> feedTimeLimit = Feeder.feedTimeLimit;

    public Grip() {
        addCommands(
                intakeFeederFSM.getTransformationFor(IntakeFeederState.COLLECT),
                new MoveGenericSubsystem(intake, intakeVoltage).withInterrupt
                        (() -> intake.getSuppliedCurrent() - intake.getStatorCurrent() >= intakeCurrentLimit.get()),
                intakeFeederFSM.getTransformationFor(IntakeFeederState.FEED_TO_SHOOTER),

                new MoveGenericSubsystem(intake, intakeVoltage).deadlineWith(
                        new MoveGenericSubsystem(feeder, feederSpeed).withTimeout(feedTimeLimit.get())
                ).withInterrupt(intake::limitPressed),
                intakeFeederFSM.getTransformationFor(IntakeFeederState.OFF));
    }

}
