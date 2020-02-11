package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Robot extends TimedRobot {

    public static OI oi;

    public static Drivetrain drivetrain = Drivetrain.getInstance();
    public static Turret turret = Turret.getInstance();
    @Override
    public void robotInit() {

        oi = new OI();
//        Shooter.getInstance().configureDashboard();
        drivetrain.setJoysticksDefaultCommand();
        turret.configureDashboard();
        drivetrain.configureDashboard();
//        Feeder.getInstance().configureDashboard();
//        Intake.getInstance().configureDashboard();
        SmartDashboard.putData("stop compressor", new InstantCommand(new Compressor()::stop));
        SmartDashboard.putData("reset IMU", new InstantCommand(Drivetrain.getInstance()::resetIMU));
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
