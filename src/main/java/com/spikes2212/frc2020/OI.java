package com.spikes2212.frc2020;

import com.spikes2212.frc2020.commands.IntakePowerCell;
import com.spikes2212.frc2020.services.VisionService;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.frc2020.subsystems.Shooter;
import com.spikes2212.frc2020.subsystems.Turret;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveGenericSubsystem;
import com.spikes2212.lib.command.genericsubsystem.commands.MoveTalonSubsystem;
import com.spikes2212.lib.util.XboXUID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class OI /* GEVALD */ {

    private Joystick left = new Joystick(0);
    private Joystick right = new Joystick(1);
    private XboXUID controller = new XboXUID(2);

    private Shooter shooter = Shooter.getInstance();
    private Turret turret = Turret.getInstance();

    private VisionService vision = VisionService.getInstance();

    public OI() {
        JoystickButton intake = new JoystickButton(left, 1);
        intake.whileHeld(
                new IntakePowerCell()
        );
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
        return Math.atan2(controller.getRightY(),controller.getRightX());
    }
}
