package com.spikes2212.frc2020.utils;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This {@link Command} repeats a sequence of commands indefinitely
 */

public class RepeatCommand extends CommandBase {

    private Command command;

    public RepeatCommand(Command... command) {
        this.command = new SequentialCommandGroup(command);
    }

    @Override
    public void initialize() {
        command.initialize();
    }

    @Override
    public void execute() {
        command.execute();
        if (command.isFinished()) {
            command.initialize();
        }
    }

    @Override
    public void end(boolean interrupted) {
        command.end(interrupted);
    }
}
