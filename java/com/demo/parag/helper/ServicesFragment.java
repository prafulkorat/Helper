package com.demo.parag.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class ServicesFragment extends android.support.v4.app.Fragment  {
    GridView androidGridView;
    private Context context;
    String[] gridViewString = {
            "Ac Repairs", "Nanny", "Fitness trainer",
            "Barber", "Driver", "Electrician",
            "Home Shift", "Plumber", "Photographer",
            "Pest Control", "Carpenter", "Cook",
            "House keeper","Nurse","Tutor"


    } ;
    int[] gridViewImageId = {
            R.drawable.ic_ac_repair, R.drawable.ic_babysitter, R.drawable.ic_trainer,
            R.drawable.ic_barber, R.drawable.ic_driver, R.drawable.ic_electrician,
            R.drawable.ic_caretaker, R.drawable.ic_plumber, R.drawable.ic_photographer,
            R.drawable.ic_pestcontrol, R.drawable.ic_carpanter, R.drawable.ic_chef,
            R.drawable.ic_housekeeper, R.drawable.ic_nurse, R.drawable.ic_tutor,



    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_services, container, false);
        if (container != null) {
            context=container.getContext();
        }
        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(getActivity(), gridViewString, gridViewImageId);
        androidGridView=rootView.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                //Toast.makeText(getActivity(), "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context,PostService.class);
                intent.putExtra("serviceName",gridViewString[+i]);
                context.startActivity(intent);
            }
        });





        return rootView;
    }

    /*@Override
    public void onBackStackChanged() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }*/


}
