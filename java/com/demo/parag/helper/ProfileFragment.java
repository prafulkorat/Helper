package com.demo.parag.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class ProfileFragment extends android.support.v4.app.Fragment {
    ImageView logout;
    private ImageView imgUser;
    private TextView txtName,txtEmail,txtPhone;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_profile, container, false);
        if (container != null) {
            context=container.getContext();
        }
        imgUser = rootView.findViewById(R.id.usr_img);
        txtName = rootView.findViewById(R.id.tv_name);
        txtEmail = rootView.findViewById(R.id.tv_email);
        txtPhone = rootView.findViewById(R.id.tv_mobile);
        GetProfileTask getProfileTask=new GetProfileTask();
        getProfileTask.execute();

        return rootView;
    }




    class GetProfileTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            JSONObject jsonObject=formJson();
            String responseString=placeWebCall(Configuration.URL,jsonObject);

            return responseString;
        }

        @Override
        protected void onPostExecute(String string) {

            try {
                if(string!=null){
                    JSONObject jsonObject=new JSONObject(string);
                    Log.d("test","test :"+jsonObject.toString());

                    JSONObject childjsonObject=jsonObject.getJSONObject("user");
                    txtName.setText(childjsonObject.getString("name"));
                    txtPhone.setText(childjsonObject.getString("mobile"));
                    txtEmail.setText(childjsonObject.getString("email"));


                    Picasso.with(context)
                            .load(Configuration.URL)
                            .error(R.drawable.ic_user_bg_img)
                            .into(imgUser);
                    //imgUser.setImageURI(Uri.parse(jsonObject.getString("avatar")));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject formJson() {

        JSONObject jsonObject=new JSONObject();

        try {
            jsonObject.put("task","get_user_profile");
            SharedPreferences sharedpreferences = context.getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            int userID=sharedpreferences.getInt("userID",-99);
            jsonObject.put("user_id",userID);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private String placeWebCall(String url, JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String responseString=null;

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("reqObject", jsonObject.toString());
            HttpEntity entity = entityBuilder.build();
            httpPost.setEntity(entity);
            HttpResponse resp = null;
            resp = client.execute(httpPost);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                resp.getEntity().writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Convert response into string
            responseString = out.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }


}
