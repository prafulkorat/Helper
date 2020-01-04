package com.demo.parag.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PostService extends AppCompatActivity  implements View.OnClickListener{
    TextView text_service;
    EditText user_name,Address;
    Button btnPostReq;
    ProgressDialog progressDialog;
    LayoutInflater inflater;
    String responseString;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Helper</font>"));
        setContentView(R.layout.activity_post_service);
        String serviceName=getIntent().getStringExtra("serviceName");

        text_service=findViewById(R.id.text_service);
        user_name=findViewById(R.id.edt_Name_user);
        Address=findViewById(R.id.edt_Address);
        btnPostReq=findViewById(R.id.btn_sendReq);

        text_service.setText(serviceName);
        btnPostReq.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_sendReq:


                if (user_name.getText().toString().trim().length()>0 && Address.getText().toString().trim().length()>10) {

                    String address=Address.getText().toString().trim();
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    progressDialog = new ProgressDialog(PostService.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    String serviceName=getIntent().getStringExtra("serviceName");

                    final JSONObject jsonObject = new JSONObject();
                    try {
                        SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                        int userID=sharedpreferences.getInt("userID",-99);
                        jsonObject.put("task", "create_post");
                        jsonObject.put("user_id",userID);
                        jsonObject.put("type","4");
                        jsonObject.put("name", serviceName);
                        jsonObject.put("question",address);

                        Log.d("test", "test :" + jsonObject.toString());
                        //jsonObject.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //send this json to server

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            placeWebCall(Configuration.URL, jsonObject);
                        }
                    });

                    thread.start();
                }
                else{

                   Toast.makeText(this,"Enter valide detail please..",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void placeWebCall(String url, JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

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


           /* String delStr = "SMTP Error: Could not connect to SMTP host.";
            final String newStr= responseString.replace(delStr, "");*/

            final String newRespStr = responseString.substring(responseString.indexOf("{"), responseString.indexOf("}") + 1);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(PostService.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse = new JSONObject(newRespStr);
            Log.d("email", "email" + jsonResponse.getString("code"));

            if (jsonResponse.getInt("code") == 200) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        progressDialog.dismiss();
                        finish();

                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            progressDialog.dismiss();
                            Toast.makeText(PostService.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
