package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    public static Namespace intakeNamespace = new RootNamespace("intake");

    public static final Supplier<Double> minSpeed = intakeNamespace
            .addConstantDouble("min speed", -1);
    public static final Supplier<Double> maxSpeed = intakeNamespace
            .addConstantDouble("max speed", 1);
    public static final Supplier<Double> gripSpeed = intakeNamespace
            .addConstantDouble("grip speed", 0.5);

    public enum IntakeState {
        UP, DOWN
    }

    private DoubleSolenoid rightSolenoid;
    private WPI_TalonSRX motor;
    private IntakeState state;

    private static Intake instance;

    public static Intake getInstance() {
        if(instance == null) {
            DoubleSolenoid right = new DoubleSolenoid(RobotMap.PCM.INTAKE_FORWARD,
                    RobotMap.PCM.INTAKE_BACKWARD);
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.INTAKE_MOTOR);
            instance = new Intake(right, motor);
        }

        return instance;
    }

    private Intake(DoubleSolenoid right, WPI_TalonSRX motor) {
        super(minSpeed, maxSpeed);
        this.rightSolenoid = right;
        this.motor = motor;
        this.state = IntakeState.UP;
    }

    public IntakeState getState() {
        return state;
    }

    public void setState(IntakeState state) {
        this.state = state;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return speed >= 0 && state == IntakeState.DOWN;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public void open() {
        setState(IntakeState.DOWN);
        rightSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void close() {
        setState(IntakeState.UP);
        rightSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    @Override
    public void configureDashboard() {
        intakeNamespace.putData("open", new InstantCommand(this::open, this));
        intakeNamespace.putData("close", new InstantCommand(this::close, this));
        intakeNamespace.putData("grip", new MoveGenericSubsystem(this, gripSpeed));
        intakeNamespace.putString("state", state::name);
    }
}
