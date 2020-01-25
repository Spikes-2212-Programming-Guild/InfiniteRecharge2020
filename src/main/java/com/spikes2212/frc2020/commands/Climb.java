package com.spikes2212.frc2020.commands;

import com.spikes2212.control.PIDSettings;
import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.subsystems.Climbing;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Climb extends CommandBase {

    private double lastSetpoint;
    private int numberOfSetpoints;
    private double setPoint;

    private PIDController leftController, rightController;

    public Climb(double lastSetpoint, int numberOfSetpoints ,PIDSettings pidSettings) {
        addRequirements(Climbing.getInstance());
        this.lastSetpoint = lastSetpoint;
        this.numberOfSetpoints=numberOfSetpoints;
        setPoint=lastSetpoint/numberOfSetpoints;
        leftController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
        rightController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
    }

    @Override
    public void execute() {
        if(leftController.atSetpoint()&&rightController.atSetpoint())
            setPoint+=lastSetpoint/numberOfSetpoints;
        leftController.setSetpoint(setPoint);
        rightController.setSetpoint(setPoint);
        Climbing.getInstance().setLeftMotor(leftController.calculate(Climbing.getInstance().getLeftDistance()));
        Climbing.getInstance().setRightMotor(rightController.calculate(Climbing.getInstance().getLeftDistance()));
    }

    @Override
    public boolean isFinished() {
        return (leftController.atSetpoint()&&rightController.atSetpoint()&&setPoint==lastSetpoint);
    }

    @Override
    public void end(boolean interrupted) {
        Climbing.getInstance().stopLeftMotor();
        Climbing.getInstance().stopRightMotor();
    }
}
