package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ShooterStateMachine extends StateMachine<ShooterStateMachine.ShooterState> {

    enum ShooterState {
        OFF, FAR, CLOSE
    }

    private static ShooterStateMachine instance;
    public static ShooterStateMachine getInstance() {
        if (instance == null)
            instance = new ShooterStateMachine();
        return instance;
    }

    private ShooterStateMachine() {
        super(ShooterState.OFF);
    }

    private Shooter shooter = Shooter.getInstance();

    @Override
    protected void generateTransformations() {
        addTransformation(ShooterState.CLOSE, new InstantCommand(shooter::closeHood));
        addTransformation(ShooterState.FAR, new InstantCommand(shooter::openHood));
        addTransformation(ShooterState.OFF, new InstantCommand(()->shooter.setEnabled(false),shooter));
    }

}