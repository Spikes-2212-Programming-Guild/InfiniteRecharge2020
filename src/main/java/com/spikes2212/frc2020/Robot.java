package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Climbing;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
    public static Climbing climbing;

    @Override
    public void robotInit() {
        climbing = Climbing.getInstance();
    }
}
