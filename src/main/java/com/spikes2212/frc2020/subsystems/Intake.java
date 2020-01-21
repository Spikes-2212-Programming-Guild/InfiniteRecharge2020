package com.spikes2212.frc2020.subsystems;

import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    public static Namespace intakeNamespace = new RootNamespace("intake");

    public static Supplier<Double> minSpeed;
    public static Supplier<Double> maxSpeed;
    public static Supplier<Double> gripSpeed;

    public enum IntakeState {
        UP, DOWN;
    }

    private DoubleSolenoid leftSolenoid, rightSolenoid;
    private VictorSP motor;
    private IntakeState state;

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

    private Intake(DoubleSolenoid left, DoubleSolenoid right, VictorSP motor) {
        super(minSpeed, maxSpeed);
        this.leftSolenoid = left;
        this.rightSolenoid = right;
        this.motor = motor;
        this.state = IntakeState.UP;
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

    public void initTestingDashboard() {
        minSpeed = intakeNamespace.addConstantDouble("min speed", -1);
        maxSpeed = intakeNamespace.addConstantDouble("max speed", 1);
        gripSpeed = intakeNamespace.addConstantDouble("grip speed", 0.5);
        intakeNamespace.putData("open", new InstantCommand(() -> {
            state = IntakeState.DOWN;
            leftSolenoid.set(DoubleSolenoid.Value.kForward);
            rightSolenoid.set(DoubleSolenoid.Value.kForward);
        }, this));
        intakeNamespace.putData("close", new InstantCommand(() -> {
            state = IntakeState.UP;
            leftSolenoid.set(DoubleSolenoid.Value.kReverse);
            rightSolenoid.set(DoubleSolenoid.Value.kReverse);
        }, this));
        intakeNamespace.putData("grip", new MoveGenericSubsystem(this, gripSpeed));
        intakeNamespace.putString("state", state::name);
    }
}
