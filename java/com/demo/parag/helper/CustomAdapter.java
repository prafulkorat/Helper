package com.demo.parag.helper;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel>{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView title_requirement,serviceName,user_name,sub_service,title_address,user_address;

    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.title_requirement = (TextView) convertView.findViewById(R.id.title_requirement);
            viewHolder.serviceName = (TextView) convertView.findViewById(R.id.serviceName);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.sub_service = (TextView) convertView.findViewById(R.id.sub_service);
            viewHolder.title_address = (TextView) convertView.findViewById(R.id.title_address);
            viewHolder.user_address = (TextView) convertView.findViewById(R.id.user_address);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }



        viewHolder.title_requirement.setText(dataModel.getTitle_requirement());
        viewHolder.serviceName.setText(dataModel.getServiceName());
        viewHolder.user_name.setText(dataModel.getUser_name());
        viewHolder.sub_service.setText(dataModel.getSub_service());
        viewHolder.title_address.setText(dataModel.getTitle_address());
        viewHolder.user_address.setText(dataModel.getUser_address());

        // Return the completed view to render on screen
        return convertView;
    }
}
