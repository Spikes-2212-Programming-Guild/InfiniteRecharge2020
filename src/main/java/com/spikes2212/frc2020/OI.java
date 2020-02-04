package com.spikes2212.frc2020;

import edu.wpi.first.wpilibj.Joystick;

public class OI /* GEVALD */{
    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);

    public double getLeftX() {
        return left.getX() * Math.abs(left.getX());
    }
    public double getLeftY() {
        return -left.getY() * Math.abs(left.getY());
    }
    public double getRightX() {
        return right.getX() * Math.abs(right.getX());
    }
    public double getRightY() {
        return -right.getY() * Math.abs(right.getY());
    }
}
