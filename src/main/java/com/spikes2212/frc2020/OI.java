package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.*;
import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.frc2020.utils.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
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
    private PhysicsService physics = PhysicsService.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();

    public OI() {
        Button turret = controller.getRightStickButton();
        Button lift = controller.getButtonBack();
        Button climb = controller.getButtonStart();
        Button unLift = controller.getGreenButton();
        Button unClimb = controller.getLeftStickButton();
        Button openFeedToLevel1 = controller.getRedButton();
        Button closeFeedToLevel1 = controller.getBlueButton();
        Button unGrip = controller.getUpButton();
        Button unFeed = controller.getDownButton();
        Button shootFromAfar = controller.getLBButton();
        Button manualShoot = controller.getLeftButton();
        Trigger closeShoot = controller.getLTButton();
        Trigger feed = controller.getRTButton();
        Button intake = controller.getRBButton();
        Button grip = controller.getYellowButton();

        Button resetTurret = controller.getRightButton();
        resetTurret.whenPressed(new ResetTurret());
        intake.whileHeld(
                new ParallelCommandGroup(
                        new RepeatCommand(new IntakePowerCell())
                )
        );
        shootFromAfar.whenHeld(
                new LongRangeShoot()
        );
        closeShoot.whileActiveOnce(new CloseRangeShoot());
        manualShoot.whenHeld(new MoveGenericSubsystemWithPID(shooter, Shooter.velocityPIDSettings,
                () -> physics.calculateSpeedForDistance(vision.getDistanceFromTarget()), shooter::getMotorSpeed, Shooter.velocityFFSettings));
        feed.whileActiveOnce(new MoveGenericSubsystem(Feeder.getInstance(), Feeder.speed));
        openFeedToLevel1.whenPressed(new FeedToLowTarget()
        );
        closeFeedToLevel1.whenPressed(new InstantCommand(Feeder.getInstance()::close));
        unFeed.whileHeld(new MoveGenericSubsystem(Feeder.getInstance(), () -> -Feeder.speed.get()));
        unGrip.whileHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> -0.5 * RobotController.getBatteryVoltage()));
        grip.whenHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> Feeder.speed.get() / RobotController.getBatteryVoltage()));
        lift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().testSpeed));
        unLift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().untestSpeed));
        climb.whenHeld(
                new SequentialCommandGroup(
//                        new MoveTalonSubsystem(this.turret, Turret.climbingAngle, () -> (double) 0.1).withTimeout(2),
                        new MoveGenericSubsystem(Climber.getInstance(), Climber.getInstance().climbSpeed)
                )
        );
        unClimb.whenHeld(new MoveGenericSubsystem(Climber.getInstance(), Climber.getInstance().unClimbSpeed));
        turret.whileHeld(new MoveTalonSubsystem(Turret.getInstance(), this::getControllerRightAngle, Turret.getInstance().waitTime));
//        turret.whenHeld(new FeedToLowTarget());

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
        if(Math.abs(controller.getRightY()) < 0.1 && Math.abs(controller.getRightX()) < 0.1) return Turret.getInstance().getYaw();
        double angle =  Math.toDegrees(Math.atan2(-controller.getRightY(), controller.getRightX()));
        return ((angle % 360) + 360) % 360;
    }
}
