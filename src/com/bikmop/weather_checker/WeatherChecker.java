package com.bikmop.weather_checker;

import com.bikmop.weather_checker.model.strategy.GismeteoUaStrategy;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.model.strategy.SinoptikUaStrategy;
import com.bikmop.weather_checker.model.strategy.WorldWeatherOnlineStrategy;
import com.bikmop.weather_checker.weather.Weather;

import java.util.Map;

public class WeatherChecker {
    public static void main(String[] args) {

        Map<Integer, Weather> gisForecast = new Provider(new GismeteoUaStrategy())
                .getHourlyWeather("chernihiv-4923", 0, false);
        Map<Integer, Weather> wwoForecast = new Provider(new WorldWeatherOnlineStrategy())
                .getHourlyWeather("Chernihiv-weather/Chernihivska-Oblast/UA", 0, false);
        Map<Integer, Weather> sinForecast = new Provider(new SinoptikUaStrategy())
                .getHourlyWeather("чернігів", 0, false);

        System.out.println();

    }
}
