package com.spikes2212.frc2020;

import com.spikes2212.frc2020.autonomous.CrossLineFromCenter;
import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.lib.command.RepeatCommand;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class Robot extends TimedRobot {

    private Climber climber = Climber.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private Shooter shooter = Shooter.getInstance();
    private Turret turret = Turret.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private Intake intake = Intake.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private VisionService visionService = VisionService.getInstance();

    //  private UsbCamera turretCam = new UsbCamera(1);
    public static OI oi;

    private Command auto;
    @Override
    public void robotInit() {
        oi = new OI();
        visionService.configureDashboard();
        shooter.configureDashboard();
        turret.configureDashboard();
        feeder.configureDashboard();
        intake.configureDashboard();
        drivetrain.configureDashboard();
        elevator.configureDashboard();
        climber.configureDashboard();

        drivetrain.setDefaultCommand(new DriveArcade(drivetrain,
                oi::getRightY, oi::getLeftX));
        SmartDashboard.putData("start compressor", new InstantCommand(new Compressor()::start));
        SmartDashboard.putData("stop compressor", new InstantCommand(new Compressor()::stop));
        SmartDashboard.putData("intake", new RepeatCommand(new IntakePowerCell()));
        Turret.turretNamespace.putNumber("joystick angle for turret", oi::getControllerRightAngle);
        auto = new CrossLineFromCenter();
        new UsbCamera("driver camera", 0);
    }

    @Override
    public void autonomousInit() {
        auto.schedule();
    }

    @Override
    public void teleopInit() {
        if (auto != null) auto.cancel();
    }

    @Override
    public void teleopPeriodic() {
        visionService.periodic();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
