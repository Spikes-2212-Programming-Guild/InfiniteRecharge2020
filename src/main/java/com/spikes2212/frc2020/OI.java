package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.commands.OrientToPowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.util.XboXUID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class OI /* GEVALD */ {

    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);
    private XboXUID controller = new XboXUID(2);

    private Shooter shooter = Shooter.getInstance();
    private Turret turret = Turret.getInstance();

    private VisionService vision = VisionService.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();

    public OI() {
        Button openFeedToLevel1 = controller.getRedButton();
        Button closeFeedToLevel1 = controller.getBlueButton();
        Button unGrip = controller.getButtonStart();
        Button unFeed = controller.getButtonBack();
        Trigger shootFromAfar = controller.getLTButton();
        Button closeShoot = controller.getLBButton();
        Button feed = controller.getGreenButton();
        Trigger grip = controller.getRTButton();
        JoystickButton intake = new JoystickButton(right, 1);
        intake.whileHeld(
                new ParallelCommandGroup(
                        new OrientToPowerCell(this::getRightY),
                        new RepeatCommand(new IntakePowerCell())
                )
        );
        shootFromAfar.toggleWhenActive(new SequentialCommandGroup(new InstantCommand(() ->shooter.open()),new MoveGenericSubsystem(shooter,
                () -> Shooter.farShootingSpeed.get() / RobotController.getBatteryVoltage())));
        closeShoot.toggleWhenActive(new SequentialCommandGroup(new InstantCommand(() ->shooter.close()),new MoveGenericSubsystem(shooter,
                () -> Shooter.shootSpeed.get() / RobotController.getBatteryVoltage())));
        grip.whileActiveOnce(new IntakePowerCell());
        feed.whenHeld(new MoveGenericSubsystem(Feeder.getInstance(), Feeder.speed));
        openFeedToLevel1.whenPressed(new InstantCommand(Feeder.getInstance()::close));
        closeFeedToLevel1.whenPressed(new InstantCommand(Feeder.getInstance()::open));
        unFeed.whileHeld(new MoveGenericSubsystem(Feeder.getInstance(), () -> -Feeder.speed.get()));
        unGrip.whileHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> -0.5 * RobotController.getBatteryVoltage()));
    }

    public double getLeftX() {
        return left.getX();
    }

    public double getLeftY() {
        return left.getY();
    }

    public double getRightX() {
        return right.getX();
    }

    public double getRightY() {
        return -right.getY();
    }

    public double getControllerRightAngle() {
        return Math.atan2(controller.getRightY(), controller.getRightX());
    }
}
