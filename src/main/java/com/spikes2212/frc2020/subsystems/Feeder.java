package com.spikes2212.frc2020.subsystems;

import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.Namespace;
import com.spikes2212.lib.dashboard.RootNamespace;
import com.spikes2212.frc2020.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Feeder extends GenericSubsystem {

    public static final Namespace FEEDER = new RootNamespace("feeder");

    private static final double MIN_SPEED = -1;
    private static final double MAX_SPEED = 1;

    private static Feeder instance;

    private VictorSP motor;
    private DoubleSolenoid solenoid;

    public static Feeder getInstance() {
        if(instance == null) {
            VictorSP motor = new VictorSP(RobotMap.PWM.FEEDER_MOTOR);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.CAN.PCM, RobotMap.PCM.FEEDER_FORWARD,
                    RobotMap.PCM.FEEDER_BACKWARD);
            instance = new Feeder(MIN_SPEED, MAX_SPEED, motor, solenoid);
        }
        return instance;
    }

    public Feeder(double minSpeed, double maxSpeed, VictorSP motor, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.solenoid = solenoid;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
    }

    @Override
    public boolean canMove(double speed) { //TODO implement
        return false;
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    public void open() {solenoid.set(DoubleSolenoid.Value.kForward);}

    public void close() {solenoid.set(DoubleSolenoid.Value.kReverse);}

    public void initTestingDashboard() {
        Supplier<Double> speed = FEEDER.addConstantDouble("speed", 0.5);
        FEEDER.putData("feed", new MoveGenericSubsystem(this, speed));
        FEEDER.putData("open level 1", new InstantCommand(this::open, this));
        FEEDER.putData("close level 1", new InstantCommand(this::close, this));
    }
}
