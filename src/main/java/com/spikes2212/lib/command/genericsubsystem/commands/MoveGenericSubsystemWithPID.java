package com.spikes2212.lib.command.genericsubsystem.commands;

import com.spikes2212.lib.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.lib.control.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;

/**
 * This command moves a {@link GenericSubsystem} according to a {@link Supplier}
 * or a constant speed until it can't move anymore.
 *
 * @author Yuval Levy
 * @see GenericSubsystem
 */
public class MoveGenericSubsystemWithPID extends CommandBase {
    /**
     * the subsystem the command moves.
     */
    private final GenericSubsystem subsystem;

    /**
     * A supplier that returns the subsystem's current location.
     */
    private Supplier<Double> source;

    /**
     * The PID Settings for the PID control loop.
     */
    private PIDSettings pidSettings;

    /**
     * The Feed Forward Settings for the feed forward control loop.
     */
    private FeedForwardSettings feedForwardSettings;

    /**
     * the setpoint for the subsystem.
     */
    private Supplier<Double> setpoint;

    /**
     * An object that makes the necessary calculations for the PID control loop.
     */
    private PIDController pidController;

    /**
     * An object that makes the necessary calculations for the feed forward control loop.
     */
    private FeedForwardController feedForwardController;

    /**
     * The last time the subsystem didn't reach target.
     */
    private double lastTimeNotOnTarget;

    public MoveGenericSubsystemWithPID(GenericSubsystem subsystem, PIDSettings pidSettings,
                                       Supplier<Double> setpoint, Supplier<Double> source,
                                       FeedForwardSettings feedForwardSettings) {
        addRequirements(subsystem);
        this.subsystem = subsystem;
        this.pidSettings = pidSettings;
        this.feedForwardSettings = feedForwardSettings;
        this.setpoint = setpoint;
        this.source = source;
        this.feedForwardController = new FeedForwardController(feedForwardSettings.getkS(), feedForwardSettings.getkV(), feedForwardSettings.getkA(), feedForwardSettings.getkG(), 0.02);
        this.pidController = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
    }

    public MoveGenericSubsystemWithPID(GenericSubsystem subsystem, PIDSettings pidSettings,
                                       double setpoint, double source,
                                       FeedForwardSettings feedForwardSettings) {
        this(subsystem, pidSettings, () -> setpoint, () -> source, feedForwardSettings);
    }

    public MoveGenericSubsystemWithPID(GenericSubsystem subsystem, PIDSettings pidSettings,
                                       Supplier<Double> setpoint, Supplier<Double> source) {
        this(subsystem, pidSettings, setpoint, source, FeedForwardSettings.EMPTY_FFSETTINGS);
    }

    public MoveGenericSubsystemWithPID(GenericSubsystem subsystem, PIDSettings pidSettings,
                                       double setpoint, double source) {
        this(subsystem, pidSettings, () -> setpoint, () -> source, FeedForwardSettings.EMPTY_FFSETTINGS);
    }

    @Override
    public void execute() {
        pidController.setTolerance(pidSettings.getTolerance());
        pidController.setPID(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD());
        feedForwardController.setGains(feedForwardSettings.getkS(), feedForwardController.getkV(),
                feedForwardController.getkA(), feedForwardController.getkG());

        double pidValue = pidController.calculate(source.get(), setpoint.get());
        double svagValue = feedForwardController.calculate(setpoint.get());
        subsystem.move((pidValue + svagValue) / 2);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stop();
    }

    @Override
    public boolean isFinished() {
        if (!pidController.atSetpoint()) {
            lastTimeNotOnTarget = Timer.getFPGATimestamp();
        }

        return Timer.getFPGATimestamp() - lastTimeNotOnTarget >= pidSettings.getWaitTime();
    }
}
