package com.bikmop.weather_checker.weather;

/** Class to describe the weather picture from the web-site */
public class PictureWeather {

    /** Additional description of the picture */
    private String weatherDescription;
    /** The path to image-file on disk  */
    private String weatherImagePath;

    /** Constructors to set weather picture parameters */
    public PictureWeather(String weatherDescription, String weatherImagePath) {
        this.weatherDescription = weatherDescription;
        this.weatherImagePath = weatherImagePath;
    }

    public PictureWeather(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    /** Getters */
    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getWeatherImage() {
        return weatherImagePath;
    }
}
