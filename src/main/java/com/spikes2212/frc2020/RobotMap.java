package com.spikes2212.frc2020;

public class RobotMap {
    public interface PWM {
        int INTAKE_MOTOR = 0;
        int FEEDER_MOTOR = 0;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int LEFT_INTAKE_FORWARD = 4;
        int LEFT_INTAKE_BACKWARD = 5;
        int RIGHT_INTAKE_FORWARD = 2;
        int RIGHT_INTAKE_BACKWARD = 3;
    }

    public interface CAN {
        int PCM = 0;
    }
}