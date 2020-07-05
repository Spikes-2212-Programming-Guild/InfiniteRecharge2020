package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Drivetrain;
//import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class MoveTurretToFieldRelativeAngle extends SequentialCommandGroup {

//    private Turret turret = Turret.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();

    private double targetTurretYaw = 0;
    private double drivetrainOffsetYaw = 0;

    public MoveTurretToFieldRelativeAngle() {
        addCommands(
                new InstantCommand(() -> {
//                    targetTurretYaw = turret.getYaw();
                    drivetrainOffsetYaw = drivetrain.getYaw();
                })
//                new MoveTalonSubsystem(turret, this::calculateSetpoint, () -> 0.0).perpetually()
        );
    }

    private double calculateSetpoint() {
        return targetTurretYaw - (drivetrain.getYaw() - drivetrainOffsetYaw);
    }
}
