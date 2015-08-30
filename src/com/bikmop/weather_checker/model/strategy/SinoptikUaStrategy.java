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
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class SinoptikUaStrategy extends Strategy {
    /** Examples of references forecast (contains cyrillic symbols).
     *  https://ua.sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BA%D0%B8%D1%97%D0%B2/2015-08-30
     *  https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BA%D0%B8%D0%B5%D0%B2/2015-08-31
     */
    // URL constants
    private static final String URL_FORMAT = "https://sinoptik.ua/%s/%s";
    private static final String URL_FORMAT_UA = "https://ua.sinoptik.ua/%s/%s";


    @Override
    /** Get hourly weather forecast using Jsoup */
    public Map<Integer, Weather> getHourlyWeather(Location location, int shiftDays, boolean isUa) {

        Map<Integer, Weather> hourlyWeather = new TreeMap<>();

        try {

            // Get necessary part of URL (location string). Example: %D0%BA%D0%B8%D0%B5%D0%B2 - cyrillic symbols
            String place;
            if (isUa) {
                place = location.getSinoptikUa();
            } else {
                place = location.getSinoptikRu();
            }

            // Convert shiftDays to necessary string
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String dateStr = dateFormat.format(new Date(today.getTime() + (1000 * 60 * 60 * 24 * shiftDays)));

            // Get HTML and fill resultant map with parsed data
            Document document = getDocument(place, dateStr, isUa);
            Elements element = document.getElementsByAttributeValue("class", "weatherDetails");

            boolean isData = true;
            if (element.size() == 0)
                isData = false;

            int i = 0;
            while (isData) {
                i++;
                Elements tmpElements = element.get(0).getElementsByAttributeValueStarting("class", "p" + i);
                if (tmpElements.size() != 0) {
                    Weather tmpWeather = new Weather(location, new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * shiftDays)));
                    if (isUa) {
                        tmpWeather.setUrl(String.format(URL_FORMAT_UA, "погода-" + place, dateStr));
                    } else {
                        tmpWeather.setUrl(String.format(URL_FORMAT, "погода-" + place, dateStr));
                    }

                    String tmpURL = tmpElements.get(1).getElementsByAttributeValueStarting("class", "weatherImg").get(0).attr("src");
                    String tmpPartsURL[] = tmpURL.split("/");
                    // Save weather image if necessary
                    saveImage("http:" + tmpURL, "resources/sinoptik/" + tmpPartsURL[tmpPartsURL.length - 1]);
                    tmpWeather.setPictureWeather(new PictureWeather(tmpElements.get(1).getElementsByAttributeValueStarting("class", "weatherIco").get(0).attr("title"),
                            "resources/sinoptik/" + tmpPartsURL[tmpPartsURL.length - 1]));

                    String tmpStr = tmpElements.get(2).getElementsByAttributeValueStarting("class", "p" + i).text();
                    tmpWeather.setTemperature(Integer.parseInt(tmpStr.substring(0, tmpStr.length() - 1)));

                    tmpStr = tmpElements.get(3).getElementsByAttributeValueStarting("class", "p" + i).text();
                    tmpWeather.setTempFeel(Integer.parseInt(tmpStr.substring(0, tmpStr.length() - 1)));

                    tmpWeather.setPressure(Integer.parseInt(tmpElements.get(4).getElementsByAttributeValueStarting("class", "p" + i).text()));

                    tmpWeather.setHumidity(Integer.parseInt(tmpElements.get(5).getElementsByAttributeValueStarting("class", "p" + i).text()));

                    String[] windStr = tmpElements.get(6).getElementsByAttributeValueStarting("class", "p" + i).
                            get(0).getElementsByAttribute("data-tooltip").attr("data-tooltip").split(",");
                    tmpWeather.setWind(new Wind(Double.parseDouble(tmpElements.get(6).getElementsByAttributeValueStarting("class", "p" + i).text()), windStr[0]));

                    String tmpPrecipitation = tmpElements.get(7).getElementsByAttributeValueStarting("class", "p" + i).text();
                    // For past, integer data of precipitation probability changed to string "-"
                    // -1000 means "-" for showing results
                    if (!tmpPrecipitation.equals("-")) {
                        tmpWeather.setPrecipitation(new Precipitation(Integer.parseInt(tmpPrecipitation)));
                    } else {
                        tmpWeather.setPrecipitation(new Precipitation(-1000));
                    }

                    String[] timeStr = tmpElements.get(0).getElementsByAttributeValueStarting("class", "p" + i).text().split(":");

                    hourlyWeather.put(Integer.parseInt(timeStr[0]), tmpWeather);
                } else {
                    isData = false;
                }
            }

        } catch (IOException ignore) {
            // TODO - add to log
        }

        return hourlyWeather;
    }

    /** Connect to weather page and get HTML document */
    protected Document getDocument(String place, String yyyyMMDD, boolean isUa) throws IOException {
        Connection connection;
        if (isUa) {
            connection = Jsoup.connect(String.format(URL_FORMAT_UA, URLEncoder.encode("погода-" + place, "UTF-8"), yyyyMMDD));
        } else {
            connection = Jsoup.connect(String.format(URL_FORMAT, URLEncoder.encode("погода-" + place, "UTF-8"), yyyyMMDD));
        }
        connection.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
        connection.referrer("https://www.google.com.ua/");
        return connection.get();
    }
}
