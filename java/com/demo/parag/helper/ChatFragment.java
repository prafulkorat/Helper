package com.demo.parag.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ChatFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    ImageView logout;
    private Button showAns;
    private TextView txtServicename, txtDateTime, txtAnscount;
    private Context context;
    private String postId;
    Handler mHandler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_chat, container, false);

        if (container != null) {
            context = container.getContext();
        }
        txtServicename = rootView.findViewById(R.id.tv_servicename);
        txtDateTime = rootView.findViewById(R.id.tv_date_time);
        txtAnscount = rootView.findViewById(R.id.tv_answer);
        showAns=rootView.findViewById(R.id.btn_showAns);

        this.mHandler = new Handler();
        m_Runnable.run();



        showAns.setOnClickListener(this);

        return rootView;
    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            ReqPostTask reqPostTask = new ReqPostTask();
            reqPostTask.execute();

            mHandler.postDelayed(m_Runnable,2000);
        }

    };



    class ReqPostTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            JSONObject jsonObject = formJson();
            String responseString = placeWebCall(Configuration.URL, jsonObject);

            return responseString;
        }

        @Override
        protected void onPostExecute(String string) {

            try {
                if (string != null) {
                    JSONObject jsonObject = new JSONObject(string);
                    final JSONArray answers = jsonObject.getJSONArray("data");
                   // JSONObject childjsonObject= (JSONObject) jsonObject.getJSONObject("data");
                   // final String newRespStr=answers.substring(responseString.indexOf("{"),responseString.indexOf("}")+1);

                   // JSONArray jsonarray = new JSONArray(jsonObject);
                    for (int i = 0; i < answers.length(); i++) {
                        JSONObject jsonobject = answers.getJSONObject(i);
                        postId=jsonobject.getString("post_id");
                        txtServicename.setText(jsonobject.getString("name"));
                        txtDateTime.setText(jsonobject.getString("created"));
                        txtAnscount.setText(jsonobject.getString("answers_count"));

                    }
                   // Log.d("chatTest", "" + childjsonObject);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        private JSONObject formJson() {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("task", "user_wall");
                SharedPreferences sharedpreferences = context.getSharedPreferences("mysharedpref", Context.MODE_PRIVATE);
                int userID = sharedpreferences.getInt("userID", -99);
                jsonObject.put("user_id", userID);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        private String placeWebCall(String url, JSONObject jsonObject) {

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            String responseString = null;

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
    @Override
    public void onClick(View view) {
        Intent i=new Intent(context,AnsListActivity.class);
        i.putExtra("postid",postId);
        context.startActivity(i);

    }
}
