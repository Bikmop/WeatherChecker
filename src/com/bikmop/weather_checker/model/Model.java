package com.bikmop.weather_checker.model;

import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.view.View;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/** Model-class of Model-View-Controller pattern */
public class Model {

    private View view;
    // List of weather forecast providers
    private List<Provider> providers;

    public Model(View view, List<Provider> providers) throws IllegalArgumentException
    {
        if (view == null || providers == null || providers.size() == 0)
            throw new IllegalArgumentException();

        this.view = view;
        this.providers = providers;
    }


    /** Method to refresh View with new data of weather forecasts
     *  Using multithreading - thread pool with own thread for each weather provider
     *  to improve refreshing speed.
     */
    public void refreshWeatherParameters(Location location, int shiftDays, boolean isUa) {
        List<Map<Integer, Weather>> forecasts = new ArrayList<>();

        // Create ThreadPool
        ExecutorService executor = Executors.newFixedThreadPool(providers.size());
        // List for results
        List<Future<Map<Integer, Weather>>> futureList = new ArrayList<>();

        // Adding tasks to get weather forecasts
        for (Provider provider : providers) {
            Future<Map<Integer, Weather>> future = executor.submit(new Callable<Map<Integer, Weather>>() {
                @Override
                public Map<Integer, Weather> call() throws Exception {
                    return provider.getHourlyWeather(location, shiftDays, isUa);
                }
            });
            futureList.add(future);
        }

        // Waiting till all tasks will be done
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < providers.size(); i++) {
                if (!futureList.get(i).isDone()) {
                    flag = true;
                    break;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO - add to log
            }
        }

        // Getting results
        for (int i = 0; i < providers.size(); i++) {
            try {
                forecasts.add(futureList.get(i).get());
            } catch (InterruptedException | ExecutionException e) {
                // TODO - add to log
            }
        }

        view.update(forecasts);
    }


}
