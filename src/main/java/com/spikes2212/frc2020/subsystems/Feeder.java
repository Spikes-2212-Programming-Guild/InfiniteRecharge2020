package com.spikes2212.frc2020.subsystems;

import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.frc2020.RobotMap;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Feeder extends GenericSubsystem  {
    public static final double MIN_SPEED = -1;
    public static final double MAX_SPEED = 1;

    private static Feeder ourInstance;

    private VictorSP motor;
    private DoubleSolenoid solenoid;

    public static Feeder getInstance() {
        if(ourInstance == null) {
            VictorSP motor = new VictorSP(RobotMap.PWM.FEEDER_MOTOR);
            DoubleSolenoid solenoid = new DoubleSolenoid(RobotMap.PCM.FEEDER_FORWARD,
                    RobotMap.PCM.FEEDER_BACKWARD);
            ourInstance = new Feeder(MIN_SPEED, MAX_SPEED, motor, solenoid);
        }
        return ourInstance;
    }

    public Feeder(double minSpeed, double maxSpeed, VictorSP motor, DoubleSolenoid solenoid) {
        super(minSpeed, maxSpeed);
        this.motor = motor;
        this.solenoid = solenoid;
    }

    @Override
    public void apply(double speed) {

    }

    @Override
    public boolean canMove(double speed) {
        return false;
    }

    @Override
    public void stop() {

    }
}
