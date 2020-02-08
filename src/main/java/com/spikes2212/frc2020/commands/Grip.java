package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class Grip extends SequentialCommandGroup {

    private Intake intake = Intake.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private Supplier<Double> appliedVoltage = intake.getSuppliedCurrent();
    private Supplier<Double> actualVoltage = intake.getStatorCurrent();

    public Grip() {
        addCommands(new MoveGenericSubsystem(intake, intake.getGripSpeed()).withInterrupt
                        (() -> appliedVoltage.get() - actualVoltage.get() > intake.getCurrentLimit().get())
                , ((new MoveGenericSubsystem(feeder, feeder::getProvidedSpeed)).deadlineWith
                        (new MoveGenericSubsystem(intake, intake.getGripSpeed()))).withTimeout(feeder.getFeedTime()));
    }

}
