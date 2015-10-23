package com.mandikart.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class CheckOutActivity extends AppCompatActivity {

    SharedPreferences pref_personal;
    SharedPreferences.Editor editor_personal;
    Boolean payment= false;
    Boolean shipment = false;
    Toolbar toolbar;

    Button proceedCheckOut;
    RadioButton cod,chk,express,standard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        cod = (RadioButton) findViewById(R.id.cod);
        chk = (RadioButton) findViewById(R.id.chk);
        express= (RadioButton) findViewById(R.id.express);
        standard = (RadioButton) findViewById(R.id.standard);

        toolbar = (Toolbar) findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        paymentClick(cod); paymentClick(chk);
        shipClick(express); shipClick(standard);

        pref_personal= getSharedPreferences("PERSONAL_DETAIL", Context.MODE_PRIVATE);
        editor_personal= pref_personal.edit();
        proceedCheckOut = (Button) findViewById(R.id.checkout_proceed);
        proceedCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment && shipment){
                    Intent i = new Intent(CheckOutActivity.this,RegistrationForm.class);
                    CheckOutActivity.this.finish();
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Kindly select payment method and shipping type to proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void paymentClick(View v){
        Boolean checked= ((RadioButton) v).isChecked();
        if(checked) payment= true; else payment= false;
        switch (v.getId()){
            case R.id.cod :
                if(checked){
                    editor_personal.putString("payment_option","cod");
                    editor_personal.commit();
                }
                break;
            case R.id.chk :
                if(checked){
                    editor_personal.putString("payment_option","chk");
                    editor_personal.commit();
                }
                break;
        }
    }

    public void shipClick(View v){
        Boolean checked= ((RadioButton) v).isChecked();
        if(checked) shipment= true; else shipment= false;
        switch (v.getId()){
            case R.id.express :
                if(checked){
                    editor_personal.putString("delivery_mode","express");
                    editor_personal.commit();
                }
                break;
            case R.id.standard :
                if(checked){
                    editor_personal.putString("delivery_mode","standard");
                    editor_personal.commit();
                }
                break;
        }
    }
}
