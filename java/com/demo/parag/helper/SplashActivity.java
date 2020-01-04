package com.demo.parag.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity{
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                final int userID=sharedpreferences.getInt("userID",-99);

                if(userID==-99){
                    Log.d("user id","user_id :"+userID);
                    startActivity(new Intent(SplashActivity.this, Main2Activity.class));
                    overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                    finish();                }
            }
        }, 2000);

        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mysplashanimation);
        logo.startAnimation(myanim);

/*        Thread thread=new Thread(runnable);
        thread.start();*/
    }


/*

     Runnable runnable=new Runnable() {
        @Override
        public void run() {
            waitForFew();
            SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            final int userID=sharedpreferences.getInt("userID",-99);

            if(userID==-99){
                Log.d("user id","user_id :"+userID);
                goToLogin();
            }else{
                goToHome();
            }
            Animation myanim = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.mysplashanimation);
            logo.startAnimation(myanim);
        }
    };

    private void waitForFew() {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/



}



