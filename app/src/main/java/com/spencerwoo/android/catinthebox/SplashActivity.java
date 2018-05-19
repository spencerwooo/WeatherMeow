package com.spencerwoo.android.catinthebox;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Random;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ArrayList<String> strings = new ArrayList<>();

        strings.add("树阴满地日当午 梦觉流莺时一声");
        strings.add("明月别枝惊鹊 清风半夜鸣蝉");
        strings.add("纸屏石枕竹方床 手倦抛书午梦长");
        strings.add("竹深树密虫鸣处 时有微凉不是风");
        strings.add("柳庭风静人眠昼 昼眠人静风庭柳");
        strings.add("声喧乱石中 色静深松里");
        strings.add("泉眼无声惜细流 树阴照水爱晴柔");
        strings.add("醉卧不知白日暮 有时空望孤云高");
        Random r = new Random();

        String string = strings.get(r.nextInt(strings.size()));
        TextView splashText = findViewById(R.id.splash_text);
        splashText.setText(string);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/tc-zh.ttf");
        splashText.setTypeface(tf);

        YoYo.with(Techniques.SlideInUp).duration(1000).playOn(findViewById(R.id.splash_icon));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, 2000);
    }

}
