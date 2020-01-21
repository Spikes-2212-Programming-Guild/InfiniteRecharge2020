package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Intake;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    @Override
    public void robotInit() {
        Intake.getInstance().initTestingDashboard();
    }
}
