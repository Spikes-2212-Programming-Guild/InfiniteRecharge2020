package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class FeedToLowTarget extends SequentialCommandGroup {

    private Feeder feeder = Feeder.getInstance();
    private Turret turret = Turret.getInstance();
    private double setpoint = Turret.frontAngle.get();

    public FeedToLowTarget() {
        addCommands(
                new MoveTalonSubsystem(turret, setpoint, Turret.waitTime),
                new InstantCommand(feeder::open)
        );
    }

}
