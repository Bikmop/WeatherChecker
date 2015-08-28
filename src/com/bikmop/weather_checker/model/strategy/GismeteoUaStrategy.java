package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.model.weather.Weather;

import java.util.Map;

public class GismeteoUaStrategy implements Strategy {

    @Override
    public Map<Integer, Weather> getHourlyWeather(String place, int shiftDays, boolean isUa) {
        return null;
    }
}
