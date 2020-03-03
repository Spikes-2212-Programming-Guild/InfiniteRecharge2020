package com.spikes2212.lib.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * This {@link Command} repeats a sequence of commands indefinitely
 */

public class RepeatCommand extends CommandBase {

    private Command command;

    public RepeatCommand(Command... commands) {
        for (Command c : commands) addRequirements(c.getRequirements().toArray(new Subsystem[]{}));
        this.command = new SequentialCommandGroup(commands);
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
