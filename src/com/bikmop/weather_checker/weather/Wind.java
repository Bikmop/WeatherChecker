package com.bikmop.weather_checker.weather;

/** Class to describe the wind parameters */
public class Wind {

    /** Wind power in m/s */
    private double power;
    /** Wind direction (N, S, E, W, NE, SE, etc.).   */
    private String direction;

    /** Wind direction and power are set in the constructor. */
    public Wind(double power, String direction) {
        this.power = power;
        this.direction = direction;
    }

    /** Getters */
    public Double getPower() {
        return power;
    }

    public String getDirection() {
        return direction;
    }
}
