package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.weather.PictureWeather;
import com.bikmop.weather_checker.weather.Weather;
import com.bikmop.weather_checker.weather.Wind;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class GismeteoUaStrategy extends Strategy {
    /** Examples of references forecast.
     *  http://www.gismeteo.ua/weather-chernihiv-4923/hourly/
     *  http://www.gismeteo.ua/weather-chernihiv-4923/3-5-days-hourly/
     *  http://www.gismeteo.ua/weather-chernihiv-4923/5-7-days-hourly/
     *  http://www.gismeteo.ua/weather-chernihiv-4923/7-9-days-hourly/
     *  http://www.gismeteo.ua/ua/weather-chernihiv-4923/hourly/
     *  http://www.gismeteo.ua/ua/weather-lutsk-4928/hourly/
     */
    // URL constants
    private static final String URL_FORMAT = "http://www.gismeteo.ua/weather-%s/%shourly/";
    private static final String URL_FORMAT_UA = "http://www.gismeteo.ua/ua/weather-%s/%shourly/";


    @Override
    /** Get hourly weather forecast using Jsoup */
    public Map<Integer, Weather> getHourlyWeather(String place, int shiftDays, boolean isUa) {

        Map<Integer, Weather> hourlyWeather = new TreeMap<>();

        try {

            // Convert shiftDays to necessary string
            String shiftStr = null;
            switch (shiftDays) {
                case 0:
                case 1:
                case 2:
                    shiftStr = "";
                    break;

                case 3:
                case 4:
                    shiftStr = "3-5-days-";
                    break;

                case 5:
                case 6:
                    shiftStr = "5-7-days-";
                    break;

                case 7:
                case 8:
                    shiftStr = "7-9-days-";
                    break;
            }

            // Get HTML and fill resultant map with parsed data
            Document document = getDocument(place, shiftStr, isUa);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            String dateStr = dateFormat.format(new Date(today.getTime() + (1000 * 60 * 60 * 24 * shiftDays)));

            Elements elements = document.getElementsByAttributeValueStarting("id", "wrow-" + dateStr);

            for (int i = 0; i < elements.size(); i++) {
                Weather tmpWeather = new Weather(place, new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * shiftDays)));

                if (isUa) {
                    tmpWeather.setUrl(String.format(URL_FORMAT_UA, place, shiftStr));
                } else {
                    tmpWeather.setUrl(String.format(URL_FORMAT, place, shiftStr));
                }

                String tmpURL = elements.get(i).getElementsByAttributeValue("class", "clicon").
                        get(0).getElementsByTag("img").get(0).attr("src");
                String tmpPartsURL[] = tmpURL.split("/");
                // Save weather image if necessary
                saveImage("http:" + tmpURL, "resources/gismeteo/" + tmpPartsURL[tmpPartsURL.length - 1]);
                tmpWeather.setPictureWeather(new PictureWeather(elements.get(i).getElementsByAttributeValue("class", "cltext").text(),
                        "resources/gismeteo/" + tmpPartsURL[tmpPartsURL.length - 1]));

                Elements temperatures = elements.get(i).getElementsByAttributeValue("class", "value m_temp c");
                tmpWeather.setTemperature(Integer.parseInt(temperatures.get(0).getElementsByAttributeValue("class", "value m_temp c").text()));
                tmpWeather.setTempFeel(Integer.parseInt(temperatures.get(1).getElementsByAttributeValue("class", "value m_temp c").text()));

                tmpWeather.setPressure(Integer.parseInt(elements.get(i).getElementsByAttributeValue("class", "value m_press torr").text()));

                tmpWeather.setWind(new Wind(Double.parseDouble(elements.get(i).getElementsByAttributeValue("class", "value m_wind ms").text()),
                        elements.get(i).getElementsByAttributeValueStarting("class", "wicon").attr("title")));

                Elements tdTags = elements.get(i).getElementsByTag("td");
                tmpWeather.setHumidity(Integer.parseInt(tdTags.get(5).text()));

                String[] timeStr = elements.get(i).getElementsByAttributeValueStarting("class", "wrow").attr("id").split("-");

                hourlyWeather.put(Integer.parseInt(timeStr[timeStr.length - 1]), tmpWeather);

            }

        } catch (IOException ignore) {
            // TODO - add to log
        }

        return hourlyWeather;
    }

    /** Connect to weather page and get HTML document */
    protected Document getDocument(String place, String shiftStr, boolean isUa) throws IOException {
        Connection connection;
        if (isUa) {
            connection = Jsoup.connect(String.format(URL_FORMAT_UA, place, shiftStr));
        } else {
            connection = Jsoup.connect(String.format(URL_FORMAT, place, shiftStr));
        }
        connection.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
        connection.referrer("https://www.google.com.ua/");
        return connection.get();
    }

}
