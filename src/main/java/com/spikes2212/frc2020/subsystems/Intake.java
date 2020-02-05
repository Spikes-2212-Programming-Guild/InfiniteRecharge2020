package com.spikes2212.frc2020.subsystems;

import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    public static RootNamespace intakeNamespace = new RootNamespace("intake");

    public static final Supplier<Double> minSpeed = intakeNamespace.addConstantDouble("min speed", -1);
    public static final Supplier<Double> maxSpeed = intakeNamespace.addConstantDouble("max speed", 1);
    public static final Supplier<Double> gripSpeed = intakeNamespace.addConstantDouble("grip speed", 0.5);

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            DoubleSolenoid left = new DoubleSolenoid(RobotMap.PCM.LEFT_INTAKE_FORWARD
                    , RobotMap.PCM.LEFT_INTAKE_BACKWARD);
            DoubleSolenoid right = new DoubleSolenoid(RobotMap.PCM.RIGHT_INTAKE_FORWARD,
                    RobotMap.PCM.RIGHT_INTAKE_BACKWARD);
            VictorSP motor = new VictorSP(RobotMap.PWM.INTAKE_MOTOR);
            instance = new Intake(left, right, motor);
        }
        return instance;
    }

    private DoubleSolenoid leftSolenoid, rightSolenoid;
    private VictorSP motor;
    private boolean enabled;

    private Intake(DoubleSolenoid left, DoubleSolenoid right, VictorSP motor) {
        super(minSpeed, maxSpeed);
        this.leftSolenoid = left;
        this.rightSolenoid = right;
        this.motor = motor;
        enabled=false;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return speed >= 0 && enabled ;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void periodic() {
        intakeNamespace.update();
    }

    public void open() {
        leftSolenoid.set(DoubleSolenoid.Value.kForward);
        rightSolenoid.set(DoubleSolenoid.Value.kForward);
        setEnabled(true);
    }

    public void close() {
        leftSolenoid.set(DoubleSolenoid.Value.kReverse);
        rightSolenoid.set(DoubleSolenoid.Value.kReverse);
        setEnabled(false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void configureDashboard() {
        intakeNamespace.putData("open", new InstantCommand(this::open, this));
        intakeNamespace.putData("close", new InstantCommand(this::close, this));
        intakeNamespace.putData("grip", new MoveGenericSubsystem(this, gripSpeed));
    }
}
