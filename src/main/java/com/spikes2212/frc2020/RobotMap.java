package com.spikes2212.frc2020;

public class RobotMap {

    public interface CAN {
        int TURRET_TALON = 5;
        int SHOOTER_MASTER = 7;
        int SHOOTER_SLAVE = 6;

        int DRIVETRAIN_LEFT_TALON = 4;
        int DRIVETRAIN_LEFT_VICTOR = 2;
        int DRIVETRAIN_RIGHT_TALON = 3;
        int DRIVETRAIN_RIGHT_VICTOR = 1;

        int PCM = 0;
    }

    public interface DIO {
        int START_LIMIT = 5;
        int END_LIMIT = 4;

        int DRIVETRAIN_LEFT_ENCODER_POS = 0;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 2;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;
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
        int FEEDER_MOTOR = 1;
    }
}
