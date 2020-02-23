package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.statemachines.FeederStateMachine;
import com.spikes2212.frc2020.statemachines.IntakeFeederStateMachine;
import com.spikes2212.frc2020.statemachines.IntakeStateMachine;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private IntakeStateMachine intakeStateMachine = IntakeStateMachine.getInstance();
    private FeederStateMachine feederStateMachine = FeederStateMachine.getInstance();
    private IntakeFeederStateMachine intakeFeederStateMachine = IntakeFeederStateMachine.getInstance();
    public static OI oi;

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
    }


    @Override
    public void teleopPeriodic() {
//        super.teleopPeriodic();
        visionService.periodic();
    }

    @Override
    public void robotPeriodic() {
//        new Compressor().stop();
        CommandScheduler.getInstance().run();
    }
}
