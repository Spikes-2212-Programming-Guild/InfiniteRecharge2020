package com.spikes2212.frc2020.subsystems;

public enum IntakeFeederSync {
    CLOSE_INTAKE_OFF_FEEDER(Intake.IntakeState.CLOSE, Feeder.FeederState.OFF),
    CLOSE_INTAKE_ON_FEEDER(Intake.IntakeState.CLOSE, Feeder.FeederState.ON),
    CLOSE_INTAKE_OPEN_FEEDER(Intake.IntakeState.CLOSE, Feeder.FeederState.OPEN),
    OPEN_INTAKE_ON_FEEDER(Intake.IntakeState.OPEN, Feeder.FeederState.ON),
    OPEN_INTAKE_OPEN_FEEDER(Intake.IntakeState.OPEN, Feeder.FeederState.OPEN);

    private Intake.IntakeState intakeState;
    private Feeder.FeederState feederState;

    IntakeFeederSync(Intake.IntakeState intakeState, Feeder.FeederState feederState){
        this.intakeState=intakeState;
        this.feederState=feederState;
    }

    public Intake.IntakeState getIntakeState() {
        return intakeState;
    }

    public Feeder.FeederState getFeederState() {
        return feederState;
    }

    public void setState() {
        Intake.getInstance().setState(getIntakeState());
        Feeder.getInstance().setState(getFeederState());
    }
}