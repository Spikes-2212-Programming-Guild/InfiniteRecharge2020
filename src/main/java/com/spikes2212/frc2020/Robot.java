package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Climber;
import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Elevator;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

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
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
}
