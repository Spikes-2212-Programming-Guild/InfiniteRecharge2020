package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.subsystems.Climber;
//import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Climb extends SequentialCommandGroup {

    private Climber climber = Climber.getInstance();
//    private Turret turret = Turret.getInstance();
//    private double setpoint = Turret.climbingAngle.get();
//    private double waitTime = Turret.waitTime.get();
    private double climbingSpeed = Climber.upSpeed.get();

    public Climb() {
        addCommands(
//                new MoveTalonSubsystem(turret, setpoint, () -> waitTime),
                new MoveGenericSubsystem(climber, () -> climbingSpeed));
    }

}
