package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CloseRangeShoot extends SequentialCommandGroup {

    private Shooter shooter = Shooter.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private Turret turret = Turret.getInstance();


    public CloseRangeShoot() {
        addCommands(
//                new OrientTurretToPowerPort(),
                new InstantCommand(shooter::open),
                new MoveTalonSubsystem(turret, Turret.frontAngle, Turret.waitTime),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        Shooter.closeShootingSpeed, shooter::getMotorSpeed, Shooter.velocityFFSettings),
                new InstantCommand(() -> shooter.setAccelerated(true)),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        Shooter.closeShootingSpeed, shooter::getMotorSpeed, Shooter.velocityFFSettings).perpetually()
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        shooter.setAccelerated(false);
    }

}
