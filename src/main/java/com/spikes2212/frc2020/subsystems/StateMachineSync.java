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
    private Shooter.ShooterState shooter;
    private Turret.TurretState turret;
    StateMachineSync(IntakeFeederSync intakeFeederSync, Shooter.ShooterState shooter, Turret.TurretState turret){
        this.intakeFeederSync = intakeFeederSync;
        this.shooter=shooter;
        this.turret=turret;
    }

    public IntakeFeederSync getIntakeFeederSync() {
        return intakeFeederSync;
    }

    public Shooter.ShooterState getShooter() {
        return shooter;
    }

    public Turret.TurretState getTurret() {
        return turret;
    }

    public static boolean validate(Intake.IntakeState intakeState, Feeder.FeederState feederState, Shooter.ShooterState shooterState, Turret.TurretState turretState) {
        return (intakeState != Intake.IntakeState.OPEN || feederState != Feeder.FeederState.OFF) &&
                (shooterState == Shooter.ShooterState.OFF || turretState == Turret.TurretState.OFF);
    }
}
