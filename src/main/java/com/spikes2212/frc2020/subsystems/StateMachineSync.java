package com.spikes2212.frc2020.subsystems;

public enum StateMachineSync {
    CLOSE_INTAKE_OFF_FEEDER_OFF_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_OFF_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.OFF),
    CLOSE_INTAKE_OFF_FEEDER_OFF_SHOOTER_VISION_TURRET(IntakeFeederSync.CLOSE_INTAKE_OFF_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.VISION),
    CLOSE_INTAKE_OFF_FEEDER_OFF_SHOOTER_MANUAL_TURRET(IntakeFeederSync.CLOSE_INTAKE_OFF_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.MANUAL),
    CLOSE_INTAKE_OFF_FEEDER_OFF_SHOOTER_ABSOLUTE_TURRET(IntakeFeederSync.CLOSE_INTAKE_OFF_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.ABSOLUTE),
    CLOSE_INTAKE_OFF_FEEDER_ON_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_OFF_FEEDER,Shooter.ShooterState.ON,Turret.TurretState.OFF),
    CLOSE_INTAKE_ON_FEEDER_OFF_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_ON_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.OFF),
    CLOSE_INTAKE_ON_FEEDER_OFF_SHOOTER_VISION_TURRET(IntakeFeederSync.CLOSE_INTAKE_ON_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.VISION),
    CLOSE_INTAKE_ON_FEEDER_OFF_SHOOTER_MANUAL_TURRET(IntakeFeederSync.CLOSE_INTAKE_ON_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.MANUAL),
    CLOSE_INTAKE_ON_FEEDER_OFF_SHOOTER_ABSOLUTE_TURRET(IntakeFeederSync.CLOSE_INTAKE_ON_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.ABSOLUTE),
    CLOSE_INTAKE_ON_FEEDER_ON_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_ON_FEEDER,Shooter.ShooterState.ON,Turret.TurretState.OFF),
    CLOSE_INTAKE_OPEN_FEEDER_OFF_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_OPEN_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.OFF),
    CLOSE_INTAKE_OPEN_FEEDER_OFF_SHOOTER_VISION_TURRET(IntakeFeederSync.CLOSE_INTAKE_OPEN_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.VISION),
    CLOSE_INTAKE_OPEN_FEEDER_OFF_SHOOTER_MANUAL_TURRET(IntakeFeederSync.CLOSE_INTAKE_OPEN_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.MANUAL),
    CLOSE_INTAKE_OPEN_FEEDER_OFF_SHOOTER_ABSOLUTE_TURRET(IntakeFeederSync.CLOSE_INTAKE_OPEN_FEEDER,Shooter.ShooterState.OFF,Turret.TurretState.ABSOLUTE),
    CLOSE_INTAKE_OPEN_FEEDER_ON_SHOOTER_OFF_TURRET(IntakeFeederSync.CLOSE_INTAKE_OPEN_FEEDER,Shooter.ShooterState.ON,Turret.TurretState.OFF),
    OPEN_INTAKE_ON_FEEDER_OFF_SHOOTER_OFF_TURRET(IntakeFeederSync.OPEN_INTAKE_ON_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.OFF),
    OPEN_INTAKE_ON_FEEDER_OFF_SHOOTER_VISION_TURRET(IntakeFeederSync.OPEN_INTAKE_ON_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.VISION),
    OPEN_INTAKE_ON_FEEDER_OFF_SHOOTER_MANUAL_TURRET(IntakeFeederSync.OPEN_INTAKE_ON_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.MANUAL),
    OPEN_INTAKE_ON_FEEDER_OFF_SHOOTER_ABSOLUTE_TURRET(IntakeFeederSync.OPEN_INTAKE_ON_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.ABSOLUTE),
    OPEN_INTAKE_ON_FEEDER_ON_SHOOTER_OFF_TURRET(IntakeFeederSync.OPEN_INTAKE_ON_FEEDER, Shooter.ShooterState.ON, Turret.TurretState.OFF),
    OPEN_INTAKE_OPEN_FEEDER_OFF_SHOOTER_OFF_TURRET(IntakeFeederSync.OPEN_INTAKE_OPEN_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.OFF),
    OPEN_INTAKE_OPEN_FEEDER_OFF_SHOOTER_VISION_TURRET(IntakeFeederSync.OPEN_INTAKE_OPEN_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.VISION),
    OPEN_INTAKE_OPEN_FEEDER_OFF_SHOOTER_MANUAL_TURRET(IntakeFeederSync.OPEN_INTAKE_OPEN_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.MANUAL),
    OPEN_INTAKE_OPEN_FEEDER_OFF_SHOOTER_ABSOLUTE_TURRET(IntakeFeederSync.OPEN_INTAKE_OPEN_FEEDER, Shooter.ShooterState.OFF, Turret.TurretState.ABSOLUTE),
    OPEN_INTAKE_OPEN_FEEDER_ON_SHOOTER_OFF_TURRET(IntakeFeederSync.OPEN_INTAKE_OPEN_FEEDER, Shooter.ShooterState.ON, Turret.TurretState.OFF);

    private IntakeFeederSync intakeFeederSync;
    private Shooter.ShooterState shooterState;
    private Turret.TurretState turretState;
    StateMachineSync(IntakeFeederSync intakeFeederSync, Shooter.ShooterState shooterState, Turret.TurretState turretState){
        this.intakeFeederSync = intakeFeederSync;
        this.shooterState = shooterState;
        this.turretState = turretState;
    }

    public IntakeFeederSync getIntakeFeederSync() {
        return intakeFeederSync;
    }

    public Shooter.ShooterState getShooterState() {
        return shooterState;
    }

    public Turret.TurretState getTurretState() {
        return turretState;
    }

    public void setState() {
        getIntakeFeederSync().setState();
        Turret.getInstance().setState(getTurretState());
        Shooter.getInstance().setState(getShooterState());
    }
}
