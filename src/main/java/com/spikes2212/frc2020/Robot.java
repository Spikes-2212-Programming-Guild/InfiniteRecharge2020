package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Roller;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        Feeder.getInstance().configureDashboard();
        Roller.getInstance().configureDashboard();
    }
}
