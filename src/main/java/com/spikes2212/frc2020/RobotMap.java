package com.spikes2212.frc2020;

public class RobotMap {
    public interface CAN {
        int LEFT_TALON = 2;
        int LEFT_VICTOR = 3;
        int RIGHT_TALON = 4;
        int RIGHT_VICTOR = 5;
        int PCM = 0;
    }

    public interface DIO {
        int LEFT_ENCODER_POS = 0;
        int LEFT_ENCODER_NEG = 1;
        int RIGHT_ENCODER_POS = 2;
        int RIGHT_ENCODER_NEG = 3;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int LEFT_INTAKE_FORWARD = 4;
        int LEFT_INTAKE_BACKWARD = 5;
        int RIGHT_INTAKE_FORWARD = 2;
        int RIGHT_INTAKE_BACKWARD = 3;
    }

    public interface PWM {
        int INTAKE_MOTOR = 0;
        int FEEDER_MOTOR = 0;
    }
}
