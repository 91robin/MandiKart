package com.affordablehomesindia.mandikart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

public class StartupActivity extends Activity {

    AndroidId deviceId;
    String device_id;
    String deviceIdJson, apiJson;
    JsonUtil jsonUtil;
    GetJsonData jsonData;
    private ImageView imageView;
    private TextView text1;
    private TextView text2;
    private ImageView blackboard;
    private TextView janne;
    private Button button;
    private LinearLayout layout;
    DatabaseHelper helper;
    ContentValues values;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        sharedPreferences = getSharedPreferences("USER_DETAIL", MODE_PRIVATE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        imageView = (ImageView) findViewById(R.id.logo);
        NetworkConnection connection = new NetworkConnection(StartupActivity.this);
        if(connection.isConnected()) {
            text1 = (TextView) findViewById(R.id.text1);
            text2 = (TextView) findViewById(R.id.text2);
            blackboard = (ImageView) findViewById(R.id.blackboard);
            janne = (TextView) findViewById(R.id.jaanne);
            button = (Button) findViewById(R.id.button);
            layout = (LinearLayout) findViewById(R.id.linear);
            deviceId = new AndroidId(getApplicationContext());
            device_id = deviceId.getId();
            jsonUtil = new JsonUtil();
            deviceIdJson = jsonUtil.makeJson(device_id);
            jsonData = new GetJsonData();
            helper = new DatabaseHelper(StartupActivity.this);
            helper.open();
            helper.deleteTable("search");


            BackgoundTask task = new BackgoundTask();
            task.execute("http://mandikart.com/mapi/rawdata/rawmetaproduct");

            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new AccelerateInterpolator());
            fadeIn.setDuration(1000);

            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setStartOffset(2000);
            fadeOut.setDuration(1000);

            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(fadeIn);
            animation.addAnimation(fadeOut);
            imageView.setAnimation(animation);
            text1.setAnimation(animation);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation animation1 = new AlphaAnimation(0, 1);
                    animation1.setDuration(500);
                    text1.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    blackboard.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    janne.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.VISIBLE);
                    janne.setAnimation(animation1);
                    blackboard.setAnimation(animation1);
                    text2.setAnimation(animation1);
                    button.setAnimation(animation1);
                    layout.setAnimation(animation1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sharedPreferences.getString("token","").equals("")){
                        Intent in = new Intent(StartupActivity.this, PhoneVerification.class);
                        startActivity(in);
                    }else{
                        Intent in = new Intent(StartupActivity.this, MainActivity.class);
                        startActivity(in);
                    }

                }
            });
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(StartupActivity.this);
            builder.setTitle("Network Connection Error");
            builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    StartupActivity.this.finish();
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public class BackgoundTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(params[0]);

                StringEntity entity = new StringEntity(deviceIdJson);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");

                HttpResponse httpResponse = client.execute(post);
                apiJson = EntityUtils.toString(httpResponse.getEntity());

                return apiJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject object = null;
            try {
                object = new JSONObject(apiJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray products = null;
            try {
                products = object.getJSONArray("products");
                for (int i = 0; i < products.length(); i++) {
                    values = new ContentValues();
                    values.put("id", Integer.parseInt(products.getJSONObject(i).getString("id")));
                    values.put("name", products.getJSONObject(i).getString("name"));
                    values.put("brand_name", products.getJSONObject(i).getString("brand_name"));
                    values.put("type", "product");
                    helper.insertData("search", values);
                }

                products = object.getJSONArray("brands");
                for (int i=0; i < products.length(); i++){
                    values = new ContentValues();
                    values.put("id", Integer.parseInt(products.getJSONObject(i).getString("id")));
                    values.put("name", products.getJSONObject(i).getString("name"));
                    values.put("brand_name", "");
                    values.put("type", "brand");
                    helper.insertData("search", values);
                }

                products = object.getJSONArray("sub_categories");
                for (int i=0; i < products.length(); i++){
                    values = new ContentValues();
                    values.put("id", Integer.parseInt(products.getJSONObject(i).getString("id")));
                    values.put("name", products.getJSONObject(i).getString("name"));
                    values.put("category_id", products.getJSONObject(i).getString("category_id"));
                    values.put("brand_name", "");
                    values.put("type", "sub_category");
                    helper.insertData("search", values);
                    //Toast.makeText(StartupActivity.this, products.getJSONObject(i).getString("name").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                products = object.getJSONArray("cities");
                for (int i=0; i < products.length(); i++){
                    values = new ContentValues();
                    values.put("code", products.getJSONObject(i).getString("code"));
                    values.put("name", products.getJSONObject(i).getString("name"));
                    values.put("position",i);
                    helper.insertData("deliver_city", values);
                    //Toast.makeText(StartupActivity.this, products.getJSONObject(i).getString("name").toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            helper.close();
        }
    }
}