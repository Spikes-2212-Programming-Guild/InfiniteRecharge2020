package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class TurretStateMachine extends StateMachine<TurretStateMachine.TurretState> {

    private Turret turret = Turret.getInstance();

    enum TurretState {
        OFF, VISION, MANUAL, ABSOLUTE,
    }

    private static TurretStateMachine instance;

    public static TurretStateMachine getInstance() {
        if(instance == null) {
            instance = new TurretStateMachine();
        }

        return instance;
    }

    private TurretStateMachine() {
        super(TurretState.OFF);
    }

    @Override
    protected void generateTransformations() {
        addTransformation(TurretState.OFF, new InstantCommand(() -> turret.setEnabled(false)));
        addTransformation(TurretState.VISION, new PrintCommand("not implemented")); //TODO implement
        addTransformation(TurretState.MANUAL, new PrintCommand("not implemented")); //TODO implement
        addTransformation(TurretState.ABSOLUTE, new PrintCommand("not implemented")); //TODO implement
    }
}
