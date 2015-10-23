package com.affordablehomesindia.mandikart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;

import models.OrderModel;

public class RegistrationForm extends AppCompatActivity {

    EditText firmName, yourName, mobileNo, email, vat, address;
    Spinner cityName;
    private SharedPreferences pref_personal;
    private SharedPreferences.Editor editor_personal;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    ProgressDialog dialog;
    String url;
    String JsonResult = null;
    Button order;
    OrderModel orderModel;
    DatabaseHelper helper;
    String city_name[];
    String city_code[];
    int city_pos[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        NetworkConnection connection = new NetworkConnection(RegistrationForm.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (connection.isConnected()) {
            helper = new DatabaseHelper(RegistrationForm.this);
            helper.open();
            Cursor cursor = helper.getData("deliver_city");
            cursor.moveToFirst();
            city_code= new String[cursor.getCount()];
            city_pos = new int[cursor.getCount()];
            city_name= new String [cursor.getCount()];
            for(int i =0 ; i<cursor.getCount();i++){
                cursor.moveToPosition(i);
                city_name[i]= cursor.getString(cursor.getColumnIndex("name"));
                city_code[i] = cursor.getString(cursor.getColumnIndex("code"));
                city_pos[i]= cursor.getInt(cursor.getColumnIndex("position"));
            }
            pref_personal = getSharedPreferences("PERSONAL_DETAIL", Context.MODE_PRIVATE);
            dialog = new ProgressDialog(this);
            prefs = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            editor = prefs.edit();
            editor_personal = pref_personal.edit();
            firmName = (EditText) findViewById(R.id.firmName);
            yourName = (EditText) findViewById(R.id.yourName);
            mobileNo = (EditText) findViewById(R.id.mobile_no);
            email = (EditText) findViewById(R.id.email);
            cityName = (Spinner) findViewById(R.id.cityName);
            cityName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, city_name));
            vat = (EditText) findViewById(R.id.vat);
            address = (EditText) findViewById(R.id.address);
            mobileNo.setText(prefs.getString("phn_screen", ""));
            firmName.setText(pref_personal.getString("firm_name", ""));
            yourName.setText(pref_personal.getString("user_name", ""));
            email.setText(pref_personal.getString("email_id", ""));
            vat.setText(pref_personal.getString("firm_tin", ""));
            address.setText(pref_personal.getString("address", ""));

            cityName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    editor_personal.putString("city", city_code[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            order = (Button) findViewById(R.id.order);
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (firmName.getText().toString().equals("") && mobileNo.getText().toString().equals("")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrationForm.this);
                        dialog.setMessage("Kindly enter your firm name and mobile no.");
                        dialog.setTitle("Message");
                        dialog.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    } else {
                        editor_personal.putString("user_name", yourName.getText().toString());
                        editor_personal.putString("firm_name", firmName.getText().toString());
                        editor_personal.putString("phone_no", mobileNo.getText().toString());
                        editor_personal.putString("email_id", email.getText().toString());

                        editor_personal.putString("firm_tin", vat.getText().toString());
                        editor_personal.putString("address", address.getText().toString());
                        editor_personal.commit();

                        //  Toast.makeText(getApplicationContext(), Json, Toast.LENGTH_LONG).show();
                        DoTask doTask = new DoTask();
                        doTask.execute();
                    }

                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationForm.this);
            builder.setTitle("Network Connection Error");
            builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RegistrationForm.this.finish();
                }
            });
            builder.show();

        }


    }

    public class DoTask extends AsyncTask<Object, Object, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            url = "http://mandikart.com/mapi/cartbuyer/checkout/";
            dialog.show();
        }

        @Override
        protected String doInBackground(Object[] params) {

            try {
                GetOrderJson util = new GetOrderJson(RegistrationForm.this);
                String putJson = util.makeJson();
                System.out.println(putJson);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                StringEntity entity = new StringEntity(putJson);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = client.execute(post);
                JsonResult = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(JsonResult);
                return JsonResult;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            dialog.dismiss();
            orderModel = GetJsonData.getOrderDetail(o);
            if (orderModel.getOrderNo() == null) {
                pref_personal.edit().clear();
                prefs.edit().clear();

                helper.deleteTable("shop_cart");
                helper.deleteTable("search");
                AlertDialog.Builder dialog = new AlertDialog.Builder(RegistrationForm.this);
                dialog.setMessage("Please try again!");
                dialog.setTitle("Message");
                dialog.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegistrationForm.this.finish();
                        Intent i = new Intent(RegistrationForm.this, StartupActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                dialog.show();
            } else {
                helper.deleteTable("shop_cart");
                Intent intent = new Intent(RegistrationForm.this, FinalActivity.class);
                intent.putExtra("order_no", orderModel.getOrderNo());
                intent.putExtra("delivery_id", orderModel.getDelivery_id());
                intent.putExtra("password", orderModel.getPassword());
                startActivity(intent);
                RegistrationForm.this.finish();
            }
            helper.close();
        }
    }
}

