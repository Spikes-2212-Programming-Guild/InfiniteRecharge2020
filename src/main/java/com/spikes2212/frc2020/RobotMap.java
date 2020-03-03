package com.spikes2212.frc2020;

public class RobotMap {

    public interface CAN {

        int PCM = 0;

        int DRIVETRAIN_LEFT_TALON = 1;
        int DRIVETRAIN_LEFT_VICTOR = 2;
        int DRIVETRAIN_RIGHT_TALON = 3;
        int DRIVETRAIN_RIGHT_VICTOR = 4;

        int SHOOTER_MASTER = 5;
        int SHOOTER_SLAVE = 6;

        int TURRET_TALON = 7;

        int INTAKE_MOTOR = 9;

        int FEEDER_VICTOR = 10;

        int ELEVATOR_TALON = 11;

        int CLIMBER_TALON = 12;
    }

    public interface DIO {

        int ELEVATOR_BOTTOM_SWITCH = 2;

        int DRIVETRAIN_RIGHT_ENCODER_POS = 4;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;

        int DRIVETRAIN_LEFT_ENCODER_POS = 5;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 6;

        int TURRET_START_LIMIT = 8;
        int TURRET_END_LIMIT = 9;

        int ELEVATOR_TOP_SWITCH = 10;
        int ELEVATOR_LIMIT = 11;

    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int SHOOTER_FORWARD = 3;
        int SHOOTER_BACKWARD = 2;
    }
}
