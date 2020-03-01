package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MoveTurretToFieldRelativeAngle extends SequentialCommandGroup {

    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Turret turret = Turret.getInstance();

    private double targetAngle = 0;

    public MoveTurretToFieldRelativeAngle() {
        addCommands(
                new InstantCommand(() -> targetAngle = findTargetAngle()),
                new MoveTalonSubsystem(turret, () -> targetAngle, () -> 0.0).perpetually()
                    .alongWith(new RepeatCommand(
                            new InstantCommand(this::findTargetAngle)
                    ))
        );
    }

    private double findTargetAngle() {
        return turret.getYaw() - drivetrain.getYaw();
    }
}
