package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class Grip extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private Supplier<Double> appliedVoltage = intake::getAppliedVoltage;
    private Supplier<Double> actualVoltage = intake::getActualVoltage;

    public Grip() {
        addCommands(new MoveGenericSubsystem(intake, intake.gripSpeed).withInterrupt
                (() -> appliedVoltage.get() > actualVoltage.get()),
                new MoveGenericSubsystem(feeder, feeder::getProvidedSpeed).withTimeout(feeder.getFeedTime()));
    }

}
