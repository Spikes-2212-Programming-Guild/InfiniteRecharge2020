package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.frc2020.subsystems.Shooter;
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

        oi = new OI();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
