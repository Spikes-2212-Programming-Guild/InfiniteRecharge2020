package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Turret;
import edu.wpi.first.wpilibj.IterativeRobotBase;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    @Override
    public void robotInit() {
        Turret.getInstance().initDashboardTesting();
    }
}
