package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Supplier;

public class Elevator extends GenericSubsystem {

    private static final RootNamespace elevatorNamspace = new RootNamespace("elevator");
    public final Supplier<Double> upSpeed = elevatorNamspace
            .addConstantDouble("up speed", 0.4);
    public final Supplier<Double> downSpeed = elevatorNamspace
            .addConstantDouble("down speed", -0.5);

    private static Elevator instance = new Elevator();

    public static Elevator getInstance() {
        return instance;
    }

    private WPI_TalonSRX motor;
    private DigitalInput bottomHallEffect;
    private DigitalInput topHallEffect;
    private DigitalInput limit;

    private Elevator() {
        limit = new DigitalInput(RobotMap.DIO.ELEVATOR_LIMIT);
        motor = new WPI_TalonSRX(RobotMap.CAN.ELEVATOR_TALON);
        bottomHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_BOTTOM_SWITCH);
        topHallEffect = new DigitalInput(RobotMap.DIO.ELEVATOR_TOP_SWITCH);
    }

    @Override
    public void periodic() {
        elevatorNamspace.update();
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return !(!topHallEffect.get() && speed > 0);
    }


    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public void configureDashboard() {
        elevatorNamspace.putBoolean("elevator limit", limit::get);
        elevatorNamspace.putBoolean("bottom limit switch", bottomHallEffect::get);
        elevatorNamspace.putData("move lift upwards",
                new MoveGenericSubsystem(this, upSpeed));
        elevatorNamspace.putData("move lift downwards",
                new MoveGenericSubsystem(this, downSpeed));
    }
}
