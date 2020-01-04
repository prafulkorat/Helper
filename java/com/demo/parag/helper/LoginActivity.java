package com.demo.parag.helper;

import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail;
    private Button btnSubmit;
    ImageView backArrow;
    String responseString;
    Handler handler = new Handler();
    ProgressDialog progressDialog;
    Toast toast;
    private BroadcastReceiver mNetworkReceiver;
    public static TextView myText;
    static View myView;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_login);
        myView = findViewById(R.id.my_view_main_login);
        myText=findViewById(R.id.internet_text_login);


        findViews();
        btnSubmit.setOnClickListener(this);
        edtEmail.addTextChangedListener(edtEmailWatcher);
        backArrow.setOnClickListener(this);

    }

    private void findViews() {
        edtEmail = findViewById(R.id.edtEmail);
        btnSubmit = findViewById(R.id.btn_submit);
        backArrow=findViewById(R.id.back_arrow_login);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_submit:
                String email = edtEmail.getText().toString();

                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);


                    final JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("task", "login");

                        jsonObject.put("email", email);
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

                } else{

                        inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast, null);
                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("Enter valide email please..!!");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_LONG);
                        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setView(layout);
                        toast.show();
                     }
                break;
            case R.id.back_arrow_login:

                Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
                finish();

                break;
        }
    }
    TextWatcher edtEmailWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


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

                    Toast.makeText(LoginActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse = new JSONObject(newRespStr);
            Log.d("email", "email" + jsonResponse.getString("code"));

            if (jsonResponse.getInt("code") == 200) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Login Detail sucessfull..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra("email", edtEmail.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        progressDialog.dismiss();
                        finish();

                    }
                });
            } else if (jsonResponse.getInt("code") == 400) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        intent.putExtra("email", edtEmail.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        progressDialog.dismiss();
                        finish();

                    }
                });
            }  else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }



}
