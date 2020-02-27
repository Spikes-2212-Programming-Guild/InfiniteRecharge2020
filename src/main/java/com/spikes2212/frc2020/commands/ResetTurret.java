package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ResetTurret extends SequentialCommandGroup {

    private Turret turret = Turret.getInstance();

    public ResetTurret() {
        addCommands(
                new MoveGenericSubsystem(turret, -1),
                new MoveTalonSubsystem(turret, Turret.frontAngle, Turret.waitTime)
        );
    }   
}
