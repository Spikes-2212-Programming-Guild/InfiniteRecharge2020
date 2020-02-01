package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class TurretStateMachine extends StateMachine<TurretStateMachine.TurretState> {

    enum TurretState {
        OFF, VISION, MANUAL, ABSOLUTE,
    }

    private static TurretStateMachine instance;
    public static TurretStateMachine getInstance() {
        if (instance == null)
            instance = new TurretStateMachine();
        return instance;
    }

    private TurretStateMachine() {
        super(TurretState.OFF);
    }

    private Turret turret = Turret.getInstance();

    @Override
    protected void generateTransformations() {
        addTransformation(TurretState.OFF, new PrintCommand("implement")); //@TODO@//////////////////////////////////
        addTransformation(TurretState.VISION, new PrintCommand("implement")); //@TODO@///////////////////////////////
        addTransformation(TurretState.MANUAL, new PrintCommand("implement")); //@TODO@///////////////////////////////
        addTransformation(TurretState.ABSOLUTE, new PrintCommand("implement")); //@TODO@/////////////////////////////
    }

}