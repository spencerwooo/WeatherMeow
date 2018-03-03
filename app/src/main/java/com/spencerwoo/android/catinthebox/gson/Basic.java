package com.spencerwoo.android.catinthebox.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by spencerwoo on 03/03/2018.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }

}
