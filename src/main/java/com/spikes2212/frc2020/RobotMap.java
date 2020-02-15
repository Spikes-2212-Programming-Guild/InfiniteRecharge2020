package com.spikes2212.frc2020;

public class RobotMap {

  public interface CAN {
    int TURRET_TALON = 6;

    int SHOOTER_MASTER = 8;
    int SHOOTER_SLAVE = 2;

    int INTAKE_MOTOR = 4;
    int FEEDER_MOTOR = 3;

    int DRIVETRAIN_LEFT_TALON = 17;
    int DRIVETRAIN_LEFT_VICTOR = 18;
    int DRIVETRAIN_RIGHT_TALON = 19;
    int DRIVETRAIN_RIGHT_VICTOR = 1;

    int PCM = 0;
  }

  public interface DIO {
    int START_LIMIT = 4;
    int END_LIMIT = 5;

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
}
