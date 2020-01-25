package com.spikes2212.frc2020.commands;

import com.spikes2212.control.PIDSettings;
import com.spikes2212.frc2020.subsystems.Climber;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Climb extends CommandBase {

    private double targetSetpoint;
    private int numberOfSetpoints;
    private double currentSetpoint;
    private Climber climber;

    private PIDController leftController, rightController;

    public Climb(double targetSetpoint, int numberOfSetpoints, PIDSettings pidSettings) {
        climber=Climber.getInstance();
        addRequirements(climber);
        this.targetSetpoint = targetSetpoint;
        this.numberOfSetpoints = numberOfSetpoints;
        currentSetpoint = targetSetpoint / numberOfSetpoints;
        leftController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
        rightController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
    }

    @Override
    public void execute() {
        if (leftController.atSetpoint() && rightController.atSetpoint())
            currentSetpoint += targetSetpoint / numberOfSetpoints;
        leftController.setSetpoint(currentSetpoint);
        rightController.setSetpoint(currentSetpoint);
        climber.setLeftMotor(leftController.calculate(climber.getLeftDistance()));
        climber.setRightMotor(rightController.calculate(climber.getLeftDistance()));
    }

    @Override
    public boolean isFinished() {
        return (leftController.atSetpoint() && rightController.atSetpoint() && currentSetpoint == targetSetpoint);
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopLeftMotor();
        climber.stopRightMotor();
    }
}
