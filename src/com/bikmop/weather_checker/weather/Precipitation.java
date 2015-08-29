package com.bikmop.weather_checker.weather;

/** Class to describe the parameters of precipitation */
public class Precipitation {

    /** Additional description of precipitation */
    private String description;
    /** Precipitation probability in percents */
    private Integer probability;

    /** Constructors to set precipitation parameters */
    public Precipitation(String description, Integer probability) {
        this.description = description;
        this.probability = probability;
    }

    public Precipitation(String description) {
        this(description, null);
    }

    public Precipitation(Integer probability) {
        this(null, probability);
    }

    public Precipitation() {
        this(null, null);
    }

    /** Getters */
    public String getDescription() {
        return description;
    }

    public Integer getProbability() {
        return probability;
    }
}
