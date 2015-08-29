package com.bikmop.weather_checker.model;

/** Geographical location with parameters for weather providers */
public class Location {
    // Ukrainian name of location
    String nameUa;
    // Russian name of location
    String nameRu;
    // Location strings for providers
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
