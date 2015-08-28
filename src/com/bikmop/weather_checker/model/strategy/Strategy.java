package com.bikmop.weather_checker.model.strategy;

import com.bikmop.weather_checker.model.weather.Weather;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/** Interface for Strategy pattern to get weather forecast from different sources */
public abstract class Strategy {

    abstract Map<Integer, Weather> getHourlyWeather(String place, int shiftDays, boolean isUa);

    /** Save weather image to file on disk if it does not exists.
     *  Improving speed of the image displaying since downloads from the Internet only new images.
     * */
    public static void saveImage(String imageUrl, String destinationFile) {

        File file = new File(destinationFile);
        if(!file.exists() || file.isDirectory()) {
            URL url = null;
            try {

                url = new URL(imageUrl);

                try (
                        InputStream is = url.openStream();
                        OutputStream os = new FileOutputStream(destinationFile);
                ){

                    byte[] b = new byte[2048];
                    int length;

                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                } catch (IOException ignore) {}

            } catch (MalformedURLException ignore) {}
        }

    }
}
