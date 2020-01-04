package com.demo.parag.helper;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    public TextView mailbox;
    public static TextView myText;
    private BroadcastReceiver mNetworkReceiver;
    static View myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main2);
        mailbox=findViewById(R.id.mailbox);
        myView = findViewById(R.id.my_view_main2_activity);
        myText=findViewById(R.id.internet_text_main2Activity);
        mailbox.setOnClickListener(this);
       // tv_check_connection=(TextView) findViewById(R.id.tv_check_connection);

    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(Main2Activity.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }





}

