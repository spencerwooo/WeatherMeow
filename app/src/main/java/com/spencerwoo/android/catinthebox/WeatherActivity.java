package com.spencerwoo.android.catinthebox;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.gyf.barlibrary.ImmersionBar;
import com.scalified.fab.ActionButton;
import com.spencerwoo.android.catinthebox.gson.Forcast;
import com.spencerwoo.android.catinthebox.gson.Weather;
import com.spencerwoo.android.catinthebox.util.CircleProgressBarDrawable;
import com.spencerwoo.android.catinthebox.util.HttpUtil;
import com.spencerwoo.android.catinthebox.util.Utility;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public PullToRefreshView pullToRefreshView;
    private Button navButton;
    private ActionButton refreshImage;

    private String mWeatherId;

    private ScrollView weatherLayout;
    private LinearLayout forecastLayout;

    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;

    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    private SimpleDraweeView bingPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            );
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
            ImmersionBar.with(WeatherActivity.this).statusBarDarkFont(true).init();
        }

        setContentView(R.layout.activity_weather);

        // Init all layouts/views
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);

        drawerLayout = findViewById(R.id.drawer_layout);
        navButton = findViewById(R.id.nav_button);
        refreshImage = findViewById(R.id.refresh_image);

        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);

        bingPicImg = findViewById(R.id.bing_pic_img);

        pullToRefreshView = findViewById(R.id.swipe_refresh);

        // Get weather info
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            // If there is cached info
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
            loadUnsplashImage();
        } else {
            // If there's no local info, get from server
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            long REFRESH_DELAY = 3000;
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
                requestWeather(mWeatherId);

                long updateTime = System.currentTimeMillis();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                long lastUpdateTime = preferences.getLong("image_update_time", 0);

                // Update every other hour
                if (updateTime - lastUpdateTime > 60 * 60 * 3600) {
                    final ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.clearCaches();
                }
                loadUnsplashImage();

                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putLong("image_update_time", updateTime);
                editor.apply();
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingsMenu(view);
            }
        });

        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).duration(1000).playOn(refreshImage);
                final ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                loadUnsplashImage();
            }
        });
    }


    public void requestWeather(final String weatherId) {

        String weatherUrl = "http://guolin.tech/api/weather?cityid=" +
                weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9"; //1f1950ed4eba4b9ebad51b7cc3baf098"
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "啊，出错了...qaq", Toast.LENGTH_SHORT).show();
                        pullToRefreshView.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "啊，失败了...qaq", Toast.LENGTH_SHORT).show();
                        }
                        pullToRefreshView.setRefreshing(false);
                    }
                });
            }
        });

        loadUnsplashImage();
    }

    private void showWeatherInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/tc-zh.ttf");
        titleCity.setTypeface(tf);
        titleUpdateTime.setText("Updated on: " + updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for (Forcast forcast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forcast.date);
            infoText.setText(forcast.more.info);
            maxText.setText(forcast.temperature.max);
            minText.setText(forcast.temperature.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车吗：" + weather.suggestion.carWash.info;
        String sport = "出门吗：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void loadUnsplashImage() {
        final String unsplashImage = "https://source.unsplash.com/collection/893395/900x1600";
        Uri uri = Uri.parse(unsplashImage);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(bingPicImg.getController())
                .build();
        bingPicImg.setController(controller);
        bingPicImg.getHierarchy().setProgressBarImage(new CircleProgressBarDrawable());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bingPicImg.setImageURI(unsplashImage);
            }
        });
    }

    private void showSettingsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(WeatherActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.settings_menu, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.about: {
                        Intent mainIntent = new Intent(WeatherActivity.this, AboutActivity.class);
                        WeatherActivity.this.startActivity(mainIntent);
                        break;
                    }
                    case R.id.version: {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WeatherActivity.this);
                        alertDialogBuilder.setMessage("\nWeather Meow Version 1.0");
                        alertDialogBuilder.setTitle("版本信息");
                        alertDialogBuilder.setIcon(R.mipmap.meow);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                    }
                    case R.id.city_chooser:
                        drawerLayout.openDrawer(GravityCompat.START);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
