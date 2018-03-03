package com.spencerwoo.android.catinthebox.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by spencerwoo on 03/03/2018.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forcast> forecastList;

}
