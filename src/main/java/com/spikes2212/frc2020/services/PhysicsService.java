package com.spikes2212.frc2020.services;

public class PhysicsService {

    private static PhysicsService instance;

    public static PhysicsService getInstance() {
        if (instance == null) instance = new PhysicsService();
        return instance;
    }


    public double calculateDistanceByHeight(double height) {
        return 0;
    }

    public double calculateSpeedForDistance(double distance) {
        return 0;
    }
}
