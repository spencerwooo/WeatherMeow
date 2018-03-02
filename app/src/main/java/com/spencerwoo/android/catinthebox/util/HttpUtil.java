package com.spencerwoo.android.catinthebox.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by spencerwoo on 02/03/2018.
 */

public class HttpUtil {

    // OkHttp 大法好啊
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
