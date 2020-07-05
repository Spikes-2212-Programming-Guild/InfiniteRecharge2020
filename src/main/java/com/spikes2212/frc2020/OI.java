package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.*;
import com.spikes2212.frc2020.services.PhysicsService;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.*;
import com.spikes2212.lib.command.RepeatCommand;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystemWithPID;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.util.XboXUID;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
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
    private Climber climber = Climber.getInstance();
    private Feeder feeder = Feeder.getInstance();
    private VisionService vision = VisionService.getInstance();
    private PhysicsService physics = PhysicsService.getInstance();
    private NetworkTable feederNetworkTable = NetworkTableInstance.getDefault().getTable("feeder");
    private NetworkTableEntry feederSpeed = feederNetworkTable.getEntry("speed");
    private NetworkTable intakeNetworkTable = NetworkTableInstance.getDefault().getTable("intake");
    private NetworkTableEntry intakeSpeed = intakeNetworkTable.getEntry("intake voltage");

    public OI() {
        Button turret = controller.getRightStickButton();
        Button lift = controller.getButtonBack();
        Button climbUp = controller.getButtonStart();
        Button unLift = controller.getGreenButton();
        Button climbDown = controller.getLeftStickButton();
        Button unGrip = controller.getUpButton();
        Button unFeed = controller.getDownButton();
        Button shootFromAfar = controller.getLBButton();
        Button manualShoot = controller.getLeftButton();
        Trigger closeShoot = controller.getLTButton();
        Trigger feed = controller.getRTButton();
        Button intake = controller.getRBButton();
        Button grip = controller.getYellowButton();
        JoystickButton reverseFeederSpeed = new JoystickButton(left, 1);
        JoystickButton reverseIntakeSpeed = new JoystickButton(right, 1);
        JoystickButton stopShooter = new JoystickButton(left,0);
        Button resetTurret = controller.getRightButton();

        resetTurret.whenPressed(new ResetTurret());

        reverseFeederSpeed.whenPressed(new InstantCommand(() -> feederSpeed.setNumber(-feederSpeed.getDouble(0))));
        reverseIntakeSpeed.whenPressed(new InstantCommand(() -> intakeSpeed.setNumber(-intakeSpeed.getDouble(0))));
        intake.whileHeld(
                new ParallelCommandGroup(
                        new RepeatCommand(new IntakePowerCell())
                )
        );
        shootFromAfar.whenHeld(
                new LongRangeShoot()
        );
        closeShoot.whileActiveOnce(new CloseRangeShoot());
        manualShoot.whenHeld(new MoveGenericSubsystemWithPID(shooter,
                () -> physics.calculateSpeedForDistance(vision.getDistanceFromTarget()),
                shooter::getMotorSpeed, Shooter.velocityPIDSettings, Shooter.velocityFFSettings));
        feed.whileActiveOnce(new MoveGenericSubsystem(Feeder.getInstance(), Feeder.speed));
        unFeed.whileHeld(new MoveGenericSubsystem(Feeder.getInstance(), () -> -Feeder.speed.get()));
        unGrip.whileHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> -0.5 * RobotController.getBatteryVoltage()));
        grip.whenHeld(new MoveGenericSubsystem(Intake.getInstance(), () -> Feeder.speed.get() / RobotController.getBatteryVoltage()));
        lift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().upSpeed));
        unLift.whenHeld(new MoveGenericSubsystem(Elevator.getInstance(), Elevator.getInstance().downSpeed));
        climbUp.whenHeld(
                new SequentialCommandGroup(
                        new MoveTalonSubsystem(this.turret, Turret.climbingAngle, () -> 0.1).withTimeout(2),
                        new MoveGenericSubsystem(Climber.getInstance(), Climber.upSpeed)
                )
        );
        climbDown.whenHeld(new MoveGenericSubsystem(Climber.getInstance(), Climber.downSpeed));
        turret.whileHeld(new MoveTalonSubsystem(Turret.getInstance(), this::getTurretSetpoint, Turret.waitTime));

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

    public double getTurretSetpoint() {
        if(Math.abs(controller.getRightY()) < 0.1 && Math.abs(controller.getRightX()) < 0.1) return Turret.getInstance().getYaw();
        double angle =  Math.toDegrees(Math.atan2(-controller.getRightY(), controller.getRightX()));
        return ((angle % 360) + 360) % 360;
    }
}
