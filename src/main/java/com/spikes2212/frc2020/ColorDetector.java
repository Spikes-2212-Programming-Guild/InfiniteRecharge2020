package com.spikes2212.frc2020;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import java.util.function.Supplier;

public class ColorDetector extends ColorSensorV3 {

    private static Namespace colors = new RootNamespace("colors");

    public enum WheelColor {
        RED, BLUE, GREEN, YELLOW, OTHER
    }

    private ColorMatch matcher;

    public static Supplier<Double> redRValue = colors.addConstantDouble("red r value", 1);
    public static Supplier<Double> redGValue = colors.addConstantDouble("red g value", 0);
    public static Supplier<Double> redBValue = colors.addConstantDouble("red b value", 0);
    public static Supplier<Double> blueRValue = colors.addConstantDouble("blue r value", 0);
    public static Supplier<Double> blueGValue = colors.addConstantDouble("blue g value", 0);
    public static Supplier<Double> blueBValue = colors.addConstantDouble("blue b value", 1);
    public static Supplier<Double> greenRValue = colors.addConstantDouble("green r value", 0);
    public static Supplier<Double> greenGValue = colors.addConstantDouble("green g value", 1);
    public static Supplier<Double> greenBValue = colors.addConstantDouble("green b value", 0);
    public static Supplier<Double> yellowRValue = colors.addConstantDouble("yellow r value", 0);
    public static Supplier<Double> yellowGValue = colors.addConstantDouble("yellow g value", 1);
    public static Supplier<Double> yellowBValue = colors.addConstantDouble("yellow b value", 1);

    public static Color redTarget;
    public static Color blueTarget;
    public static Color greenTarget;
    public static Color yellowTarget;

    public ColorDetector(I2C.Port port) {
        super(port);
        calibrateColors();
    }

    public WheelColor getDetectedColor() {
        ColorMatchResult match = matcher.matchClosestColor(getColor());
        if(match.color.equals(redTarget)) return WheelColor.RED;
        if(match.color.equals(blueTarget)) return WheelColor.BLUE;
        if(match.color.equals(greenTarget)) return WheelColor.GREEN;
        if(match.color.equals(yellowTarget)) return WheelColor.YELLOW;
        return WheelColor.OTHER;
    }

    public void calibrateColors() {
        matcher = new ColorMatch();
        redTarget = ColorMatch.makeColor(redRValue.get(), redGValue.get(), redBValue.get());
        blueTarget = ColorMatch.makeColor(blueRValue.get(), blueGValue.get(), blueBValue.get());
        greenTarget = ColorMatch.makeColor(greenRValue.get(), greenGValue.get(), greenBValue.get());
        yellowTarget = ColorMatch.makeColor(yellowRValue.get(), yellowGValue.get(), yellowBValue.get());
        matcher.addColorMatch(redTarget);
        matcher.addColorMatch(blueTarget);
        matcher.addColorMatch(greenTarget);
        matcher.addColorMatch(yellowTarget);
    }
}
