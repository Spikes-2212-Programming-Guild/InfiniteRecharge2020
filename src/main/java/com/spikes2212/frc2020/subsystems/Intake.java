package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    private static RootNamespace intakeNamespace = new RootNamespace("intake");
    private static final Supplier<Double> minSpeed = intakeNamespace
            .addConstantDouble("min speed", -1);
    private static final Supplier<Double> maxSpeed = intakeNamespace
            .addConstantDouble("max speed", 1);
    private static final Supplier<Double> gripSpeed = intakeNamespace
            .addConstantDouble("grip speed", 0.5);
    public static final Supplier<Double> intakeCurrent = intakeNamespace
            .addConstantDouble("current when gripped", 19);

    private DoubleSolenoid rightSolenoid;
    private WPI_TalonSRX motor;

    private boolean enabled;

    private static Intake instance;

    public static Intake getInstance() {
        if(instance == null) {
            DoubleSolenoid right = new DoubleSolenoid(RobotMap.PCM.INTAKE_FORWARD,
                    RobotMap.PCM.INTAKE_BACKWARD);
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.INTAKE_MOTOR);
            motor.setInverted(true);
            instance = new Intake(right, motor);
        }

        return instance;
    }

    private Intake(DoubleSolenoid right, WPI_TalonSRX motor) {
        super(minSpeed, maxSpeed);
        this.rightSolenoid = right;
        this.motor = motor;
        enabled = false;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return speed >= 0 && enabled;
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
        rightSolenoid.set(DoubleSolenoid.Value.kForward);
        setEnabled(true);
    }

    public void close() {
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
        intakeNamespace.putNumber("current current", motor::getStatorCurrent);
        intakeNamespace.putData("open", new InstantCommand(this::open, this));
        intakeNamespace.putData("close", new InstantCommand(this::close, this));
        intakeNamespace.putData("grip", new MoveGenericSubsystem(this, gripSpeed));
    }


    public double getSuppliedCurrent(){
        return motor.getSupplyCurrent();
    }

    public double getStatorCurrent(){
        return motor.getStatorCurrent();
    }

    public double getCurrentLimit() {
        return intakeCurrent.get();
    }

    public double getGripSpeed() {
        return gripSpeed.get();
    }
}
