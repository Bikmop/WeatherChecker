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

        // Initialize fields of class (get from *.data files)
        init();

        View frame = new SwingFrameView();
        frame.init(providers, locations, isUa);
        Model model = new Model(frame, providers);
        frame.setController(new Controller(model));

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

        } catch (IOException ignore) {
            // Ignore, because users do not need messages of the program.
        }

        locations = Location.getLocations("resources/locations.data");
    }
}
