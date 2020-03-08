package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class IntakePowerCell extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();

    private Supplier<Double> intakeVoltage = Intake.intakeVoltage;
    private Supplier<Double> intakeCurrentLimit = Intake.intakeCurrentLimit;

    private Supplier<Double> feederSpeed = Feeder.speed;
    private Supplier<Double> feedTimeLimit = Feeder.feedTimeLimit;

    public IntakePowerCell() {
        addCommands(
                new MoveGenericSubsystem(intake, intakeVoltage).withInterrupt
                        (() -> intake.getSuppliedCurrent() >= intakeCurrentLimit.get()),
                new MoveGenericSubsystem(intake, intakeVoltage).withTimeout(0.2),
                new InstantCommand(() -> feeder.reset()),
                new MoveGenericSubsystemWithPID(feeder, Feeder.setpoint, feeder::getPosition, Feeder.pidSettings).withTimeout(
                        feedTimeLimit.get())
                );
    }

}
