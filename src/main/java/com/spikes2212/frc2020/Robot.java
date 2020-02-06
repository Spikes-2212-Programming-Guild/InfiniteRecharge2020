package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {

    private static OI oi;

    private static Shooter shooter = Shooter.getInstance();
    private static Turret turret = Turret.getInstance();
    private static Feeder feeder = Feeder.getInstance();
    private static Intake intake = Intake.getInstance();

    @Override
    public void robotInit() {
        shooter.configureDashboard();
        turret.configureDashboard();
        feeder.configureDashboard();
        intake.configureDashboard();

        oi = new OI();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
