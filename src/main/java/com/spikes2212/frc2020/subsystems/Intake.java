package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.commands.OrientToPowerCell;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Intake extends GenericSubsystem {

    public static final RootNamespace intakeNamespace = new RootNamespace("intake");

    private static Supplier<Double> minSpeed = intakeNamespace.addConstantDouble("min speed", -1);
    private static Supplier<Double> maxSpeed = intakeNamespace.addConstantDouble("max speed", 1);
    public static Supplier<Double> intakeVoltage = intakeNamespace.addConstantDouble("intake voltage", 6);
    public static Supplier<Double> intakeCurrentLimit = intakeNamespace.addConstantDouble("intake current limit", 17);

    private static final Intake instance = new Intake();

    public static Intake getInstance() {
        return instance;
    }

    private WPI_TalonSRX motor;
    private DigitalInput limit;

    private boolean enabled;

    private Intake() {
        super(minSpeed, maxSpeed);
        motor = new WPI_TalonSRX(RobotMap.CAN.INTAKE_MOTOR);
        limit = new DigitalInput(RobotMap.DIO.INTAKE_LIMIT);
        enabled = true;
    }

    @Override
    public void apply(double speed) {
        motor.setVoltage(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return enabled;
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
        intakeNamespace.putNumber("intake supplied current", this::getSuppliedCurrent);
        intakeNamespace.putData("intake", new MoveGenericSubsystem(this, intakeVoltage));
        intakeNamespace.putData("intake power cell", new IntakePowerCell());
        intakeNamespace.putData("orient to ball", new OrientToPowerCell(Robot.oi::getRightY));
        intakeNamespace.putBoolean("limit", limit.get());
    }

    public boolean isPressed() {
        return limit.get();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getSuppliedCurrent() {
        return motor.getSupplyCurrent();
    }

    public double getStatorCurrent() {
        return motor.getStatorCurrent();
    }

}
