package com.bikmop.weather_checker.weather;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/** Geographical location with parameters for weather providers */
public class Location {
    // Ukrainian name of location
    String nameUa;
    // Russian name of location
    String nameRu;
    // Location strings for providers (part of URL which is different for each geographical location)
    String sinoptikUa;
    String sinoptikRu;
    String gismeteo;
    String wwo;

    public Location(String nameUa, String nameRu, String sinoptikUa, String sinoptikRu, String gismeteo, String wwo) {
        this.nameUa = nameUa;
        this.nameRu = nameRu;
        this.sinoptikUa = sinoptikUa;
        this.sinoptikRu = sinoptikRu;
        this.gismeteo = gismeteo;
        this.wwo = wwo;
    }

    // Getters
    public String getNameUa() {
        return nameUa;
    }

    public String getNameRu() {
        return nameRu;
    }

    public String getSinoptikUa() {
        return sinoptikUa;
    }

    public String getSinoptikRu() {
        return sinoptikRu;
    }

    public String getGismeteo() {
        return gismeteo;
    }

    public String getWwo() {
        return wwo;
    }

    /** Static method to get locations from file on disk
     * Example:
     * Київ                                     // nameUa
     * Киев                                     // nameRu
     * київ                                     // sinoptikUa
     * киев                                     // sinoptikRu
     * kyiv-4944                                // gismeteo
     * Kiev-weather/Kyyivska-Oblast/UA          // wwo
     */
    public static List<Location> getLocations(String fileName) {
        List<Location> locations = new ArrayList<>();
        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"))) {

            String nameUa = "";
            String nameRu = "";
            String sinoptikUa = "";
            String sinoptikRu = "";
            String gismeteo = "";
            int i = 0;

            // Skip first symbol
            if (reader.ready()) {
                reader.read();
            }
            while (reader.ready()) {
                String tmpStr = reader.readLine().trim();

                switch (i) {
                    case 0 :
                        nameUa = tmpStr;
                        break;
                    case 1 :
                        nameRu = tmpStr;
                        break;
                    case 2 :
                        sinoptikUa = tmpStr;
                        break;
                    case 3 :
                        sinoptikRu = tmpStr;
                        break;
                    case 4 :
                        gismeteo = tmpStr;
                        break;
                    case 5 :
                        locations.add(new Location(nameUa, nameRu, sinoptikUa, sinoptikRu, gismeteo, tmpStr));
                        i = -1;
                        nameUa = "";
                        nameRu = "";
                        sinoptikUa = "";
                        sinoptikRu = "";
                        gismeteo = "";
                        break;
                }

                i++;
            }
        } catch (IOException ignore) {
            // Ignore, because users do not need messages of the program.
        }
        return locations;
    }
}
