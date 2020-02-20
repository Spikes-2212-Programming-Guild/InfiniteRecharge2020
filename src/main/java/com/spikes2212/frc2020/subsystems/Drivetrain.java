package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.drivetrains.OdometryDrivetrain;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.path.OdometryHandler;
import com.spikes2212.lib.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;

import java.util.function.Supplier;

public class Drivetrain extends OdometryDrivetrain {

    public static RootNamespace drivetrainNamespace = new RootNamespace("drivetrain");

    public static Supplier<Double> width = drivetrainNamespace
            .addConstantDouble("width", 0.7);
    public static Supplier<Double> wheelDiameter = drivetrainNamespace
            .addConstantDouble("wheel diameter (inches)", 6);

    private static Drivetrain instance = new Drivetrain();

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
        this.leftVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR);
        this.rightVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR);
        this.leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
        this.rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
        this.imu = imu;
        this.odometry = new OdometryHandler(leftEncoder::getDistance, rightEncoder::getDistance,
                new PigeonWrapper(new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON))::getYaw, 0, 0);
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

        drivetrainNamespace.putNumber("imu yaw", imu::getYaw);
    }
}
