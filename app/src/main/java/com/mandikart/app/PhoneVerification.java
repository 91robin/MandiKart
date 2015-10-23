package com.mandikart.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class PhoneVerification extends AppCompatActivity {

    String otp_send = "http://mandikart.com/mapi/cartbuyer/send_otp/";
    String phone_number = "";
    String result = "";
    String[] spinnerValues = {"Gurgaon", "Faridabad", "Mewat", "Rewari", "Mahendergarh", "Alwar"};
    private Spinner spinner;
    ProgressDialog dialog;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification);

        dialog = new ProgressDialog(PhoneVerification.this);

        Button button = (Button) findViewById(R.id.buttonProceed);
        final EditText phoneNumber = (EditText) findViewById(R.id.phone_number);
        spinner = (Spinner) findViewById(R.id.spinnerCity);
        spinner.setAdapter(new ArrayAdapter<String>(PhoneVerification.this, android.R.layout.simple_spinner_dropdown_item, spinnerValues));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkConnection connection = new NetworkConnection(PhoneVerification.this);
                if (connection.isConnected()) {
                    if (phoneNumber.getText().toString().equals("")) {
                        Toast.makeText(PhoneVerification.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                    } else {
                        if (phoneNumber.getText().length() != 10) {
                            Toast.makeText(PhoneVerification.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                        } else {
                            phone_number = phoneNumber.getText().toString();
                            OTPSendAndVerifiy verify = new OTPSendAndVerifiy();
                            verify.execute(otp_send + phone_number);
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PhoneVerification.this);
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

    public class OTPSendAndVerifiy extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(post);
                result = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.toLowerCase().equals("\nsuccess")) {
                Intent intent = new Intent(PhoneVerification.this, OtpVerification.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone_number", phone_number);
                bundle.putString("user_city", city);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.animator.activity_open_scale, R.animator.activity_open_scale);
                dialog.dismiss();
            }
        }
    }
}
