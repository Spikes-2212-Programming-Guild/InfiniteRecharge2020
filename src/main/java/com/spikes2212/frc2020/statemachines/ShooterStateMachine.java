package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ShooterStateMachine extends StateMachine<ShooterStateMachine.ShooterState> {

    public enum ShooterState {
        OFF, FAR, CLOSE
    }

    private static ShooterStateMachine instance;
    public static ShooterStateMachine getInstance() {
        if(instance == null) {
            instance = new ShooterStateMachine();
        }

        return instance;
    }

    private Shooter shooter = Shooter.getInstance();

    private ShooterStateMachine() {
        super(ShooterState.OFF);
    }

    @Override
    protected void generateTransformations() {
        addTransformation(ShooterState.OFF, new InstantCommand(() -> shooter.setEnabled(false), shooter));
        addTransformation(ShooterState.CLOSE, new InstantCommand(shooter::closeHood, shooter));
        addTransformation(ShooterState.FAR, new InstantCommand(shooter::openHood, shooter));
    }

}