package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView result;
    Button hit;
    private ViewPager viewPager;
    private NavigationDrawerFragment navigationDrawerFragment;
    private Toolbar toolbar;
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting unique device Id
        AndroidId getId = new AndroidId(getApplicationContext());
        deviceId = getId.getId();

        //Saving unique Id
        SharedPreferences sharedpreferences = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("device_id", deviceId);
        editor.commit();

        //Setting and Coloring Toolbar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        final ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor("#D57B33"));
        toolbar.setBackgroundDrawable(colorDrawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        //Building Navigation Drawer
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
        DrawerLayout mainLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
        navigationDrawerFragment.setUp(mainLayout, toolbar);

        //Building Viewpager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        AdvertismentAdapter adapter = new AdvertismentAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        hit = (Button) findViewById(R.id.categoryMore);
        result = (TextView) findViewById(R.id.result);

        hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask task = new BackgroundTask();
                task.execute("http://jasonparsing.parseapp.com/jsonData/moviewDemoItem.txt");
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                URL apiLink = new URL(params[0]);
                urlConnection = (HttpURLConnection)apiLink.openConnection();
                urlConnection.connect();

                InputStream stream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result.setText(deviceId);
        }
    }
}
