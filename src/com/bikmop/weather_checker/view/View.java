package com.bikmop.weather_checker.view;

import com.bikmop.weather_checker.Controller;
import com.bikmop.weather_checker.model.strategy.Provider;
import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.Weather;

import java.util.List;
import java.util.Map;

/** View-interface of Model-View-Controller pattern */
public interface View {
    void update(List<Map<Integer, Weather>> forecasts);
    void setController(Controller controller);
    void init(List<Provider> providers, List<Location> locations, boolean isUa);
}
