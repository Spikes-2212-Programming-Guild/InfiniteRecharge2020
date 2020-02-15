package com.spikes2212.frc2020.commands.roller;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Roller;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

public class DriveToTrench extends CommandBase {

    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Roller roller = Roller.getInstance();

    private Supplier<Double> forwardSpeed;

    public DriveToTrench(Supplier<Double> forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.tankDrive(forwardSpeed.get(), forwardSpeed.get());
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return roller.canMove(1);
    }
}
