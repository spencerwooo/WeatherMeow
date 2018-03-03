package com.spencerwoo.android.catinthebox.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by spencerwoo on 03/03/2018.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }
}
