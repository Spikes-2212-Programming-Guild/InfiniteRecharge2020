package com.spikes2212.frc2020.autonomous;

//import com.spikes2212.frc2020.commands.ResetTurret;
import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CrossLine extends SequentialCommandGroup {

    private Drivetrain drivetrain = Drivetrain.getInstance();

    public CrossLine() {
        addCommands(
//                new ResetTurret(),
                new DriveArcade(
                        drivetrain,
                        Drivetrain.autoForwardSpeed,
                        () -> 0.0).withTimeout(Drivetrain.autoForwardTimeout.get())
        );
    }
}
