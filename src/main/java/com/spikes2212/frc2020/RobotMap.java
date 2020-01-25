package com.spikes2212.frc2020;

public class RobotMap {
    public interface PWM {
        int FEEDER_MOTOR = 0;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;
    }

    public interface CAN {
        int PCM = 0;
    }
}