package com.spikes2212.frc2020;

public class RobotMap {
    public interface CAN {
        int ELEVATOR_TALON = 0;
    }

    public interface DIO {
        int ELEVATOR_ENCODER_POS = 1;
        int ELEVATOR_ENCODER_NEG = 2;
        int ELEVATOR_BOTTOM_SWITCH = 3;
        int ELEVATOR_TOP_SWITCH = 4;

    }
}
