package com.spikes2212.frc2020;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

    public static Compressor compressor;

    @Override
    public void robotInit() {
        compressor = new Compressor(RobotMap.CAN.PCM);
    }
}
