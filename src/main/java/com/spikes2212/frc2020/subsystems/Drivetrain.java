package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.drivetrains.OdometryDrivetrain;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.path.OdometryHandler;
import com.spikes2212.lib.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

import java.util.function.Supplier;

public class Drivetrain extends OdometryDrivetrain {

    public static RootNamespace drivetrainNamespace = new RootNamespace("drivetrain");

    public static Supplier<Double> width = drivetrainNamespace.addConstantDouble("width", 0.7);
    public static Supplier<Double> wheelDiameter = drivetrainNamespace
            .addConstantDouble("wheel diameter (inches)", 6);

    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if(instance == null) {
            WPI_TalonSRX leftTalon = new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_LEFT_TALON);
            WPI_TalonSRX rightTalon = new WPI_TalonSRX(RobotMap.CAN.DRIVETRAIN_RIGHT_TALON);
            WPI_VictorSPX leftVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_LEFT_VICTOR);
            WPI_VictorSPX rightVictor = new WPI_VictorSPX(RobotMap.CAN.DRIVETRAIN_RIGHT_VICTOR);
            leftVictor.follow(leftTalon);
            rightVictor.follow(rightTalon);
            rightTalon.setInverted(true);
            rightVictor.setInverted(true);
            Encoder leftEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_LEFT_ENCODER_NEG);
            Encoder rightEncoder = new Encoder(RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_POS, RobotMap.DIO.DRIVETRAIN_RIGHT_ENCODER_NEG);
            leftEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
            rightEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
            PigeonWrapper imu = new PigeonWrapper(leftTalon);
            instance = new Drivetrain(leftTalon, rightTalon, leftVictor, rightVictor, leftEncoder, rightEncoder, imu);
        }

        return instance;
    }

    private WPI_VictorSPX leftVictor, rightVictor;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private PigeonWrapper imu;
    private OdometryHandler odometry;
    private boolean inverted;

    private Drivetrain(WPI_TalonSRX left, WPI_TalonSRX right, WPI_VictorSPX leftVictor, WPI_VictorSPX rightVictor,
                       Encoder leftEncoder, Encoder rightEncoder, PigeonWrapper imu) {
        super(left, right);
        this.leftVictor = leftVictor;
        this.rightVictor = rightVictor;
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.imu = imu;
        this.odometry = new OdometryHandler(leftEncoder::getDistance, rightEncoder::getDistance, imu::getY,
                0, 0);
        this.inverted = false;
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
}
