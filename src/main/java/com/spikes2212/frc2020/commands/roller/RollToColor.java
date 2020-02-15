package com.spikes2212.frc2020.commands.roller;

import com.spikes2212.frc2020.subsystems.Roller;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class RollToColor extends SequentialCommandGroup {

    private Roller roller = Roller.getInstance();

    public RollToColor(Supplier<Double> forwardSpeed, Supplier<Double> rollSpeed, Color color,
                       Supplier<Double> waitTime) {
        addCommands(new DriveToTrench(forwardSpeed), new MoveTalonSubsystem(roller, roller.getSetpoint(color),
                waitTime));
    }
}
