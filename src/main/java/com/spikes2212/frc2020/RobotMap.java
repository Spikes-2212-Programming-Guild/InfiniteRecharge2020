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
        int TURRET_START_LIMIT = 5;
        int TURRET_END_LIMIT = 6;

        int DRIVETRAIN_LEFT_ENCODER_POS = 0;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 2;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;

        int ELEVATOR_ENCODER_POS = 24;
        int ELEVATOR_ENCODER_NEG = 25;
        int ELEVATOR_BOTTOM_SWITCH = 26;
        int ELEVATOR_TOP_SWITCH = 27;
        int ELEVATOR_HALL_EFFECT=210;

        int CLIMBER_ENCODER_LEFT_POS = 211;
        int CLIMBER_ENCODER_LEFT_NEG = 212;
        int CLIMBER_ENCODER_RIGHT_POS = 213;
        int CLIMBER_ENCODER_RIGHT_NEG = 214;
    }

    public interface PCM {
        int FEEDER_FORWARD = 1;
        int FEEDER_BACKWARD = 0;

 //       int INTAKE_FORWARD = 4;
 //       int INTAKE_BACKWARD = 5;
 //       int RIGHT_INTAKE_FORWARD = 2;
 //       int RIGHT_INTAKE_BACKWARD = 3;

        int SHOOTER_FORWARD = 2;
        int SHOOTER_BACKWARD = 3;
    }

    public interface PWM {

        int FEEDER_MOTOR = 1;
    }
}
