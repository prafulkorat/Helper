package com.demo.parag.helper;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    Fragment fragmentHome,fragmentServices,fragmentSearch,fragmentChat,fragmentProfile;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public static TextView myText;
    private BroadcastReceiver mNetworkReceiver;
    static View myView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Helper</font>"));

        setContentView(R.layout.activity_main);
        myView = findViewById(R.id.my_view_main_activity);
        myText=findViewById(R.id.internet_text_mainActivity);



        viewPager =findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:


                            viewPager.setCurrentItem(0);


                        return true;
                    case R.id.navigation_services:

                        viewPager.setCurrentItem(1);

                        return true;
                    case R.id.navigation_chat:

                        viewPager.setCurrentItem(2);

                        return true;
                    case R.id.navigation_search:

                        viewPager.setCurrentItem(3);

                        return true;
                    case R.id.navigation_profile:

                        viewPager.setCurrentItem(4);

                        return true;
                }

                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
            }
            Log.d("page", "onPageSelected: " + position);
            bottomNavigationView.getMenu().getItem(position).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(position);

        }


        @Override
        public void onPageScrollStateChanged(int state) {

        }
    });
       /* viewPager.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    });*/
    setupViewPager(viewPager);

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to LOGOUT?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences=getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.remove("userID");
                        editor.clear();
                        editor.apply();
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


        return super.onOptionsItemSelected(item);

    }

    ViewPagerAdapter viewPagerAdapter;

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentHome = new HomeFragment();
        fragmentServices = new ServicesFragment();
        fragmentSearch = new SearchFragment();
        fragmentProfile=new ProfileFragment();
        fragmentChat=new ChatFragment();

        viewPagerAdapter.addFragment(fragmentHome);
        viewPagerAdapter.addFragment(fragmentServices);
        viewPagerAdapter.addFragment(fragmentChat);
        viewPagerAdapter.addFragment(fragmentSearch);
        viewPagerAdapter.addFragment(fragmentProfile);
        viewPager.setAdapter(viewPagerAdapter);
        //this will clear the back stack and displays no animation on the screen
    }
    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            viewPager.setCurrentItem(0, true);
        }
    }






}