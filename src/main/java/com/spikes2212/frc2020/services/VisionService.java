package com.spikes2212.frc2020.services;

import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.networktables.*;

import java.util.function.Supplier;

public class VisionService {

    private static final RootNamespace visionNamespace = new RootNamespace("vision");

    private static final VisionService instance = new VisionService();

    public static VisionService getInstance() {
        return instance;
    }

    private NetworkTable turretCam;
    private NetworkTable intakeCam;

    private NetworkTableEntry retroreflectiveYaw;
    private NetworkTableEntry intakeYaw;
    private NetworkTableEntry pitch;
    private NetworkTableEntry area;

    public void periodic() {
        visionNamespace.update();
    }

    private VisionService() {
        turretCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("turret");
        intakeCam = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("intake");
        retroreflectiveYaw = turretCam.getEntry("targetYaw");
        intakeYaw = intakeCam.getEntry("targetYaw");
        pitch = turretCam.getEntry("pitch");
        area = turretCam.getEntry("targetArea");
    }

    public double getRetroReflectiveYaw() {
        return retroreflectiveYaw.getDouble(0);
    }

    public double getIntakeYaw() {
        return intakeYaw.getDouble(10);
    }

    public double getPitch() {
        return pitch.getDouble(0);
    }

    public double getArea() {
        return area.getDouble(0);
    }

    public void configureDashboard() {
        visionNamespace.putNumber("angle to balls", this::getIntakeYaw);
        visionNamespace.putNumber("angle to retroReflective", this::getRetroReflectiveYaw);
        visionNamespace.putNumber("distance from target", this::getDistanceFromTarget);
    }

    public double getDistanceFromTarget() {
        return 302.93 * getArea() * getArea() - 82.022 * getArea() + 6.592;
    }
}
