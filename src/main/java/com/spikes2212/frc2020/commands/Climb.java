package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Climber;
import com.spikes2212.lib.control.PIDSettings;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

public class Climb extends CommandBase {

    private Supplier<Double> targetSetpoint;
    private int numberOfSetpoints;
    private double currentSetpoint;
    private Climber climber = Climber.getInstance();

    private PIDController leftController;
    private PIDController rightController;

    public Climb(Supplier<Double> targetSetpoint, int numberOfSetpoints, PIDSettings pidSettings) {
        addRequirements(climber);
        this.targetSetpoint = targetSetpoint;
        this.numberOfSetpoints = numberOfSetpoints;
        currentSetpoint = targetSetpoint.get() / numberOfSetpoints;
        leftController = new PIDController(pidSettings.getkP(), pidSettings.getkI(),
                pidSettings.getkD());
        rightController = new PIDController(pidSettings.getkP(), pidSettings.getkI(),
                pidSettings.getkD());
    }

    @Override
    public void execute() {
        if(leftController.atSetpoint() && rightController.atSetpoint()) {
            currentSetpoint += targetSetpoint.get() / numberOfSetpoints;
        }

        leftController.setSetpoint(currentSetpoint);
        rightController.setSetpoint(currentSetpoint);
        climber.setLeft(leftController.calculate(climber.getLeftDistance()));
        climber.setRight(rightController.calculate(climber.getRightDistance()));
    }

    @Override
    public boolean isFinished() {
        return leftController.atSetpoint() && rightController.atSetpoint() &&
                currentSetpoint == targetSetpoint.get();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopLeft();
        climber.stopRight();
    }
}
