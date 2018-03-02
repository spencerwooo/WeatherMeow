package com.spencerwoo.android.catinthebox.db;

import org.litepal.crud.DataSupport;

/**
 * Created by spencerwoo on 02/03/2018.
 */

public class Country extends DataSupport {

    private int id;

    private String countryName;

    private String weatherID;

    private int cityID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(String weatherID) {
        this.weatherID = weatherID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
