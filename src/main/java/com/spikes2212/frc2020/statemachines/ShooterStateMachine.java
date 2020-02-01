package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class ShooterStateMachine extends StateMachine<ShooterStateMachine.ShooterState> {

    enum ShooterState {
        OFF, ON,DISABLED,
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
        addTransformation(ShooterState.OFF, new MoveGenericSubsystem(shooter, 0));
        addTransformation(ShooterState.ON, new MoveGenericSubsystem(shooter, Shooter.shootSpeed));
        addTransformation(ShooterState.DISABLED, new InstantCommand(()->shooter.setEnabled(false)));
    }

}