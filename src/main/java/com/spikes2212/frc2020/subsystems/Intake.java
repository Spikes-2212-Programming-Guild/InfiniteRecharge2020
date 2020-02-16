package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    public static RootNamespace intakeNamespace = new RootNamespace("intake");

    private static Supplier<Double> minSpeed = intakeNamespace.addConstantDouble("min speed", -1);
    private static Supplier<Double> maxSpeed = intakeNamespace.addConstantDouble("max speed", 1);
    public static Supplier<Double> intakeVoltage = intakeNamespace.addConstantDouble("grip speed", 0.5);
    public static Supplier<Double> intakeCurrentLimit = intakeNamespace.addConstantDouble("intake Current", 0);

    private static Intake instance;

    public static Intake getInstance() {
        if (instance == null) {
            DigitalInput limit = new DigitalInput(RobotMap.DIO.INTAKE_LIMIT);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.PCM.INTAKE_FORWARD,
                    RobotMap.PCM.INTAKE_BACKWARD);
            WPI_TalonSRX motor = new WPI_TalonSRX(RobotMap.CAN.INTAKE_MOTOR);
            instance = new Intake(motor, limit, solenoid);
        }

        return instance;
    }

    private WPI_TalonSRX motor;
    private DigitalInput limit;
    private DoubleSolenoid solenoid;

    private boolean enabled;

    private Intake(WPI_TalonSRX motor, DigitalInput limit, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.limit = limit;
        this.solenoid = solenoid;
        enabled=false;
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

    @Override
    public void configureDashboard() {
        intakeNamespace.putData("grip", new MoveGenericSubsystem(this, intakeVoltage));
        intakeNamespace.putData("open", new InstantCommand(this::open, this));
        intakeNamespace.putData("close", new InstantCommand(this::close, this));
    }

    public void open() {
        solenoid.set(DoubleSolenoid.Value.kForward);
        setEnabled(true);
    }

    public void close() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
        setEnabled(false);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean limitPressed() {
        return limit.get();
    }

    public double getSuppliedCurrent(){
        return motor.getSupplyCurrent();
    }

    public double getStatorCurrent(){
        return motor.getStatorCurrent();
    }
}
