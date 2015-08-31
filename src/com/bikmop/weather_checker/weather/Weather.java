package com.bikmop.weather_checker.weather;

import java.util.Date;

/** Weather parameters */
public class Weather {

    /** Geographical location of the weather */
    private Location location;
    /** Date */
    private Date dateTime;
    /** Temperature in Celsius degrees */
    private int temperature;
    /** Temperature feelings */
    private int tempFeel;
    /** The weather picture from the web-site */
    private PictureWeather pictureWeather;
    /** Pressure in mmHg */
    private int pressure;
    /** Humidity in percents */
    private int humidity;
    /** Wind parameters */
    private Wind wind;
    /** Precipitations parameters */
    private Precipitation precipitation;
    /** URL-string with current weather forecast */
    private String url;


    /** Constructor */
    public Weather(Location location, Date dateTime) {
        this.location = location;
        this.dateTime = dateTime;
    }


    /** Getters and Setters */
    public Location getLocation() {
        return location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Integer getTempFeel() {
        return tempFeel;
    }

    public PictureWeather getPictureWeather() {
        return pictureWeather;
    }

    public Integer getPressure() {
        return pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Wind getWind() {
        return wind;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setTempFeel(int tempFeel) {
        this.tempFeel = tempFeel;
    }

    public void setPictureWeather(PictureWeather pictureWeather) {
        this.pictureWeather = pictureWeather;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
