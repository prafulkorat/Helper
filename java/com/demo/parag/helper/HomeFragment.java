package com.demo.parag.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("ValidFragment")
public class HomeFragment extends  android.support.v4.app.Fragment {
    private ViewPager mPager;
    private CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.five};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    private LinearLayout item_horizontal;

    private LayoutInflater mInflater;
    private Context context;
    private int[] mImgIds = new int[] { R.drawable.ic_ac_repair, R.drawable.ic_babysitter, R.drawable.ic_trainer,
            R.drawable.ic_barber, R.drawable.ic_driver, R.drawable.ic_electrician,
            R.drawable.ic_caretaker, R.drawable.ic_plumber, R.drawable.ic_photographer,
    };
    private String[] gridViewString =new String[] {
        "Ac Repairs", "Nanny", "Fitness",
                "Barber", "Driver", "Electrician",
                "Home Shift", "Plumber", "Photographer",

    } ;
    private HorizontalScrollView horizontalScrollView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_home, container, false);
        if (container != null) {
            context=container.getContext();
        }
        mPager = rootView.findViewById(R.id.pager);
        indicator = rootView.findViewById(R.id.indicator);
        item_horizontal = rootView.findViewById(R.id.id_gallery);
        listView=rootView.findViewById(R.id.list);

        init();
        mInflater = LayoutInflater.from(getActivity());
        initView();
        itemListview();



        return rootView;
    }


    private void init() {
        ImagesArray.addAll(Arrays.asList(IMAGES));
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void initData() {

    }

    private void initView() {

        for (int i = 0; i < mImgIds.length; i++)
        {

            View view = mInflater.inflate(R.layout.horizontal_scroll_item, item_horizontal, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            txt.setText(gridViewString[i]);

            item_horizontal.addView(view);

        }

    }

    private void itemListview() {

        dataModels= new ArrayList<>();

        dataModels.add(new DataModel("REQUIREMENT","Home Cleaning Services","Milan Patel","General Cleaning -2BHK","Address :","Christ Univercity,Facolty of Engineering, Mysroe Road, Kanminike, Banglore. Pin No: 500407"));
        dataModels.add(new DataModel("REQUIREMENT","Home Cleaning Services","MR Patel","General Cleaning -2BHK","Address :","AMU Univercity,Near Branch of Engineering, Ring Road, Jaipur, Rajsthan. Pin No: 528596"));
        dataModels.add(new DataModel("REQUIREMENT","Home Cleaning Services","MR Patel","General Cleaning -2BHK","Address :","Intercity Row House,Opp. Amin medical, City Road, Kaiminike, Banglore. Pin No: 50048"));

        adapter= new CustomAdapter(dataModels,getActivity().getApplicationContext());

        listView.setAdapter(adapter);
    }



}



