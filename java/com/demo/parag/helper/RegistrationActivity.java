package com.demo.parag.helper;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtName, edtEmail, edtMobile;
    private ImageView imgUser,backArrow;

    private RadioButton rdbMale;
    private RadioButton rdbFemale;
    private Button btnSubmit;
    String responseString=null;
    public String temp;
    Handler handler=new Handler();
    int PICK_IMAGE_REQUEST=1;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    LayoutInflater inflater;
    public static TextView myText;
    private BroadcastReceiver mNetworkReceiver;
    static View myView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_registration);
        String email=getIntent().getStringExtra("email");
        findViews();
        myView = findViewById(R.id.my_view_main_register);
        myText=findViewById(R.id.internet_text_register);

        btnSubmit.setOnClickListener(this);
        imgUser.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        edtEmail.setText(email);

    }
    private void findViews() {
        edtName = findViewById(R.id.edt_Name);
        edtEmail = findViewById(R.id.edt_Email);
        edtMobile = findViewById(R.id.edt_Mobile);
        rdbMale = findViewById(R.id.rdb_Male);
        rdbFemale = findViewById(R.id.rdb_Female);
        imgUser=findViewById(R.id.img_User);
        btnSubmit=findViewById(R.id.btn_submit);
        backArrow=findViewById(R.id.back_arrow_reg);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_submit:
                if (isEdtEmpty(edtName)) {
                    String t_name="Enter valide NAME please..!!";
                    toast(t_name);
                    //Toast.makeText(RegistrationActivity.this, "", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
                    String t_email="Enter valide EMAIL please..!!";
                    toast(t_email);
                    //Toast.makeText(RegistrationActivity.this, "Enter valide Email please..!!", Toast.LENGTH_SHORT).show();

                } else if (edtMobile.length()<10) {
                    String t_mobile="Enter valide MOBILE NUMBER please..!!";
                    toast(t_mobile);
                    //Toast.makeText(RegistrationActivity.this, "Enter valide Mobile number please..!!", Toast.LENGTH_SHORT).show();

                } else if ((!rdbMale.isChecked()) && (!rdbFemale.isChecked())) {
                    String t_gender="Enter valide GENDER please..!!";
                    toast(t_gender);
                    // Toast.makeText(RegistrationActivity.this, "Please select Gender..!!", Toast.LENGTH_SHORT).show();

                }else{
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    progressDialog = new ProgressDialog(RegistrationActivity.this,R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    webCallThread.start();
                }


                break;


            case R.id.img_User:

                setImage();
                break;

            case R.id.back_arrow_reg:

                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left);
                finish();

                break;
        }

    }
    void toast(String t_value){
        inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(t_value);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
    }
    private void setImage() {

        Intent intent = new Intent ();
        intent.setType ("image/*");
        intent.setAction (Intent.ACTION_GET_CONTENT);
        startActivityForResult (Intent.createChooser (intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    Thread webCallThread =new Thread(new Runnable() {
        @Override
        public void run() {

            JSONObject formedJson=formJson();
            placeWebCall(Configuration.URL,formedJson);
        }
    });

    private JSONObject formJson() {

        final JSONObject jsonObject=new JSONObject();
        try {

            jsonObject.put("task","register");
            jsonObject.put("name",getEdtValue(edtName));
            jsonObject.put("email",getEdtValue(edtEmail));
            jsonObject.put("mobile",getEdtValue(edtMobile));
            String strGendr=rdbMale.isChecked()?"M":"F";
            jsonObject.put("gender",strGendr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private String getEdtValue(EditText edt) {

        return edt.getText().toString().trim();
    }


    private boolean isEdtEmpty(EditText edt) {

        return edt.getText().toString().trim().length()<=0;
    };




    private void placeWebCall(String url,JSONObject jsonObject) {

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("reqObject", jsonObject.toString());
            if(bitmap!=null){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte [] data = bos.toByteArray();
                long l=System.currentTimeMillis();
                entityBuilder.addPart("avatar", new ByteArrayBody(data,"image/jpeg", "image"+l+".jpg"));
            }

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
            responseString = out.toString();

            final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(RegistrationActivity.this, newRespStr, Toast.LENGTH_SHORT).show();
                }
            });
            final JSONObject jsonResponse=new JSONObject(newRespStr);

            if(jsonResponse.getInt("code") == 200){
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(RegistrationActivity.this,"Registration sucessfull..", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegistrationActivity.this,OtpActivity.class);
                        intent.putExtra("email",edtEmail.getText().toString());
                        startActivity(intent);
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
                            Toast.makeText(RegistrationActivity.this,jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                imgUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}
