package com.spikes2212.frc2020;

import com.spikes2212.frc2020.subsystems.Drivetrain;
import com.spikes2212.frc2020.subsystems.Feeder;
import com.spikes2212.frc2020.subsystems.Intake;
import com.spikes2212.lib.command.drivetrains.commands.DriveArcade;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

    private static OI oi;

    @Override
    public void robotInit() {
        Feeder.getInstance().configureDashboard();
        Intake.getInstance().configureDashboard();
        oi = new OI();
        Drivetrain.getInstance().setDefaultCommand(new DriveArcade(Drivetrain.getInstance(),
                oi::getLeftY, oi::getRightX));
    }
}
