package com.spikes2212.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import com.spikes2212.frc2020.RobotMap;

public class Shooter extends GenericSubsystem {
    private static Shooter instance;

    private WPI_TalonSRX master;
    private WPI_TalonSRX slave;

    private Shooter(WPI_TalonSRX master, WPI_TalonSRX slave) {
        this.master = master;
        this.slave = slave;
    }

    public static Shooter getInstance() {
        if(instance == null)
            instance = new Shooter(new WPI_TalonSRX(RobotMap.CAN.SHOOTER_MASTER),
                    new WPI_TalonSRX(RobotMap.CAN.SHOOTER_SLAVE));

        return instance;
    }

    @Override
    public void apply(double speed) {
        master.set(speed);
    }

    @Override
    public boolean canMove(double speed) {
        return true;
    }

    @Override
    public void stop() {
        master.stopMotor();
    }
}
