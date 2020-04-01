package com.spikes2212.frc2020.commands;

import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.lib.command.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LongRangeShoot extends SequentialCommandGroup {

    private Shooter shooter = Shooter.getInstance();
    private PhysicsService physicsService = PhysicsService.getInstance();
    private VisionService visionService = VisionService.getInstance();
    private double speedSetpoint = 0;

    public LongRangeShoot() {
        addCommands(
                new InstantCommand(shooter::close),
                new OrientTurretToPowerPort(),
                new MoveGenericSubsystemWithPID(shooter,
                        () -> physicsService.calculateSpeedForDistance(
                                visionService.getDistanceFromTarget()),
                        shooter::getMotorSpeed,  Shooter.velocityPIDSettings, Shooter.velocityFFSettings),
                new InstantCommand(() -> shooter.setAccelerated(true)),
                new MoveGenericSubsystemWithPID(shooter,
                        () -> speedSetpoint,
                        shooter::getMotorSpeed,  Shooter.velocityPIDSettings, Shooter.velocityFFSettings)
                        .perpetually().alongWith(
                new RepeatCommand(
                        new InstantCommand(this::updateSpeedSetpoint)
                ))
        );
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        shooter.setAccelerated(false);
    }

    private void updateSpeedSetpoint() {
        speedSetpoint = physicsService.calculateSpeedForDistance(visionService.getDistanceFromTarget());
    }


}
