package com.affordablehomesindia.mandikart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OtpVerification extends AppCompatActivity {

    String otp_validate = "http://mandikart.com/mapi/cartbuyer/validate_otp/";
    String phone_number;
    EditText otp;
    ProgressDialog dialog;
    String result;
    String deliver_city;
    String token;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        dialog = new ProgressDialog(OtpVerification.this);
        sharedPreferences = getSharedPreferences("USER_DETAIL", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle bundle = getIntent().getExtras();
        phone_number = bundle.getString("phone_number");
        deliver_city = bundle.getString("user_city");
        otp = (EditText) findViewById(R.id.otp_number);
        Button proceedButton = (Button) findViewById(R.id.buttonProceed);
        TextView view = (TextView)findViewById(R.id.phoneNumber);
        view.setText(phone_number);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavUtils.navigateUpFromSameTask(OtpVerification.this);
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConnection connection = new NetworkConnection(OtpVerification.this);
                if (connection.isConnected()) {
                if(otp.getText().toString().equals("")){
                    Toast.makeText(OtpVerification.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if(otp.getText().toString().length() == 4){
                        OTPVerification verification = new OTPVerification();
                        verification.execute(otp_validate + phone_number + "/" + otp.getText().toString());

                    } else{
                        Toast.makeText(OtpVerification.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(OtpVerification.this);
                builder.setTitle("Network Connection Error");
                builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
                builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
            }
        });

    }

    public class OTPVerification extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String isVerified = "";
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(post);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject object = null;
            try {
                object = new JSONObject(result);
                for(int i = 0; i < object.length(); i++ ){
                    try {
                        if(object.getString("status").equals("SUCCESS")){
                            isVerified = object.getString("status");
                            token = object.getString("token");
                        } else {
                            isVerified = "ERROR";
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return isVerified;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.toString().toLowerCase().equals("success")){
                Toast.makeText(OtpVerification.this, "Verified" + token, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpVerification.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("phone_number", phone_number);
                intent.putExtras(bundle);
                editor.putString("phn_screen", phone_number);
                editor.putString("city", deliver_city);
                editor.putString("token", token);
                editor.commit();
                startActivity(intent);
                overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_open_scale);
            } else {
                Toast.makeText(OtpVerification.this, "Invalid OTP or OTP expired", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
