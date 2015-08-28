package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.model.weather.Weather;

import java.util.Map;
import java.util.TreeMap;

public class WorldWeatherOnlineStrategy implements Strategy {
    /** Examples of references forecast.
     *  http://www.worldweatheronline.com/Chernihiv-weather/Chernihivska-Oblast/UA.aspx?day=20&tp=1
     *  http://www.worldweatheronline.com/Kiev-weather/Kyyivska-Oblast/UA.aspx?day=20&tp=1
     *  http://www.worldweatheronline.com/Klagenfurt-weather/Karnten/AT.aspx?day=20&tp=1
    */
    // URL constant
    private static final String URL_FORMAT = "http://www.worldweatheronline.com/%s.aspx?day=20&tp=1";


    @Override
    public Map<Integer, Weather> getHourlyWeather(String place, int shiftDays, boolean isUa) {
        Map<Integer, Weather> hourlyWeather = new TreeMap<>();

        try {

            Document document = getDocument(place);
            Elements elements = document.getElementsByAttributeValue("class", "weathertoday_div");

            // Находим номер нужного дня
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();

            Integer dayShift = null;
            if (shiftDays >= 0 && shiftDays < 7) {
                dayShift = shiftDays;
            }
/*
            for (int i = 0; i < 7; i++) {
                String currentDateStr = dateFormat.format(currentDate.getTime() + (1000 * 60 * 60 * 24 * i));
                if (currentDateStr.equals(date)) {
                    dayShift = i;
                    break;
                }
            }
*/

            if (dayShift != null) {
                Elements eachHourDataString = elements.get(dayShift).getElementsByTag("ul")/*.get(3).getElementsByTag("li")*/;

                for (int i = 3; i < eachHourDataString.size(); i++) {
                    Weather tmpWeather = new Weather(place, /*new SimpleDateFormat("yyyy-MM-dd").parse*/new Date(new Date().getTime() + (1000 * 60 * 60 * 24 * dayShift)));

                    tmpWeather.setUrl(String.format(URL_FORMAT, place));

                    Elements dataString = eachHourDataString.get(i).getElementsByTag("li");

                    tmpWeather.setTemperature(Integer.parseInt(dataString.get(2).text().split(" ")[0]));

                    tmpWeather.setTempFeel(Integer.parseInt(dataString.get(3).text().split(" ")[0]));

                    tmpWeather.setHumidity(Integer.parseInt(dataString.get(10).text().split("%")[0]));

                    tmpWeather.setPressure((int) (Integer.parseInt(dataString.get(11).text().split(" ")[0]) * 0.7500637554192));

                    tmpWeather.setPrecipitation(new Precipitation(dataString.get(4).text(),
                            Integer.parseInt(dataString.get(5).text().split("%")[0])));

                    String tmpWind;
                    if (isUa) {
                        tmpWind = dataString.get(8).text().replaceAll("S", "Пд").replaceAll("N", "Пн").replaceAll("W", "З").replaceAll("E", "С");
                    } else {
                        tmpWind = dataString.get(8).text().replaceAll("S", "Ю").replaceAll("N", "С").replaceAll("W", "З").replaceAll("E", "В");
                    }
                    tmpWeather.setWind(new Wind(Math.round(Double.parseDouble(dataString.get(7).text().split(" ")[0]) * 0.44704 * 10) / 10.0, tmpWind));

                    String tmpURL = dataString.get(1).getAllElements().get(1).attr("src");
                    String tmpPartsURL[] = tmpURL.split("/");
                    saveImage(tmpURL, "resources/wwo/" + tmpPartsURL[tmpPartsURL.length - 1]);
                    tmpWeather.setPictureWeather(new PictureWeather(dataString.get(1).getAllElements().get(1).attr("title"),
                            "resources/wwo/" + tmpPartsURL[tmpPartsURL.length - 1]));

                    hourlyWeather.put(Integer.parseInt(dataString.get(0).text().split(":")[0]), tmpWeather);
                }
            }

        } catch (IOException ignore) {}

        return hourlyWeather;
    }

    protected Document getDocument(String place) throws IOException {
        Connection connection = Jsoup.connect(String.format(URL_FORMAT, place));
        connection.userAgent("Mozilla/5.0 (Windows NT 6.1; rv:38.0) Gecko/20100101 Firefox/38.0");
        connection.referrer("https://www.google.com.ua/");
        return connection.get();
    }


}

