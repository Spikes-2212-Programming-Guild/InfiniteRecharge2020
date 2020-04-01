package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.lib.control.FeedForwardSettings;
import com.spikes2212.lib.control.PIDSettings;
import com.spikes2212.lib.control.noise.ExponentialFilter;
import com.spikes2212.lib.control.noise.NoiseReducer;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Shooter extends GenericSubsystem {

    public static final double distancePerPulse = 10 / 4096.0;


    private static RootNamespace shooterNamespace = new RootNamespace("shooter");
    private static Namespace PID = shooterNamespace.addChild("PID");
    private static Supplier<Double> maxSpeed = shooterNamespace.addConstantDouble("Max Speed", 0.6);
    private static Supplier<Double> minSpeed = shooterNamespace.addConstantDouble("Min Speed", 0);
    public static Supplier<Double> shootSpeed =
            shooterNamespace.addConstantDouble("speed", 0.4);

    public static Supplier<Double> closeShootingSpeed = shooterNamespace.addConstantDouble("close shooting speed", 3.7);

    private static Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private static Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private static Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    private static Supplier<Double> kS = PID.addConstantDouble("kS", 0);
    private static Supplier<Double> kV = PID.addConstantDouble("kV", 0);
    private static Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    private static Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);

    private static Supplier<Double> targetSpeed = PID.addConstantDouble("target speed", 0);

    public static PIDSettings velocityPIDSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
    public static FeedForwardSettings velocityFFSettings = new FeedForwardSettings(kS, kV, () -> 0.0);

    private static final Shooter instance = new Shooter();

    public static Shooter getInstance() {
        return instance;
    }

    private WPI_TalonSRX master;
    private WPI_VictorSPX slave;
    private DoubleSolenoid solenoid;

    private NoiseReducer noiseReducer;

    private Shooter() {
        super(minSpeed, maxSpeed);
        master = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_MASTER);
        slave = new WPI_VictorSPX(RobotMap.CAN.SHOOTER_SLAVE);
        solenoid = new DoubleSolenoid(RobotMap.CAN.PCM, RobotMap.PCM.SHOOTER_FORWARD,
                RobotMap.PCM.SHOOTER_BACKWARD);
        master.setNeutralMode(NeutralMode.Brake);
        master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        master.setInverted(true);
        slave.setNeutralMode(NeutralMode.Brake);
        slave.follow(master);
        noiseReducer = new NoiseReducer(() -> master.getSelectedSensorVelocity() * distancePerPulse,
                new ExponentialFilter(0.05));
    }

    @Override
    public void apply(double speed) {
        master.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        master.stopMotor();
    }

    @Override
    public void periodic() {
        shooterNamespace.update();
    }

    public void open() {
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void close() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public double getMotorSpeed() {
        return noiseReducer.get();
    }

    @Override
    public void configureDashboard() {
        VisionService vision = VisionService.getInstance();
        PhysicsService physics = PhysicsService.getInstance();
        shooterNamespace.putNumber("shooter velocity - filtered", noiseReducer);
        shooterNamespace.putNumber("shooter velocity", () -> (double) master.getSelectedSensorVelocity() * distancePerPulse);
        shooterNamespace.putData("open", new InstantCommand(this::open));
        shooterNamespace.putData("close", new InstantCommand(this::close));
        shooterNamespace.putData("shoot", new MoveGenericSubsystem(this, shootSpeed));

        shooterNamespace.putData("pid shoot",
                new MoveGenericSubsystemWithPID(this, targetSpeed,
                        this::getMotorSpeed,
                        velocityPIDSettings, velocityFFSettings));

        shooterNamespace.putData("shoot for calculated distance",
                new MoveGenericSubsystemWithPID(this,
                        () -> physics.calculateSpeedForDistance(vision.getDistanceFromTarget()),
                        () -> master.getSelectedSensorVelocity() * distancePerPulse,
                        velocityPIDSettings, velocityFFSettings));
    }

    public void setAccelerated(boolean isAccelerated) {
        shooterNamespace.putBoolean("is accelerated", isAccelerated);
    }
}
