package com.demo.parag.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
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

public class OtpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    EditText otpText, edt1, edt2, edt3, edt4, edt5, edt6;
    TextView errorOtpText,txtEmail,txtEmail2,trouble_text;
    String digit1,digit2,digit3,digit4,digit5,digit6;
    Button btnSubmit,btnResend,btnCancle;
    String responseString;
    Handler handler=new Handler();
    ProgressDialog progressDialog;
    View myView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_otp);

        progressDialog = new ProgressDialog(OtpActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Wait For Otp ...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }, 7000);
        findViews();
        String email=getIntent().getStringExtra("email");
        txtEmail.setText(email+".");
        txtEmail2.setText(email+".");
        textChangeAndOnKeyListeners();

        /*otpText = findViewById(R.id.inputOtp);
        btnOtpVerify = findViewById(R.id.btn_verify_otp);
        otpText.addTextChangedListener(edtOtpWatcher);*/


    }



    private void findViews() {
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        edt5 = findViewById(R.id.edt5);
        edt6 = findViewById(R.id.edt6);
        txtEmail=findViewById(R.id.txt_email);
        txtEmail2=findViewById(R.id.txt_email2);
        errorOtpText=findViewById(R.id.error_otp_txt);
        btnSubmit=findViewById(R.id.btn_submit);
        trouble_text=findViewById(R.id.text_trouble);
        btnResend=findViewById(R.id.btn_resend_code);
        btnCancle=findViewById(R.id.btn_cancel);
        myView = findViewById(R.id.my_view);

    }

    private void textChangeAndOnKeyListeners() {
        edt1.addTextChangedListener(edt1Watcher);
        edt2.addTextChangedListener(edt2Watcher);
        edt3.addTextChangedListener(edt3Watcher);
        edt4.addTextChangedListener(edt4Watcher);
        edt5.addTextChangedListener(edt5Watcher);
        edt6.addTextChangedListener(edt6Watcher);

        edt2.setOnKeyListener(this);
        edt3.setOnKeyListener(this);
        edt4.setOnKeyListener(this);
        edt5.setOnKeyListener(this);
        edt6.setOnKeyListener(this);
        btnSubmit.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        trouble_text.setOnClickListener(this);

    }

    TextWatcher edt1Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt1.getText().toString().length() == 1)     //size as per your requirement
            {
                edt1.clearFocus();
                edt2.requestFocus();
            }
            errorOtpText.setVisibility(View.GONE);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };
    TextWatcher edt2Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt2.getText().toString().length() == 1)     //size as per your requirement
            {
                edt2.clearFocus();
                edt3.requestFocus();
            }
            errorOtpText.setVisibility(View.GONE);


        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    };
    TextWatcher edt3Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt3.getText().toString().length() == 1)     //size as per your requirement
            {
                edt3.clearFocus();
                edt4.requestFocus();
            }
            errorOtpText.setVisibility(View.GONE);

        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    };
    TextWatcher edt4Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt4.getText().toString().length() == 1)     //size as per your requirement
            {
                edt4.clearFocus();
                edt5.requestFocus();
            }
            errorOtpText.setVisibility(View.GONE);

        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    };
    TextWatcher edt5Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (edt5.getText().toString().length() == 1)     //size as per your requirement
            {
                edt5.clearFocus();
                edt6.requestFocus();
            }
            errorOtpText.setVisibility(View.GONE);

        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    };
    TextWatcher edt6Watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            errorOtpText.setVisibility(View.GONE);

        }

        @Override
        public void afterTextChanged(Editable s) {


        }

    };


    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        switch (view.getId()) {

            case R.id.edt2:

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edt2.getText().toString().length() == 0){
                        edt2.clearFocus();
                        edt1.requestFocus();
                    }

                }
                break;
            case R.id.edt3:

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edt3.getText().toString().length() == 0){
                        edt3.clearFocus();
                        edt2.requestFocus();
                    }

                }
                break;
            case R.id.edt4:

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edt4.getText().toString().length() == 0){
                        edt4.clearFocus();
                        edt3.requestFocus();
                    }

                }
                break;
            case R.id.edt5:

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edt5.getText().toString().length() == 0){
                        edt5.clearFocus();
                        edt4.requestFocus();
                    }

                }
                break;
            case R.id.edt6:

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (edt6.getText().toString().length() == 0){
                        edt6.clearFocus();
                        edt5.requestFocus();
                    }
                }
                break;

        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_submit:

                String otp;
                digit1=edt1.getText().toString();
                digit2=edt2.getText().toString();
                digit3=edt3.getText().toString();
                digit4=edt4.getText().toString();
                digit5=edt5.getText().toString();
                digit6=edt6.getText().toString();
                otp=digit1+digit2+digit3+digit4+digit5+digit6;
                if (otp.length() == 6) {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    progressDialog = new ProgressDialog(OtpActivity.this,R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    String email=getIntent().getStringExtra("email");

                    final JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("task","verify_otp");
                        jsonObject.put("email",email);
                        jsonObject.put("otp",otp);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //send this json to server
                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            placeWebCall(Configuration.URL,jsonObject);
                        }
                    });
                    thread.start();
                }else{
                    errorOtpText.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.text_trouble:
                InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                slideUp(myView);
                break;

            case R.id.btn_resend_code:

                final JSONObject jsonObjectL = new JSONObject();
                try {

                    jsonObjectL.put("task", "login");
                    String email=getIntent().getStringExtra("email");

                    jsonObjectL.put("email", email);
                    Log.d("test", "test :" + jsonObjectL.toString());
                    //jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //send this json to server

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        placeWebCallL(Configuration.URL, jsonObjectL);
                    }
                });

                thread.start();

                slideDown(myView);


                break;

            case R.id.btn_cancel:
                slideDown(myView);
                break;
        }
    };

    private void placeWebCall(String url,JSONObject jsonObject) {

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

            ByteArrayOutputStream out = new ByteArrayOutputStream ();
            try {
                resp.getEntity ().writeTo (out);
            } catch (IOException e) {
                e.printStackTrace ();
            }
            //Convert response into string

            responseString = out.toString();

           /* String delStr = "SMTP Error: Could not connect to SMTP host.";
            final String newStr= responseString.replace(delStr, "");*/

            final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(OtpActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse=new JSONObject(newRespStr);

            if(jsonResponse.getInt("code") == 200){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedpreferences.edit();
                        try {
                            editor.putInt("userID",jsonResponse.getInt("user_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                        Toast.makeText(OtpActivity.this,"Login sucessfull..", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(OtpActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                        progressDialog.dismiss();
                        finish();

                    }
                });
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        errorOtpText.setVisibility(View.VISIBLE);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void placeWebCallL(String url, JSONObject jsonObject) {

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

                    Toast.makeText(OtpActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse = new JSONObject(newRespStr);
            Log.d("email", "email" + jsonResponse.getString("code"));



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

}



