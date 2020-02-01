package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcadeWithPID;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.control.PIDSettings;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.function.Supplier;

public class TurretFieldRelative extends SequentialCommandGroup {

    private Turret turret;
    private Drivetrain drivetrain;
    private Supplier<Double> setpoint;

    public TurretFieldRelative(Turret turret, Supplier<Double> setpoint, Supplier<Double> waitTime, Drivetrain drivetrain) {
        addCommands(new MoveTalonSubsystem(turret, () -> setpoint.get() - drivetrain.getHandler().getYaw(), waitTime));
        this.turret = turret;
        this.drivetrain = drivetrain;
        this.setpoint = setpoint;
    }

    @Override
    public void end(boolean interrupted) {
        if(turret.getAngle() + drivetrain.getHandler().getYaw() != setpoint.get()) {
            new DriveArcadeWithPID(drivetrain, new PIDSettings(0,0,0,0,0),
                    () -> drivetrain.getHandler().getYaw(), () -> setpoint.get() - turret
                    .getAngle(),
                    () -> 0.0).schedule();
        }
    }
}
