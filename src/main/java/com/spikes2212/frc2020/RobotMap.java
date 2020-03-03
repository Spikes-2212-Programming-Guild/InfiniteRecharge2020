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
        int TURRET_START_LIMIT = 10;
        int TURRET_END_LIMIT = 8;

        int DRIVETRAIN_LEFT_ENCODER_POS = 0;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 2;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;

        int ELEVATOR_ENCODER_POS = 24;
        int ELEVATOR_ENCODER_NEG = 25;
        int ELEVATOR_BOTTOM_SWITCH = 11;
        int ELEVATOR_TOP_SWITCH = 9;
        int ELEVATOR_LIMIT = 12;

        int INTAKE_LIMIT = 4;
    }

    public interface PCM {
        int FEEDER_FORWARD = 0;
        int FEEDER_BACKWARD = 1;

        //       int INTAKE_FORWARD = 4;
        //       int INTAKE_BACKWARD = 5;
        //       int RIGHT_INTAKE_FORWARD = 2;
        //       int RIGHT_INTAKE_BACKWARD = 3;

        int SHOOTER_FORWARD = 3;
        int SHOOTER_BACKWARD = 2;
    }

    public interface PWM {

        int FEEDER_MOTOR = 1;
    }
}
