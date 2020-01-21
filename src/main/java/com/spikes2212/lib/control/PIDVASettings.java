package com.spikes2212.lib.control;

import java.util.function.Supplier;

/**
 * {@inheritDoc}
 * This class also has a velocity and acceleration constants, used for PIDVA control.
 */
public class PIDVASettings extends PIDSettings {
    /**
     * The velocity constant.
     */
    private Supplier<Double> kV;
    /**
     * The acceleration constant.
     */
    private Supplier<Double> kA;

    public PIDVASettings(double kP, double tolerance, double waitTime, Supplier<Double> kV, Supplier<Double> kA) {
        super(kP, tolerance, waitTime);
        this.kV = kV;
        this.kA = kA;
    }

    public PIDVASettings(double kP, double kI, double kD, double tolerance, double waitTime, Supplier<Double> kV, Supplier<Double> kA) {
        super(kP, kI, kD, tolerance, waitTime);
        this.kV = kV;
        this.kA = kA;
    }

    public PIDVASettings(Supplier<Double> kP, Supplier<Double> tolerance, Supplier<Double> waitTime, Supplier<Double> kV, Supplier<Double> kA) {
        super(kP, tolerance, waitTime);
        this.kV = kV;
        this.kA = kA;
    }

    public PIDVASettings(Supplier<Double> kP, Supplier<Double> kI, Supplier<Double> kD, Supplier<Double> tolerance, Supplier<Double> waitTime, Supplier<Double> kV, Supplier<Double> kA) {
        super(kP, kI, kD, tolerance, waitTime);
        this.kV = kV;
        this.kA = kA;
    }

    public PIDVASettings(double kP, double tolerance, double waitTime, double kV, double kA) {
        super(kP, tolerance, waitTime);
        this.kV = () -> kV;
        this.kA = () -> kA;
    }

    public PIDVASettings(double kP, double kI, double kD, double tolerance, double waitTime, double kV, double kA) {
        super(kP, kI, kD, tolerance, waitTime);
        this.kV = () -> kV;
        this.kA = () -> kA;
    }

    public PIDVASettings(Supplier<Double> kP, Supplier<Double> tolerance, Supplier<Double> waitTime, double kV, double kA) {
        super(kP, tolerance, waitTime);
        this.kV = () -> kV;
        this.kA = () -> kA;
    }

    public PIDVASettings(Supplier<Double> kP, Supplier<Double> kI, Supplier<Double> kD, Supplier<Double> tolerance,
                         Supplier<Double> waitTime, double kV, double kA) {
        super(kP, kI, kD, tolerance, waitTime);
        this.kV = () -> kV;
        this.kA = () -> kA;
    }

    public double getkV() {
        return kV.get();
    }

    public void setkV(Supplier<Double> kV) {
        this.kV = kV;
    }

    public double getkA() {
        return kA.get();
    }

    public void setkA(Supplier<Double> kA) {
        this.kA = kA;
    }
}
