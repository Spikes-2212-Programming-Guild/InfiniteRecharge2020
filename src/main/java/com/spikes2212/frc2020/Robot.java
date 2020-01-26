package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.frc2020.subsystems.Shooter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        Shooter.getInstance().configureDashboard();
        Turret.getInstance().configureDashboard();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
