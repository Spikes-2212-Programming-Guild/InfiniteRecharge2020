package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Robot extends TimedRobot {

    private static OI oi;

    @Override
    public void robotInit() {
        Shooter.getInstance().configureDashboard();
        Turret.getInstance().configureDashboard();
        Feeder.getInstance().configureDashboard();
        Intake.getInstance().configureDashboard();
        Climber.getInstance().configureDashboard();
        Elevator.getInstance().configureDashboard();

        oi = new OI();
        Drivetrain.getInstance().setDefaultCommand(new DriveArcade(Drivetrain.getInstance(),
                oi::getLeftY, oi::getRightX));
        SmartDashboard.putData("start compressor", new InstantCommand(new Compressor()::start));
        SmartDashboard.putData("stop compressor", new InstantCommand(new Compressor()::stop));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
