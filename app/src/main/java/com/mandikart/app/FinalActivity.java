package com.mandikart.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    Button finish;
    TextView detail;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        toolbar = (Toolbar) findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalActivity.this.finish();
                Intent i = new Intent(FinalActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        Intent i = getIntent();
        String orderId= i.getStringExtra("order_no");
        String deliveryId= i.getStringExtra("delivery_id");
        String password = i.getStringExtra("password");
        String display="Order no:  "+orderId+"\nDelivery Id:  "+deliveryId;
        if(!(password.length()<1)){
            display = display + "\nPassword :  "+ password;
        }
        detail =(TextView)findViewById(R.id.detailOrder);
        detail.setText(display);
        finish =(Button)findViewById(R.id.finishButton);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalActivity.this.finish();
                Intent i = new Intent(FinalActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}