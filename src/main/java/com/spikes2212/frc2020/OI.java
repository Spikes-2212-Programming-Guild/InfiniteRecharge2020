package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.util.XboXUID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Button;
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
        Button turretLeft = controller.getLeftButton();
        Button turretRight = controller.getRightButton();
        Button lift = controller.getButtonBack();
        Button climb = controller.getButtonStart();
        Button unLift = controller.getRightStickButton();
        Button unClimb = controller.getLeftStickButton();
        Button openFeedToLevel1 = controller.getRedButton();
        Button closeFeedToLevel1 = controller.getBlueButton();
        Button unGrip = controller.getUpButton();
        Button unFeed = controller.getDownButton();
        Button shootFromAfar = controller.getLBButton();
        Trigger closeShoot = controller.getLTButton();
        Trigger feed = controller.getRTButton();
        Button intake = controller.getRBButton();
        Button grip = controller.getYellowButton();
        intake.whileHeld(
                new ParallelCommandGroup(
                        new RepeatCommand(new IntakePowerCell())
                )
        );
        shootFromAfar.toggleWhenActive(new SequentialCommandGroup(new InstantCommand(() -> shooter.open()), new MoveGenericSubsystem(shooter,
                () -> Shooter.farShootingSpeed.get() / RobotController.getBatteryVoltage())));
        closeShoot.whileActiveOnce(new SequentialCommandGroup(new InstantCommand(() -> shooter.close()), new MoveGenericSubsystem(shooter,
                () -> Shooter.shootSpeed.get() / RobotController.getBatteryVoltage())));
        feed.whileActiveOnce(new MoveGenericSubsystem(Feeder.getInstance(), Feeder.speed));
        openFeedToLevel1.whenPressed(new InstantCommand(Feeder.getInstance()::close));
        closeFeedToLevel1.whenPressed(new InstantCommand(Feeder.getInstance()::open));
        unFeed.whileHeld(new MoveGenericSubsystem(Feeder.getInstance(), () -> -Feeder.speed.get()));
        unGrip.whileHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> -0.5 * RobotController.getBatteryVoltage()));
        grip.whenHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> Feeder.speed.get() / RobotController.getBatteryVoltage()));
        lift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().testSpeed));
        unLift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().untestSpeed));
        climb.whenHeld(new MoveGenericSubsystem(Climber.getInstance(), Climber.getInstance().climbSpeed));
        unClimb.whenHeld(new MoveGenericSubsystem(Climber.getInstance(), Climber.getInstance().unClimbSpeed));
        turretRight.whileHeld(new MoveTalonSubsystem(Turret.getInstance(), this::getControllerRightAngle, () -> 0.0));
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
        if(controller.getRightY() < 0.2 && controller.getRightX() < 0.2) return Turret.getInstance().getYaw();
        return Math.atan2(controller.getRightY(), controller.getRightX());
    }
}
