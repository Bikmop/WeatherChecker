package com.bikmop.weather_checker;

import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.model.strategy.GismeteoUaStrategy;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.model.strategy.SinoptikUaStrategy;
import com.bikmop.weather_checker.model.strategy.WorldWeatherOnlineStrategy;
import com.bikmop.weather_checker.weather.Weather;

import java.util.Map;

public class WeatherChecker {
    public static void main(String[] args) {


        Location chernigiv = new Location("Чернігів", "Чернигов", "чернігів", "чернигов", "chernihiv-4923",
                "Chernihiv-weather/Chernihivska-Oblast/UA");
        Map<Integer, Weather> gisForecast = new Provider(new GismeteoUaStrategy())
                .getHourlyWeather(chernigiv, 2, true);
        Map<Integer, Weather> wwoForecast = new Provider(new WorldWeatherOnlineStrategy())
                .getHourlyWeather(chernigiv, 2, true);
        Map<Integer, Weather> sinForecast = new Provider(new SinoptikUaStrategy())
                .getHourlyWeather(chernigiv, 2, true);

        System.out.println();

    }
}
