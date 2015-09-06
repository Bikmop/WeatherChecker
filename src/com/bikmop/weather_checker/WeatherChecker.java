package com.bikmop.weather_checker;

import com.bikmop.weather_checker.model.Model;
import com.bikmop.weather_checker.view.SwingFrameView;
import com.bikmop.weather_checker.view.View;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.model.strategy.GismeteoUaStrategy;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.model.strategy.SinoptikUaStrategy;
import com.bikmop.weather_checker.model.strategy.WorldWeatherOnlineStrategy;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/** WeatherChecker
 * Program to get weather-forecasts from different web-site providers and show it in one JFrame.
 * Using:
 *      org.jsoup.Jsoup - to parse html-pages of weather forecasts
 *      java.util.concurrent - ThreadPool with Callable-tasks to get forecasts at the same time
 *      javax.swing, java.awt - GUI
 *      Model-View-Controller - program based pattern
 *      Strategy pattern - weather-provider selection
 *
 * Designed to be easily extended with new weather forecast providers.
 *
 * Current capabilities:
 * - Three different providers of weather forecast (two are the most popular in Ukraine and one is international site):
 *      http://www.gismeteo.ua
 *      https://sinoptik.ua
 *      http://www.worldweatheronline.com
 *
 * - One day (each three hours) of forecast on the frame. From today to +6 days.
 * - Ukrainian and Russian languages of interface. Fast switching, no need to restart the program.
 * - The most part of interface data is stored in *.data and java-properties files.
 * - User can edit data-files to add new or remove geographical locations, change the names of the fields in the GUI,
 *   change providers order, etc.
 */
public class WeatherChecker {
    public static boolean isUa = true;
    public static List<Provider> providers = new ArrayList<>();
    public static List<Location> locations;

    /** Main */
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
