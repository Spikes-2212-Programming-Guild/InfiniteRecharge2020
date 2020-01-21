package com.spikes2212.frc2020.subsystems;

import com.spikes2212.frc2020.RobotMap;
import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends GenericSubsystem {

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
}
