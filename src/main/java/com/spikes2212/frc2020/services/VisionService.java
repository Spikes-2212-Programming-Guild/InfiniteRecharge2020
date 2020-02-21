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

    public static final VisionService instance = new VisionService();

    public static VisionService getInstance() {
        return instance;
    }
    private NetworkTable turretCam;
    private NetworkTable intakeCam;

    private NetworkTableEntry retroreflectiveYaw;
    private NetworkTableEntry intakeYaw;
    private NetworkTableEntry pitch;
    private NetworkTableEntry area;


    public VisionService() {
        turretCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("turret");
        intakeCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("intake-cam");
        retroreflectiveYaw = turretCam.getEntry("yaw");
        intakeYaw = intakeCam.getEntry("yaw");
        pitch = turretCam.getEntry("pitch");
        area = turretCam.getEntry("area");
    }

    public double getRetrorelfectiveYaw() {
        return retroreflectiveYaw.getDouble(0);
    }
    public double getIntakeYaw() { return intakeYaw.getDouble(0); }

    public double getPitch() {
        return pitch.getDouble(0);
    }

    public double getArea() { return area.getDouble(0); }

    public double getDistanceFromTarget() {
        return 245.62 * getArea() * getArea() - 64.093 * getArea() + 5.9978;
    }
}
