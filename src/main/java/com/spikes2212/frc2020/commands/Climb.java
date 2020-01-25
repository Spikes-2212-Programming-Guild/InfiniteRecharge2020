package com.spikes2212.frc2020.commands;

import com.spikes2212.control.PIDSettings;
import com.spikes2212.frc2020.subsystems.Climber;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Climb extends CommandBase {

    private double lastSetpoint;
    private int numberOfSetpoints;
    private double setPoint;

    private PIDController leftController, rightController;

    public Climb(double lastSetpoint, int numberOfSetpoints, PIDSettings pidSettings) {
        addRequirements(Climber.getInstance());
        this.lastSetpoint = lastSetpoint;
        this.numberOfSetpoints = numberOfSetpoints;
        setPoint = lastSetpoint / numberOfSetpoints;
        leftController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
        rightController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
    }

    @Override
    public void execute() {
        if (leftController.atSetpoint() && rightController.atSetpoint())
            setPoint += lastSetpoint / numberOfSetpoints;
        leftController.setSetpoint(setPoint);
        rightController.setSetpoint(setPoint);
        Climber.getInstance().setLeftMotor(leftController.calculate(Climber.getInstance().getLeftDistance()));
        Climber.getInstance().setRightMotor(rightController.calculate(Climber.getInstance().getLeftDistance()));
    }

    @Override
    public boolean isFinished() {
        return (leftController.atSetpoint() && rightController.atSetpoint() && setPoint == lastSetpoint);
    }

    @Override
    public void end(boolean interrupted) {
        Climber.getInstance().stopLeftMotor();
        Climber.getInstance().stopRightMotor();
    }
}
