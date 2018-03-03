package com.spencerwoo.android.catinthebox.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by spencerwoo on 03/03/2018.
 */

public class Forcast {

    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;
        public String min;

    }

    public class More {

        @SerializedName("txt_d")
        public String info;

    }

}
