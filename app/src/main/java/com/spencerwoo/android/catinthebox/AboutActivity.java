package com.spencerwoo.android.catinthebox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gyf.barlibrary.ImmersionBar;
import com.scalified.fab.ActionButton;

import mehdi.sakout.fancybuttons.FancyButton;

public class AboutActivity extends AppCompatActivity{

    private TextView githubStar;
    private ImageView aboutIcon;

    private ActionButton fab;
    private ActionButton fabGitHub;
    private ActionButton fabWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImmersionBar.with(AboutActivity.this).init();

        githubStar = findViewById(R.id.github_star);
        githubStar.setMovementMethod(LinkMovementMethod.getInstance());
        githubStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://github.com/spencerwoo98/WeatherMeow"));
                startActivity(browserIntent);
            }
        });

        aboutIcon = findViewById(R.id.about_icon);
        aboutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).duration(1000).playOn(aboutIcon);
            }
        });

        fab = findViewById(R.id.fab);
        fab.setShowAnimation(ActionButton.Animations.FADE_IN);
        fabGitHub = findViewById(R.id.fab_github);
        fabWebsite = findViewById(R.id.fab_web);
        fabGitHub.hide();
        fabWebsite.hide();


        fab.setOnClickListener(new View.OnClickListener() {
            boolean fabsShown = false;
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada).duration(1000).playOn(fab);
                if (!fabsShown) {
                    fabGitHub.show();
                    fabWebsite.show();
                    fabGitHub.moveUp(100.0f);
                    fabWebsite.moveLeft(100.0f);
                    fabsShown = true;
                } else {
                    fabGitHub.moveDown(100.0f);
                    fabWebsite.moveRight(100.0f);
                    fabGitHub.hide();
                    fabWebsite.hide();
                    fabsShown = false;
                }
            }
        });

        fabGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url1 = "https://github.com/spencerwoo98/WeatherMeow";
                Intent oneIntent = new Intent(Intent.ACTION_VIEW);
                oneIntent.setData(Uri.parse(url1));
                startActivity(oneIntent);
            }
        });

        fabWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url2 = "https://spencerwoo.com";
                Intent twoIntent = new Intent(Intent.ACTION_VIEW);
                twoIntent.setData(Uri.parse(url2));
                startActivity(twoIntent);
            }
        });
    }
}
