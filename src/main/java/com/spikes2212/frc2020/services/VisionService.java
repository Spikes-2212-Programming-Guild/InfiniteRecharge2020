package com.spikes2212.frc2020.services;

import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.Supplier;

public class VisionService {

    private static final Namespace visionServiceNamespace = new RootNamespace("vision");

    private Supplier<Double> turretHeight = visionServiceNamespace.addConstantDouble("turret height", 78); //recheck the turret height
    private Supplier<Double> retroReflectiveHeight = visionServiceNamespace.addConstantDouble("retro reflective height", 249 - turretHeight.get());
    private Supplier<String> cameraName = visionServiceNamespace.addConstantString("camera name", "turret");

    public static VisionService getInstance() {
        if (visionService == null)
            visionService = new VisionService();
        return visionService;
    }

    private NetworkTable turretCam;

    private Supplier<Double> yawToRetroReflective;
    private Supplier<Double> pitchToRetroReflective;

    public static VisionService visionService;

    public VisionService() {
        turretCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable(cameraName.get());
        yawToRetroReflective = () -> turretCam.getEntry("yaw").getDouble(0.0);
        pitchToRetroReflective = () -> turretCam.getEntry("pitch").getDouble(0.0);
    }

    public double getYawToRetroReflective() {
        return yawToRetroReflective.get();
    }

    public double getPitchToRetroReflective() {
        return pitchToRetroReflective.get();
    }

    public double getDistanceToRetroReflective() {
        return retroReflectiveHeight.get() / Math.tan(Math.abs(pitchToRetroReflective.get()));
    }


}
