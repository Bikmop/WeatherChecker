package com.bikmop.weather_checker.weather;

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
}
