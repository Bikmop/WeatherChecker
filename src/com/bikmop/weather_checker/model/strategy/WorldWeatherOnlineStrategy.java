package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.weather.Location;
import com.bikmop.weather_checker.weather.PictureWeather;
import com.bikmop.weather_checker.weather.Precipitation;
import com.bikmop.weather_checker.weather.Weather;
import com.bikmop.weather_checker.weather.Wind;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/** Strategy implementation for provider: http://www.worldweatheronline.com */
public class WorldWeatherOnlineStrategy extends Strategy {
    /** Examples of references forecast.
     *  http://www.worldweatheronline.com/Chernihiv-weather/Chernihivska-Oblast/UA.aspx?day=20&tp=1
     *  http://www.worldweatheronline.com/Kiev-weather/Kyyivska-Oblast/UA.aspx?day=20&tp=1
     *  http://www.worldweatheronline.com/Klagenfurt-weather/Karnten/AT.aspx?day=20&tp=1
     */
    // URL constant
    private static final String URL_FORMAT = "http://www.worldweatheronline.com/%s.aspx?day=20&tp=1";

    // Directory with resources for current strategy
    private static final String RESOURCES_DIRECTORY = "resources/wwo/";

    @Override
    public String getDirectoryPath() {
        return RESOURCES_DIRECTORY;
    }

    @Override
    public String getDefaultLink() {
        return "http://www.worldweatheronline.com";
    }

    @Override
    /** Get hourly weather forecast using Jsoup */
    public Map<Integer, Weather> getHourlyWeather(Location location, int shiftDays, boolean isUa) {

        Map<Integer, Weather> hourlyWeather = new TreeMap<>();

        if (shiftDays >= 0 && shiftDays < 7) {
            try {

                // Get necessary part of URL (location string). Example: Chernihiv-weather/Chernihivska-Oblast/UA
                String place = location.getWwo();

                // Get HTML and fill resultant map with parsed data
                Document document = getDocument(place);
                Elements elements = document.getElementsByAttributeValue("class", "weathertoday_div");
                Elements eachHourDataString = elements.get(shiftDays).getElementsByTag("ul");

                for (int i = 3; i < eachHourDataString.size(); i++) {
                    Weather tmpWeather = new Weather(location, new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * shiftDays)));

                    tmpWeather.setUrl(String.format(URL_FORMAT, place));

                    Elements dataString = eachHourDataString.get(i).getElementsByTag("li");

                    tmpWeather.setTemperature(Integer.parseInt(dataString.get(2).text().split(" ")[0]));

                    tmpWeather.setTempFeel(Integer.parseInt(dataString.get(3).text().split(" ")[0]));

                    tmpWeather.setHumidity(Integer.parseInt(dataString.get(10).text().split("%")[0]));

                    // Convert pressure to mmHg
                    tmpWeather.setPressure((int) (Integer.parseInt(dataString.get(11).text().split(" ")[0]) * 0.7500637554192));

                    tmpWeather.setPrecipitation(new Precipitation(dataString.get(4).text(),
                            Integer.parseInt(dataString.get(5).text().split("%")[0])));

                    String tmpWind;
                    // Translate the wind direction to ukrainian or russian
                    if (isUa) {
                        tmpWind = dataString.get(8).text().replaceAll("S", "Пд").replaceAll("N", "Пн").replaceAll("W", "З").replaceAll("E", "С");
                    } else {
                        tmpWind = dataString.get(8).text().replaceAll("S", "Ю").replaceAll("N", "С").replaceAll("W", "З").replaceAll("E", "В");
                    }
                    tmpWeather.setWind(new Wind(Math.round(Double.parseDouble(dataString.get(7).text().split(" ")[0]) * 0.44704 * 10) / 10.0, tmpWind));

                    String tmpURL = dataString.get(1).getAllElements().get(1).attr("src");
                    String tmpPartsURL[] = tmpURL.split("/");
                    // Save weather image if necessary
                    saveImage(tmpURL, RESOURCES_DIRECTORY + tmpPartsURL[tmpPartsURL.length - 1]);
                    tmpWeather.setPictureWeather(new PictureWeather(dataString.get(1).getAllElements().get(1).attr("title"),
                            RESOURCES_DIRECTORY + tmpPartsURL[tmpPartsURL.length - 1]));

                    hourlyWeather.put(Integer.parseInt(dataString.get(0).text().split(":")[0]), tmpWeather);
                }


            } catch (IOException ignore) {
                // Ignore, because users do not need messages of the program.
            }

        }

        return hourlyWeather;
    }

    /** Connect to weather page and get HTML document */
    protected Document getDocument(String place) throws IOException {
        Connection connection = Jsoup.connect(String.format(URL_FORMAT, place));
        connection.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
        connection.referrer("https://www.google.com.ua/");
        return connection.get();
    }
}

