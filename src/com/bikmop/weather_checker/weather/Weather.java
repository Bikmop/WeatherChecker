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
    /** String path to icon of weather provider */
    private String iconProviderFilePath;


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

    public String getIconProviderFilePath() {
        return iconProviderFilePath;
    }

    public void setIconProviderFilePath(String iconProviderFilePath) {
        this.iconProviderFilePath = iconProviderFilePath;
    }

    /** Standard generated equals() and hashCode() */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;

        Weather weather = (Weather) o;

        if (temperature != weather.temperature) return false;
        if (tempFeel != weather.tempFeel) return false;
        if (pressure != weather.pressure) return false;
        if (humidity != weather.humidity) return false;
        if (location != null ? !location.equals(weather.location) : weather.location != null) return false;
        if (dateTime != null ? !dateTime.equals(weather.dateTime) : weather.dateTime != null) return false;
        if (pictureWeather != null ? !pictureWeather.equals(weather.pictureWeather) : weather.pictureWeather != null)
            return false;
        if (wind != null ? !wind.equals(weather.wind) : weather.wind != null) return false;
        if (precipitation != null ? !precipitation.equals(weather.precipitation) : weather.precipitation != null)
            return false;
        if (url != null ? !url.equals(weather.url) : weather.url != null) return false;
        return !(iconProviderFilePath != null ? !iconProviderFilePath.equals(weather.iconProviderFilePath) : weather.iconProviderFilePath != null);

    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + temperature;
        result = 31 * result + tempFeel;
        result = 31 * result + (pictureWeather != null ? pictureWeather.hashCode() : 0);
        result = 31 * result + pressure;
        result = 31 * result + humidity;
        result = 31 * result + (wind != null ? wind.hashCode() : 0);
        result = 31 * result + (precipitation != null ? precipitation.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (iconProviderFilePath != null ? iconProviderFilePath.hashCode() : 0);
        return result;
    }
}
