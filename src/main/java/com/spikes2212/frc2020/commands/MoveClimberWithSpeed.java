package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

public class MoveClimberWithSpeed extends CommandBase {

  private Climber climber = Climber.getInstance();
  private Supplier<Double> leftSpeed;
  private Supplier<Double> rightSpeed;

  public MoveClimberWithSpeed(Supplier<Double> leftSpeed, Supplier<Double> rightSpeed) {
    addRequirements(climber);
    this.leftSpeed = leftSpeed;
    this.rightSpeed = rightSpeed;
  }

  @Override
  public void execute() {
    climber.setLeft(leftSpeed.get());
    climber.setRight(rightSpeed.get());
  }

  @Override
  public void end(boolean interrupted) {
    climber.stop();
  }
}
