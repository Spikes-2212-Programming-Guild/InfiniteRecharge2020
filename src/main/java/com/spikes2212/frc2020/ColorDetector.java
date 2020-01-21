package com.spikes2212.frc2020;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import javax.naming.Name;

public class ColorDetector extends ColorSensorV3 {

    private static Namespace colors = new RootNamespace("colors");

    public enum WheelColor {
        RED, BLUE, GREEN, YELLOW, OTHER
    }

    private ColorMatch matcher;

    public static final Color redTarget = null;
    public static final Color blueTarget = null;
    public static final Color greenTarget = null;
    public static final Color yellowTarget = null; //TODO calibrate colors

    public ColorDetector(I2C.Port port) {
        super(port);
        matcher = new ColorMatch();
    }

    public WheelColor getDetectedColor() {
        ColorMatchResult match = matcher.matchClosestColor(getColor());
        if(match.color.equals(redTarget)) return WheelColor.RED;
        if(match.color.equals(blueTarget)) return WheelColor.BLUE;
        if(match.color.equals(greenTarget)) return WheelColor.GREEN;
        if(match.color.equals(yellowTarget)) return WheelColor.YELLOW;
        return WheelColor.OTHER;
    }
}
