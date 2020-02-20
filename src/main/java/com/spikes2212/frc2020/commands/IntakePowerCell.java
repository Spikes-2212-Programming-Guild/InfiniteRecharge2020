package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.function.Supplier;

import static com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine.IntakeFeederState;

public class IntakePowerCell extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private IntakeFeederStateMachine intakeFeederFSM = IntakeFeederStateMachine.getInstance();

    private Supplier<Double> intakeVoltage = Intake.intakeVoltage;
    private Supplier<Double> intakeCurrentLimit = Intake.intakeCurrentLimit;

    private Supplier<Double> feederSpeed = Feeder.speed;
    private Supplier<Double> feedTimeLimit = Feeder.feedTimeLimit;

    public IntakePowerCell() {
        addCommands(
                new MoveGenericSubsystem(intake, intakeVoltage).withInterrupt
                        (() -> intake.getSuppliedCurrent() >= intakeCurrentLimit.get()),
                new MoveGenericSubsystem(intake, intakeVoltage)
                        .withInterrupt(() -> intake.getSuppliedCurrent() <= 12)
                        .alongWith(
                                new MoveGenericSubsystem(feeder, feederSpeed).withTimeout(feedTimeLimit.get()
                                ))
        );
    }

}
