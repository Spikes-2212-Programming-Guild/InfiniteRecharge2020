package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
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

    public static final double distancePerPulse = 2 * Math.PI / 4096.0;

    private static RootNamespace shooterNamespace = new RootNamespace("shooter");
    private static Namespace PID = shooterNamespace.addChild("PID");
    private static Supplier<Double> maxSpeed = shooterNamespace.addConstantDouble("Max Speed", 0.6);
    private static Supplier<Double> minSpeed = shooterNamespace.addConstantDouble("Min Speed", 0);
    public static Supplier<Double> shootSpeed =
      shooterNamespace.addConstantDouble("Shooting Speed", 0.6);

    private static Supplier<Double> kP = PID.addConstantDouble("kP", 0);
    private static Supplier<Double> kI = PID.addConstantDouble("kI", 0);
    private static Supplier<Double> kD = PID.addConstantDouble("kD", 0);
    private static Supplier<Double> kS = PID.addConstantDouble("kS", 0);
    private static Supplier<Double> kF = PID.addConstantDouble("kF", 0);
    private static Supplier<Double> tolerance = PID.addConstantDouble("Tolerance", 0);
    public static Supplier<Double> waitTime = PID.addConstantDouble("Wait Time", 0);

    private static Supplier<Double> targetSpeed = PID.addConstantDouble("target speed", 0);

    private static PIDSettings velocityPIDSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);
    private static FeedForwardSettings velocityFFSettings = new FeedForwardSettings(kS, kF, () -> 0.0);

    private static Shooter instance;

    public static Shooter getInstance() {
        if(instance == null) {
            WPI_TalonSRX master = new WPI_TalonSRX(RobotMap.CAN.SHOOTER_MASTER);
            WPI_VictorSPX slave = new WPI_VictorSPX(RobotMap.CAN.SHOOTER_SLAVE);
            master.setNeutralMode(NeutralMode.Brake);
            slave.setNeutralMode(NeutralMode.Brake);
            slave.follow(master);
            master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
            master.setSelectedSensorPosition(0);
            master.setInverted(true);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.CAN.PCM, RobotMap.PCM.SHOOTER_FORWARD,
                    RobotMap.PCM.SHOOTER_BACKWARD);
            instance = new Shooter(master, slave, solenoid);
        }

        return instance;
    }

    private PIDSettings pidSettings = new PIDSettings(kP, kI, kD, tolerance, waitTime);

    private WPI_TalonSRX master;
    private WPI_VictorSPX slave;
    private DoubleSolenoid solenoid;

    private NoiseReducer noiseReducer;

    private boolean enabled;
    private double last = 0;
    private Shooter(WPI_TalonSRX master, WPI_VictorSPX slave, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.master = master;
        this.slave = slave;
        this.solenoid = solenoid;

        this.noiseReducer = new NoiseReducer(() -> master.getSelectedSensorVelocity() * distancePerPulse
        , new ExponentialFilter(0.1));
        enabled = true;
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


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void configureDashboard() {
        VisionService vision = VisionService.getInstance();
        shooterNamespace.putNumber("shooter velocity - filtered", noiseReducer);
        shooterNamespace.putNumber("shooter velocity", () -> (double) master.getSelectedSensorVelocity() * distancePerPulse);
        shooterNamespace.putNumber("shooter position", master::getSelectedSensorPosition);
        shooterNamespace.putNumber("distance to target", vision::getDistanceFromTarget);
        shooterNamespace.putData("open", new InstantCommand(this::open));
        shooterNamespace.putData("close", new InstantCommand(this::close));
        shooterNamespace.putData("shoot", new MoveGenericSubsystem(this, shootSpeed));
        shooterNamespace.putData("pid shoot",
                new MoveGenericSubsystemWithPID(this, velocityPIDSettings, targetSpeed, noiseReducer, velocityFFSettings));
    }
}
