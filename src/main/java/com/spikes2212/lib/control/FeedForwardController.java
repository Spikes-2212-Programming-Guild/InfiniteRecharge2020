package com.spikes2212.lib.control;

/**
 * This class represents a FeedForwardController.
 */
public class FeedForwardController {

    /**
     * The velocity constant
     */
    private double kV;

    /**
     * The acceleration constant
     */
    private double kA;

    /**
     * The previous target.
     * Used for the derivative.
     */
    private double previousTarget;

    /**
     * The calling period for the calculate function.
     */
    private double period;

    public FeedForwardController(double kV, double kA, double period) {
        this(kV, kA, period, 0);
    }

    public FeedForwardController(double kV, double kA, double period, double initialTarget) {
        this.kV = kV;
        this.kA = kA;
        this.period = period;
        this.previousTarget = initialTarget;
    }

    public double getkV() {
        return kV;
    }

    public void setkV(double kV) {
        this.kV = kV;
    }

    public double getkA() {
        return kA;
    }

    public void setkA(double kA) {
        this.kA = kA;
    }

    public double getPeriod() {
        return period;
    }

    public void reset() {
        this.previousTarget = 0;
    }

    /**
     * Calculates the desired output using a simple feed forward method.
     * This method should be called with the period given in the constructor.
     * @param setpoint the target velocity
     * @return the desired output
     */
    public double calculate(double setpoint) {
        double targetDerivative = (setpoint - previousTarget) / period;
        previousTarget = setpoint;
        return kV * setpoint + kA * targetDerivative;
    }
}
