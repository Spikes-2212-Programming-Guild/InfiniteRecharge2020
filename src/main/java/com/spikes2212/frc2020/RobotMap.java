package com.spikes2212.frc2020;

public class RobotMap {

    public interface CAN {
        int TURRET_TALON = 6;

        int SHOOTER_MASTER = 8;
        int SHOOTER_SLAVE = 9;

        int INTAKE_MOTOR = 4;
        int FEEDER_MOTOR = 3;

        int DRIVETRAIN_LEFT_TALON = 17;
        int DRIVETRAIN_LEFT_VICTOR = 18;
        int DRIVETRAIN_RIGHT_TALON = 19;
        int DRIVETRAIN_RIGHT_VICTOR = 1;

        int PCM = 0;
        int CLIMBER_TALON_LEFT = 62;
        int CLIMBER_TALON_RIGHT = 61;
        int ELEVATOR_TALON = 60;
    }

    public interface DIO {
        int START_LIMIT = 4;
        int END_LIMIT = 5;

        int DRIVETRAIN_LEFT_ENCODER_POS = 0;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 2;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;
        int CLIMBER_ENCODER_LEFT_POS = 10;
        int CLIMBER_ENCODER_LEFT_NEG = 11;
        int CLIMBER_ENCODER_RIGHT_POS = 12;
        int CLIMBER_ENCODER_RIGHT_NEG = 13;
        int ELEVATOR_ENCODER_POS = 14;
        int ELEVATOR_ENCODER_NEG = 15;
        int ELEVATOR_BOTTOM_SWITCH = 16;
        int ELEVATOR_TOP_SWITCH = 17;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int SHOOTER_FORWARD = 4;
        int SHOOTER_BACKWARD = 5;
        int INTAKE_FORWARD = 2;
        int INTAKE_BACKWARD = 3;
    }
}
