package com.spencerwoo.android.catinthebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** START - this is the purpose of this Activity */
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finish();
        /** END - everything more than this is time consuming */
//        setContentView(R.layout.activity_splash);
    }

}
