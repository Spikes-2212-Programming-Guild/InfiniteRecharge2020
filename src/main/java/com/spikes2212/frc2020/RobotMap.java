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

        int ELEVATOR_TALON = 17;

        int CLIMBER_TALON = 1;
    }

    public interface DIO {
        int TURRET_START_LIMIT = 4;
        int TURRET_END_LIMIT = 5;

        int DRIVETRAIN_LEFT_ENCODER_POS = 0;
        int DRIVETRAIN_LEFT_ENCODER_NEG = 1;
        int DRIVETRAIN_RIGHT_ENCODER_POS = 2;
        int DRIVETRAIN_RIGHT_ENCODER_NEG = 3;

        int ELEVATOR_ENCODER_POS = 24;
        int ELEVATOR_ENCODER_NEG = 25;
        int ELEVATOR_BOTTOM_SWITCH = 10;
        int ELEVATOR_TOP_SWITCH = 11;
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
