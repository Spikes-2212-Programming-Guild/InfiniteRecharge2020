package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Climber;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

public class MoveClimberWithSpeed extends CommandBase {

    Supplier<Double> speed;
    Climber climber;

    public MoveClimberWithSpeed(Supplier<Double> speed){
        addRequirements(climber);
        this.speed = speed;
        climber=Climber.getInstance();
    }

    @Override
    public void execute(){
        climber.move(speed);
    }

    @Override
    public void end(boolean interrupted){
        climber.stop();
    }
}
