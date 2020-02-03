package com.spikes2212.frc2020.statemachines;

import com.spikes2212.frc2020.Robot;
import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.state.StateMachine;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;

public class TurretStateMachine extends StateMachine<TurretStateMachine.TurretState> {

    enum TurretState {
        OFF, AUTOMATIC, MANUAL
    }

    private static TurretStateMachine instance;
    private Drivetrain drivetrain = Drivetrain.getInstance();
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
        addTransformation(TurretState.MANUAL, new InstantCommand(() -> turret.setManualDefaultCommand()));
        /* target angle for AUTOMATIC state should be the angle Vision service returns. */
//        addTransformation(TurretState.AUTOMATIC, new MoveTalonSubsystem(turret, targetAngle, 0).perpetually();
    }

}