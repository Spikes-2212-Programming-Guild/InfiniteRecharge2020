package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class TurretStateMachine extends StateMachine<TurretStateMachine.TurretState> {

    enum TurretState {
        OFF, VISION, MANUAL, ABSOLUTE
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
        addTransformation(TurretState.OFF, new InstantCommand(()-> turret.setEnabled(false)));
//        addTransformation(TurretState.VISION, new MoveTalonSubsystem(turret,o,0));
        addTransformation(TurretState.MANUAL, new MoveTalonSubsystem(turret, Robot.oi::getRightX, () -> 0.0).perpetually()); //should be something else from OI
        addTransformation(TurretState.ABSOLUTE, new MoveTalonSubsystem(turret, () -> ,0).perpetually());
    }

}