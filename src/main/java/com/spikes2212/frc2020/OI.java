package com.spikes2212.frc2020;

import com.spikes2212.lib.util.XboXUID;
import edu.wpi.first.wpilibj.Joystick;

public class OI /* GEVALD */ {

    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);
    private XboXUID controller = new XboXUID(2);

    public double getLeftX() {
        return left.getX();
    }

    public double getLeftY() {
        return left.getY();
    }

    public double getRightX() {
        return right.getX();
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getControllerRightAngle() {
        return Math.atan2(controller.getRightY(),controller.getRightX());
    }
}
