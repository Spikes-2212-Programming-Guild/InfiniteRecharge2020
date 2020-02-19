package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Robot extends TimedRobot {

    private Shooter shooter = Shooter.getInstance();
    private Turret turret = Turret.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private Intake intake = Intake.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();

    public static OI oi;

    @Override
    public void robotInit() {
        shooter.configureDashboard();
        turret.configureDashboard();
        feeder.configureDashboard();
        intake.configureDashboard();
        drivetrain.configureDashboard();

        oi = new OI();
        drivetrain.setDefaultCommand(new DriveArcade(drivetrain,
                oi::getRightY, oi::getLeftX));
        SmartDashboard.putData("start compressor", new InstantCommand(new Compressor()::start));
        SmartDashboard.putData("stop compressor", new InstantCommand(new Compressor()::stop));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
