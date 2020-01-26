package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.drivetrains.OdometryDrivetrain;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.lib.path.OdometryHandler;
import com.spikes2212.lib.util.PigeonWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

import java.util.function.Supplier;

public class Drivetrain extends OdometryDrivetrain {

    public static Namespace drivetrainNamespace = new RootNamespace("drivetrain");
    public static Supplier<Double> width = drivetrainNamespace.addConstantDouble("width", 0.7);
    public static Supplier<Double> wheelDiameter = drivetrainNamespace
            .addConstantDouble("wheel diameter (inches)", 6);

    private static Drivetrain instance;

    public static Drivetrain getInstance() {
        if(instance == null) {
            WPI_TalonSRX left = new WPI_TalonSRX(RobotMap.CAN.LEFT_TALON);
            WPI_TalonSRX right = new WPI_TalonSRX(RobotMap.CAN.RIGHT_TALON);
            new WPI_VictorSPX(RobotMap.CAN.LEFT_VICTOR).follow(left);
            new WPI_VictorSPX(RobotMap.CAN.RIGHT_VICTOR).follow(right);
            right.setInverted(true);
            Encoder leftEncoder = new Encoder(RobotMap.DIO.LEFT_ENCODER_POS, RobotMap.DIO.LEFT_ENCODER_NEG);
            Encoder rightEncoder = new Encoder(RobotMap.DIO.RIGHT_ENCODER_POS, RobotMap.DIO.RIGHT_ENCODER_NEG);
            leftEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
            rightEncoder.setDistancePerPulse(wheelDiameter.get() * 0.0254 * Math.PI / 360);
            PigeonWrapper imu = new PigeonWrapper(left);
            instance = new Drivetrain(left, right, leftEncoder, rightEncoder, imu);
        }
        return instance;
    }

    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private PigeonWrapper imu;
    private OdometryHandler odometry;

    private Drivetrain(SpeedController left, SpeedController right, Encoder leftEncoder, Encoder rightEncoder,
                       PigeonWrapper imu) {
        super(left, right);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        this.imu = imu;
        this.odometry = new OdometryHandler(leftEncoder::getDistance, rightEncoder::getDistance, imu::getY,
                0,0);
    }

    @Override
    public void periodic() {
        odometry.calculate();
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
        //TODO reset the imu
    }

    @Override
    public double getLeftRate() {
        return leftEncoder.getRate();
    }

    @Override
    public double getRightRate() {
        return rightEncoder.getRate();
    }
}
