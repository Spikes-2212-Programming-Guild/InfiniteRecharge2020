package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class Climb extends CommandBase {

    private double setPoint;
    private double speed;

    public Climb(double setPoint, double speed) {
        addRequirements((Subsystem) Robot.climbing);
        this.setPoint = setPoint;
        this.speed = speed;
    }

    @Override
    public void execute() {
        while (Robot.climbing.getLeftDistance() < setPoint || Robot.climbing.getRightDistance() < setPoint) {

            if (Robot.climbing.getLeftDistance() < setPoint)
                Robot.climbing.stopLeftMotor();
            else {
                if (Robot.climbing.getRightDistance() < setPoint)
                    Robot.climbing.stopRightMotor();
                else {
                    Robot.climbing.setLeftMotor(speed);
                    Robot.climbing.setRightMotor(speed);
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return (Robot.climbing.getLeftDistance() >= setPoint && Robot.climbing.getRightDistance() >= setPoint);
    }
}
