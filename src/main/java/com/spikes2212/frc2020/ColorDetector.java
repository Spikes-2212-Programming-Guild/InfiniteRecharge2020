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
    public static Supplier<Double> confidence = colors.addConstantDouble("sensor confidence", 0.95);

    public static Color redTarget;
    public static Color blueTarget;
    public static Color greenTarget;
    public static Color yellowTarget;

    public ColorDetector(I2C.Port port) {
        super(port);
        calibrateColors();
    }

    public Color getDetectedColor() {
        ColorMatchResult match = matcher.matchClosestColor(getColor());
        if(match.color.equals(redTarget)) return Color.kRed;
        if(match.color.equals(blueTarget)) return Color.kSeaGreen;
        if(match.color.equals(greenTarget)) return Color.kGreen;
        if(match.color.equals(yellowTarget)) return Color.kDarkOliveGreen;
        return Color.kWheat;
    }

    public void calibrateColors() {
        matcher = new ColorMatch();
        matcher.setConfidenceThreshold(confidence.get());
        redTarget = Color.kRed;
        blueTarget = Color.kSeaGreen;
        greenTarget = Color.kGreen;
        yellowTarget = Color.kDarkOliveGreen;
        matcher.addColorMatch(redTarget);
        matcher.addColorMatch(blueTarget);
        matcher.addColorMatch(greenTarget);
        matcher.addColorMatch(yellowTarget);
    }
}
