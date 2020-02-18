package com.spikes2212.frc2020;

public class RobotMap {

    public interface CAN {

        int PCM = 0;

        int INTAKE_MOTOR = 4;
        int FEEDER_VICTOR = 7;

        int TURRET_TALON = 13;

        int SHOOTER_MASTER = 10;
        int SHOOTER_SLAVE = 12;

        int DRIVETRAIN_LEFT_TALON = 2;
        int DRIVETRAIN_LEFT_VICTOR = 9;
        int DRIVETRAIN_RIGHT_TALON = 8;
        int DRIVETRAIN_RIGHT_VICTOR = 5;

        int ELEVATOR_TALON = 8;

        int CLIMBER_TALON_LEFT = 6;
        int CLIMBER_TALON_RIGHT = 60;
    }

    public interface DIO {
        int TURRET_START_LIMIT = 4;
        int TURRET_END_LIMIT = 8;

        int DRIVETRAIN_LEFT_ENCODER_POS = 5;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 7;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 10;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 9;

        int ELEVATOR_ENCODER_POS = 4;
        int ELEVATOR_ENCODER_NEG = 5;
        int ELEVATOR_BOTTOM_SWITCH = 6;
        int ELEVATOR_TOP_SWITCH = 7;
        int ELEVATOR_HALL_EFFECT=10;

        int CLIMBER_ENCODER_LEFT_POS = 11;
        int CLIMBER_ENCODER_LEFT_NEG = 12;
        int CLIMBER_ENCODER_RIGHT_POS = 13;
        int CLIMBER_ENCODER_RIGHT_NEG = 14;

        int INTAKE_LIMIT = 15;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        int INTAKE_FORWARD = 4;
        int INTAKE_BACKWARD = 5;
        int RIGHT_INTAKE_FORWARD = 2;
        int RIGHT_INTAKE_BACKWARD = 3;

        int SHOOTER_FORWARD = 6;
        int SHOOTER_BACKWARD = 7;
    }

    public interface PWM {

        int FEEDER_MOTOR = 1;
    }
}
