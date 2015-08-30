package com.bikmop.weather_checker;

import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.model.strategy.GismeteoUaStrategy;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.model.strategy.SinoptikUaStrategy;
import com.bikmop.weather_checker.model.strategy.WorldWeatherOnlineStrategy;
import com.bikmop.weather_checker.weather.Weather;

import java.util.List;
import java.util.Map;

public class WeatherChecker {
    public static void main(String[] args) {

        List<Location> locations = Location.getLocations("resources/locations.data");

        Map<Integer, Weather> gisForecast = new Provider(new GismeteoUaStrategy())
                .getHourlyWeather(locations.get(0), 2, true);
        Map<Integer, Weather> wwoForecast = new Provider(new WorldWeatherOnlineStrategy())
                .getHourlyWeather(locations.get(0), 2, true);
        Map<Integer, Weather> sinForecast = new Provider(new SinoptikUaStrategy())
                .getHourlyWeather(locations.get(0), 2, false);

        System.out.println();

    }
}
