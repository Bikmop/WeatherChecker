package com.bikmop.weather_checker.model;

import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.view.View;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Model-class of Model-View-Controller pattern */
public class Model {

    private View view;
    // List of weather forecast providers
    private Provider[] providers;

    public Model(View view, Provider[] providers) throws IllegalArgumentException
    {
        if (view == null || providers == null || providers.length == 0)
            throw new IllegalArgumentException();

        this.view = view;
        this.providers = providers;
    }

    public void selectWeatherParameters(Location location, int shiftDays, boolean isUa) {
        List<Map<Integer, Weather>> forecasts = new ArrayList<>();
        for (Provider provider : providers) {

        }

//        view.update();
    }


}
