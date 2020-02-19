package com.spikes2212.frc2020.services;

import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.util.function.Supplier;

public class VisionService {

    private static final Namespace visionNamespace = new RootNamespace("vision");

    private Supplier<Double> turretHeight = visionNamespace.addConstantDouble("turret height", 78); //recheck the turret height
    private Supplier<Double> retroReflectiveHeight = visionNamespace.addConstantDouble("retro reflective height", 249 - turretHeight.get());
    private Supplier<String> cameraName = visionNamespace.addConstantString("camera name", "turret");

    public static VisionService getInstance() {
        if (visionService == null)
            visionService = new VisionService();
        return visionService;
    }

    private NetworkTable turretCam;

    private NetworkTableEntry yaw;
    private NetworkTableEntry pitch;
    private NetworkTableEntry height;

    public static VisionService visionService;

    public VisionService() {
        turretCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable(cameraName.get());
        yaw = turretCam.getEntry("yaw");
        pitch = turretCam.getEntry("pitch");
        height = turretCam.getEntry("height");
    }

    public double getYaw() {
        return yaw.getDouble(0);
    }

    public double getPitch() {
        return pitch.getDouble(0);
    }

    public double getHeight() {
        return height.getDouble(0);
    }


}
