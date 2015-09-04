package com.bikmop.weather_checker;

import com.bikmop.weather_checker.model.Model;
import com.bikmop.weather_checker.view.SwingFrameView;
import com.bikmop.weather_checker.view.View;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.model.strategy.GismeteoUaStrategy;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.model.strategy.SinoptikUaStrategy;
import com.bikmop.weather_checker.model.strategy.WorldWeatherOnlineStrategy;
import com.bikmop.weather_checker.weather.Weather;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherChecker {
    public static boolean isUa = true;
    public static List<Provider> providers = new ArrayList<>();
    public static List<Location> locations;

    public static void main(String[] args) {

        // Initialize fields of class
        init();


/*        Map<Integer, Weather> gisForecast = new Provider(new GismeteoUaStrategy())
                .getHourlyWeather(locations.get(2), 2, true);
        Map<Integer, Weather> wwoForecast = new Provider(new WorldWeatherOnlineStrategy())
                .getHourlyWeather(locations.get(2), 2, true);
        Map<Integer, Weather> sinForecast = new Provider(new SinoptikUaStrategy())
                .getHourlyWeather(locations.get(2), 2, true);*/
//        Provider[] providers = new Provider[3];
//        providers[0] = new Provider(new SinoptikUaStrategy());
//        providers[1] = new Provider(new GismeteoUaStrategy());
//        providers[2] = new Provider(new WorldWeatherOnlineStrategy());


        View frame = new SwingFrameView();

        frame.init(providers, locations, isUa);

        Model model = new Model(new SwingFrameView(), providers);
        model.refreshWeatherParameters(locations.get(0), 0, isUa);
        frame.setController(new Controller(model));

        System.out.println();

    }

    /** Get default language, list of weather providers and list of locations from
     *  "resources/init.data" and "resources/locations.data" files.
     *  And put this data to corresponding fields.
     */
    public static void init() {

        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/init.data"), "UTF8"))) {
            String tmpStr;
            if (reader.ready()) {
                tmpStr = reader.readLine().trim().toLowerCase();
                if ("ru".equals(tmpStr)) {
                    isUa = false;
                }
            }

            while (reader.ready()) {
                tmpStr = reader.readLine().trim().toLowerCase();
                switch (tmpStr) {
                    case "sinoptik" :
                        providers.add(new Provider(new SinoptikUaStrategy()));
                        break;
                    case "gismeteo" :
                        providers.add(new Provider(new GismeteoUaStrategy()));
                        break;
                    case "wwo" :
                        providers.add(new Provider(new WorldWeatherOnlineStrategy()));
                        break;
                }
            }

        } catch (IOException e) {
            // TODO - add to log
        }

        locations = Location.getLocations("resources/locations.data");
    }
}
