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

    private ColorMatch matcher;

    public static final Color blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    public static final Color greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    public static final Color redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    public static final Color yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    public ColorDetector(I2C.Port port) {
        super(port);
        matcher = new ColorMatch();
        matcher.addColorMatch(redTarget);
        matcher.addColorMatch(blueTarget);
        matcher.addColorMatch(greenTarget);
        matcher.addColorMatch(yellowTarget);
    }

    public Color getDetectedColor() {
        ColorMatchResult match = matcher.matchClosestColor(getColor());
        if(match.color.equals(redTarget)) return Color.kRed;
        if(match.color.equals(blueTarget)) return Color.kSeaGreen;
        if(match.color.equals(greenTarget)) return Color.kGreen;
        if(match.color.equals(yellowTarget)) return Color.kDarkOliveGreen;
        return Color.kWheat;
    }
}
