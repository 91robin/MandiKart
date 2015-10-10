package com.affordablehomesindia.mandikart;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    Button skip, sign_in;
    TextView app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = (ImageView) findViewById(R.id.app_icon);

        app_name = (TextView) findViewById(R.id.app_name);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/djb_custom_bold.ttf");
        app_name.setTypeface(custom_font);
        app_name.setAlpha(0);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        imageView.setAnimation(anim);
        anim.start();

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                app_name.setAlpha(100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });

        sign_in = (Button) findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //skip.setAlpha(0);
                //skip.setClickable(false);
                //sign_in.setAlpha(0);
                //sign_in.setClickable(false);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
