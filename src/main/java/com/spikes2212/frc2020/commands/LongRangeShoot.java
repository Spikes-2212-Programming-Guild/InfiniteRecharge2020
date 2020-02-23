package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LongRangeShoot extends SequentialCommandGroup {

    private Shooter shooter = Shooter.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private PhysicsService physicsService = PhysicsService.getInstance();
    private VisionService visionService = VisionService.getInstance();
    private double speedSetpoint = 0;

    public LongRangeShoot() {
        addCommands(
                new OrientTurretToPowerPort(),
                new InstantCommand(shooter::close),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        () -> speedSetpoint, shooter::getMotorSpeed, Shooter.velocityFFSettings).alongWith(
                        new RepeatCommand(
                                new InstantCommand(this::updateSpeedSetpoint)
                        )),
                new InstantCommand(() -> shooter.setAccelerated(true)),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        () -> speedSetpoint, shooter::getMotorSpeed, Shooter.velocityFFSettings).perpetually().alongWith(
                        new RepeatCommand(
                                new InstantCommand(this::updateSpeedSetpoint)
                        ))
        );
    }

    @Override
    public void end(boolean interrupted) {
        shooter.setAccelerated(false);
    }

    private void updateSpeedSetpoint() {
        speedSetpoint = physicsService.calculateSpeedForDistance(visionService.getDistanceFromTarget());
    }

}
