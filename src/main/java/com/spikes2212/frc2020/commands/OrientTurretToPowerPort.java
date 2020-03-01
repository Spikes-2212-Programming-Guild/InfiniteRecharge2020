package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class OrientTurretToPowerPort extends SequentialCommandGroup {


    private VisionService vision = VisionService.getInstance();
    private Turret turret = Turret.getInstance();
    private double setpoint = 0;

    public OrientTurretToPowerPort() {
        addCommands(
                new MoveTalonSubsystem(turret, () -> setpoint, Turret.waitTime)
                        .deadlineWith(
                                new RepeatCommand(
                                        new InstantCommand(this::updateSetpoint),
                                        new WaitCommand(0.1)
                                )
                        )
        );
    }

    private void updateSetpoint() {
        setpoint = turret.getYaw() - vision.getRetroReflectiveYaw();
    }
}
