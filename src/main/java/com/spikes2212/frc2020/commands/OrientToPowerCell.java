package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.lib.command.RepeatCommand;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcadeWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import java.util.function.Supplier;

public class OrientToPowerCell extends SequentialCommandGroup {

    private Drivetrain drivetrain = Drivetrain.getInstance();

    private VisionService vision = VisionService.getInstance();

    private double setpoint = 0;

    public OrientToPowerCell(Supplier<Double> moveValue) {
        addCommands(
                new InstantCommand(this::updateSetpoint),
                new DriveArcadeWithPID(drivetrain,
                        Drivetrain.orientationPIDSettings,
                        Drivetrain.orientationFFSettings,
                        drivetrain::getYaw,
                        () -> setpoint,
                        moveValue).alongWith(new RepeatCommand(
                        new InstantCommand(this::updateSetpoint),
                        new WaitCommand(0.1)
                ))
        );
    }

    private void updateSetpoint() {
        setpoint = drivetrain.getYaw() - vision.getIntakeYaw();
    }
}
