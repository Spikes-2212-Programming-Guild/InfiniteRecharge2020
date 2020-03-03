package com.spikes2212.frc2020.autonomous;

import com.spikes2212.frc2020.commands.ResetTurret;
import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CrossLineBackwards extends SequentialCommandGroup {

    private Drivetrain drivetrain = Drivetrain.getInstance();

    public CrossLineBackwards() {
        addCommands(
                new ResetTurret(),
                new DriveArcade(
                        drivetrain,
                        () -> -Drivetrain.autoForwardSpeed.get(),
                        () -> 0.0).withTimeout(Drivetrain.autoForwardTimeout.get()),
                new AutoShoot()
        );
    }
}
