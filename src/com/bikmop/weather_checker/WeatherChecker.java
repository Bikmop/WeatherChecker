package com.bikmop.weather_checker;

import com.bikmop.weather_checker.model.Model;
import com.bikmop.weather_checker.view.View;
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

/*        Map<Integer, Weather> gisForecast = new Provider(new GismeteoUaStrategy())
                .getHourlyWeather(locations.get(2), 2, true);
        Map<Integer, Weather> wwoForecast = new Provider(new WorldWeatherOnlineStrategy())
                .getHourlyWeather(locations.get(2), 2, true);
        Map<Integer, Weather> sinForecast = new Provider(new SinoptikUaStrategy())
                .getHourlyWeather(locations.get(2), 2, true);*/

        Provider[] providers = new Provider[3];
        providers[0] = new Provider(new SinoptikUaStrategy());
        providers[1] = new Provider(new GismeteoUaStrategy());
        providers[2] = new Provider(new WorldWeatherOnlineStrategy());

//        Model model = new Model(new View(), providers);
//        model.selectWeatherParameters(locations.get(1), 6, true);

        System.out.println();

    }
}
