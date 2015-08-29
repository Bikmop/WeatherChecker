package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.weather.Weather;

import java.util.Map;

/** Context class for Strategy pattern  */
public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public Map<Integer, Weather> getHourlyWeather(String place, int shiftDays, boolean isUa) {
        return strategy.getHourlyWeather(place, shiftDays, isUa);
    }
}
