package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CloseRangeShoot extends SequentialCommandGroup {

    private Shooter shooter = Shooter.getInstance();
    private Feeder feeder = Feeder.getInstance();

    public CloseRangeShoot() {
        addCommands(
                new OrientTurretToPowerPort(),
                new InstantCommand(shooter::open),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        Shooter.closeShootingSpeed, shooter::getMotorSpeed, Shooter.velocityFFSettings),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        Shooter.closeShootingSpeed, shooter::getMotorSpeed, Shooter.velocityFFSettings).alongWith(
                        new MoveGenericSubsystem(feeder, Feeder.speed)
                )
        );
    }
}
