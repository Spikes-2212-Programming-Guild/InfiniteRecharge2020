package com.spikes2212.frc2020;

public class RobotMap {

    public interface CAN {

        int PCM = 0;

        int TURRET_TALON = 5;

        int SHOOTER_MASTER = 11;
        int SHOOTER_SLAVE = 12;

        int DRIVETRAIN_LEFT_TALON = 4;
        int DRIVETRAIN_LEFT_VICTOR = 2;
        int DRIVETRAIN_RIGHT_TALON = 3;
        int DRIVETRAIN_RIGHT_VICTOR = 1;

        int ELEVATOR_TALON = 8;
    }

    public interface DIO {
        int START_LIMIT = 0;
        int END_LIMIT = 1;

        int DRIVETRAIN_LEFT_ENCODER_POS = 6;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 7;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 8;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 9;

        int ELEVATOR_ENCODER_POS = 4;
        int ELEVATOR_ENCODER_NEG = 5;
        int ELEVATOR_BOTTOM_SWITCH = 6;
        int ELEVATOR_TOP_SWITCH = 7;
        int ELEVATOR_HALL_EFFECT=10;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int LEFT_INTAKE_FORWARD = 4;
        int LEFT_INTAKE_BACKWARD = 5;
        int RIGHT_INTAKE_FORWARD = 2;
        int RIGHT_INTAKE_BACKWARD = 3;

        int SHOOTER_FORWARD = 6;
        int SHOOTER_BACKWARD = 7;
    }

    public interface PWM {
        int INTAKE_MOTOR = 0;

        int FEEDER_MOTOR = 1;
    }
}
