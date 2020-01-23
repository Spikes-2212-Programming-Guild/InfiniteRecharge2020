package com.spikes2212.frc2020;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.Supplier;

public class VisionService {

    private static final double targetHeight = 0;

    private static NetworkTable turretCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("front");

    public static final Supplier<Double> yawToRetroReflective = () -> turretCam.getEntry("yaw").getDouble(0.0);
    public static final Supplier<Double> pitchToRetroReflective = () -> turretCam.getEntry("pitch").getDouble(0.0);

    public static double getDistanceToRetroReflective() {
        return targetHeight / Math.tan(Math.abs(pitchToRetroReflective.get()));
    }
}
