package com.demo.parag.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AnsListActivity extends AppCompatActivity {
    private JSONObject jsonObject = null;
    private Handler handler = new Handler();
    private AnsList objAnsList;
    private ArrayList<AnsList> allAns = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    int postid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Helper</font>"));
        setContentView(R.layout.activity_ans_list);
        postid= Integer.parseInt(getIntent().getStringExtra("postid"));
        getUserData();

        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter();

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout linearLayout;
            TextView txtAns;
            public MyViewHolder(View linearLayout) {
                super(linearLayout);
                txtAns=linearLayout.findViewById(R.id.answer_post_view);



            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Log.d("myAdapter","adpater mehod");

            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_ans, parent, false);

            MyViewHolder vh = new MyViewHolder(linearLayout);
            return vh;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.txtAns.setText(allAns.get(position).getPostans());
           
        }

        @Override
        public int getItemCount() {
            
            return allAns.size();

        }


    }

    public void getUserData() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {

                final String response = placeWebCall();
                allAns=new ArrayList<>();
                try {
                    jsonObject = new JSONObject(response);

                    final JSONArray userJson = jsonObject.getJSONArray("data");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //Log.d("userJson", String.valueOf(userJson.length()));

                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject jsonObjectChild = userJson.getJSONObject(i);

                                    if(Integer.parseInt(jsonObjectChild.getString("post_id"))==postid){
                                        objAnsList = new AnsList();
                                        objAnsList.setPostans(jsonObjectChild.getString("answer"));
                                        allAns.add(objAnsList);

                                    }


                                }
                                recyclerView.setAdapter(adapter);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private String placeWebCall() {

        String responseString = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Configuration.URL);

        try {

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //Log.d("response", formJson().toString());
            entityBuilder.addTextBody("reqObject", formJson().toString());
            HttpEntity entity = entityBuilder.build();

            httpPost.setEntity(entity);
            HttpResponse resp = null;
            try {

                resp = client.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                resp.getEntity().writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            responseString = out.toString();
            //final String newRespStr=responseString.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);
            // final JSONObject jsonResponse=new JSONObject(newRespStr);
            // Log.d("avatar2",""+jsonResponse.getString("avatar"));

            //  Log.d("response2", responseString);


        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseString;
    }

    private JSONObject formJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("task", "user_wall");
            SharedPreferences sharedpreferences = getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
            int userID = sharedpreferences.getInt("userID", -99);
            jsonObject.put("user_id", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
