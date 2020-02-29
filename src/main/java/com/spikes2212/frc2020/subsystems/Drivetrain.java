package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.commands.OrientToPowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.lib.command.drivetrains.OdometryDrivetrain;
import com.spikes2212.lib.command.drivetrains.commands.FollowPath;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.path.*;
import com.spikes2212.lib.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.Supplier;

public class Drivetrain extends OdometryDrivetrain {

    public static RootNamespace drivetrainNamespace = new RootNamespace("drivetrain");

    public static Namespace orientationNamespace = drivetrainNamespace.addChild("orientation");

    public static Supplier<Double> width = drivetrainNamespace
            .addConstantDouble("width", 0.7);
    public static Supplier<Double> wheelDiameter = drivetrainNamespace
            .addConstantDouble("wheel diameter (inches)", 6);

    public static Supplier<Double> orientationKP = orientationNamespace.addConstantDouble("kP", 0);
    public static Supplier<Double> orientationKI = orientationNamespace.addConstantDouble("kI", 0);
    public static Supplier<Double> orientationKD = orientationNamespace.addConstantDouble("kD", 0);

    public static Supplier<Double> orientationKS = orientationNamespace.addConstantDouble("kS", 0);

    public static Supplier<Double> orientationTolerance = orientationNamespace
            .addConstantDouble("tolerance", 0);
    public static Supplier<Double> orientationWaitTime = orientationNamespace
            .addConstantDouble("wait time", 0);

    public static PIDSettings orientationPIDSettings = new PIDSettings(orientationKP, orientationKI, orientationKD,
            orientationTolerance, orientationWaitTime);

    public static FeedForwardSettings orientationFFSettings = new FeedForwardSettings(orientationKS, () -> 0.0, () -> 0.0);


    public static Supplier<Double> autoForwardSpeed = drivetrainNamespace.addConstantDouble("auto forward speed", 0.5);
    public static Supplier<Double> autoForwardTimeout = drivetrainNamespace.addConstantDouble("auto forward timeout", 1.5);

    private static final Drivetrain instance = new Drivetrain();

    public static Drivetrain getInstance() {
        return instance;
    }

    private WPI_VictorSPX leftVictor, rightVictor;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private PigeonWrapper imu;
    private OdometryHandler odometry;

    private boolean inverted;

    private Drivetrain() {
        super(new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON), new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON));
        leftVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR);
        rightVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR);
        leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        leftEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
        rightEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
        leftVictor.follow((WPI_TalonSRX) leftController);
        rightVictor.follow((WPI_TalonSRX) rightController);
        ((WPI_TalonSRX) leftController).setNeutralMode(NeutralMode.Brake);
        ((WPI_TalonSRX) rightController).setNeutralMode(NeutralMode.Brake);
        rightVictor.setNeutralMode(NeutralMode.Brake);
        leftVictor.setNeutralMode(NeutralMode.Brake);
        imu = new PigeonWrapper((WPI_TalonSRX) leftController);
        imu.reset();
        odometry = new OdometryHandler(leftEncoder::getDistance, rightEncoder::getDistance,
                () -> -imu.getYaw(), 0, 0);
    }

    @Override
    public void periodic() {
        super.periodic();
        drivetrainNamespace.update();
    }

    @Override
    public OdometryHandler getHandler() {
        return odometry;
    }

    public double getYaw() {
        return odometry.getYaw();
    }

    @Override
    public double getWidth() {
        return width.get();
    }

    @Override
    public void zeroSensors() {
        leftEncoder.reset();
        rightEncoder.reset();
        imu.reset();
    }

//    @Override
//    public void arcadeDrive(double moveValue, double rotateValue) {
//        super.curvatureDrive(moveValue, rotateValue);
//    }

    @Override
    public double getLeftRate() {
        return leftEncoder.getRate();
    }

    @Override
    public double getRightRate() {
        return rightEncoder.getRate();
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
        leftController.setInverted(inverted);
        rightController.setInverted(!inverted);
        leftVictor.setInverted(inverted);
        rightVictor.setInverted(!inverted);
    }

    public void configureDashboard() {
        VisionService vision = VisionService.getInstance();
        Path path = new Path(0.075, 0.6, 0.6, 2, 0.75, 3,
                new Waypoint(0, 0), new Waypoint(0, 1), new Waypoint(0.75, 1.5), new Waypoint(0.75, 4));

        drivetrainNamespace.putNumber("imu yaw", imu::getYaw);
        drivetrainNamespace.putNumber("error", () -> vision.getIntakeYaw() - imu.getYaw());
        drivetrainNamespace.putNumber("x", this.getHandler()::getX);
        drivetrainNamespace.putNumber("y", this.getHandler()::getY);
        drivetrainNamespace.putData("orient to powercell", new OrientToPowerCell(() -> 0.0));
        SmartDashboard.putData("autonomous", new FollowPath(this, path, 0.4,
                new PIDSettings(0.02, 0, 0), new FeedForwardSettings(0.2, 0), 2));

    }
}
