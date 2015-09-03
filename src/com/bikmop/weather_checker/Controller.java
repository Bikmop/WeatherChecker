package com.bikmop.weather_checker;

import com.bikmop.weather_checker.model.Model;
import com.bikmop.weather_checker.weather.Location;

/** Controller-class of Model-View-Controller pattern */
public class Controller {
    private Model model;

    public Controller(Model model) throws IllegalArgumentException
    {
        if (model == null)
            throw new IllegalArgumentException();

        this.model = model;
    }

    public void onParametersChange(Location location, int shiftDays, boolean isUa) {
        model.refreshWeatherParameters(location, shiftDays, isUa);
    }
}
