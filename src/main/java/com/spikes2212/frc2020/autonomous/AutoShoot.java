package com.spikes2212.frc2020.autonomous;

import com.spikes2212.frc2020.commands.OrientTurretToPowerPort;
import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class AutoShoot extends SequentialCommandGroup {

    private Shooter shooter = Shooter.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private PhysicsService physicsService = PhysicsService.getInstance();
    private VisionService visionService = VisionService.getInstance();
    private double speedSetpoint = 0;

    public AutoShoot() {
        addCommands(
                new InstantCommand(shooter::close),
                new OrientTurretToPowerPort(),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        () -> physicsService.calculateSpeedForDistance(visionService.getDistanceFromTarget()),
                        shooter::getMotorSpeed, Shooter.velocityFFSettings),
                new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                        () -> physicsService.calculateSpeedForDistance(visionService.getDistanceFromTarget()),
                        shooter::getMotorSpeed, Shooter.velocityFFSettings).perpetually().alongWith(
                        new RepeatCommand(
                                new InstantCommand(this::updateSpeedSetpoint),
                                new WaitCommand(0.1)
                        ), new MoveGenericSubsystem(feeder, Feeder.speed))
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
