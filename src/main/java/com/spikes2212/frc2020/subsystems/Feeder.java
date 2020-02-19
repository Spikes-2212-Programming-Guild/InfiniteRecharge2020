package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.dashboard.RootNamespace;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.function.Supplier;

public class Feeder extends GenericSubsystem {

    public static RootNamespace feederNamespace = new RootNamespace("feeder");

    private static final Supplier<Double> minSpeed = feederNamespace.addConstantDouble("min speed", -1);
    private static final Supplier<Double> maxSpeed = feederNamespace.addConstantDouble("max speed", 1);
    public static final Supplier<Double> speed = feederNamespace.addConstantDouble("speed", 0.7);
    public static final Supplier<Double> feedTimeLimit = feederNamespace.addConstantDouble("feeding time", 2);

    private static Feeder instance;

    public static Feeder getInstance() {
        if(instance == null) {
            WPI_VictorSPX motor = new WPI_VictorSPX(RobotMap.CAN.FEEDER_VICTOR);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.CAN.PCM, RobotMap.PCM.FEEDER_FORWARD,
                    RobotMap.PCM.FEEDER_BACKWARD);
            instance = new Feeder(motor, solenoid);
        }

        return instance;
    }

    private WPI_VictorSPX motor;
    private DoubleSolenoid solenoid;

    private boolean enabled;

    public Feeder(WPI_VictorSPX motor, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.solenoid = solenoid;
        enabled = true;
    }

    @Override
    public void apply(double speed) {
        motor.set(speed);
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
        feederNamespace.update();
    }

    public void open() {
        solenoid.set(DoubleSolenoid.Value.kForward);
        setEnabled(true);
    }

    public void close() {
        solenoid.set(DoubleSolenoid.Value.kReverse);
        setEnabled(true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void configureDashboard() {
        feederNamespace.putData("feed", new MoveGenericSubsystem(this, speed));
        feederNamespace.putData("open level 1", new InstantCommand(this::open, this));
        feederNamespace.putData("close level 1", new InstantCommand(this::close, this));
    }

}
