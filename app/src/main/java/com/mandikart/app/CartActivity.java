package com.mandikart.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {


    private CartListAdapter adapter;
    private ListView cartItemList;
    LinearLayout checkOut;
    Toolbar bar;
    DatabaseHelper helper;
    Cursor cursor;


    RelativeLayout visiblePart;
    LinearLayout invisiblePart;
    TextView totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartItemList = (ListView) findViewById(R.id.cartlist);
        visiblePart = (RelativeLayout) findViewById(R.id.visiblePart);
        invisiblePart = (LinearLayout) findViewById(R.id.invisiblePart);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        helper = new DatabaseHelper(this);
        helper.open();
        cursor = helper.getData("shop_cart");
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            invisiblePart.setVisibility(View.VISIBLE);
            visiblePart.setVisibility(View.INVISIBLE);
        } else {
            visiblePart.setVisibility(View.VISIBLE);
            invisiblePart.setVisibility(View.INVISIBLE);
        }
        adapter = new CartListAdapter(CartActivity.this, totalPrice);
        bar = (Toolbar) findViewById(R.id.bar);
        setSupportActionBar(bar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cartItemList.setAdapter(adapter);

        checkOut = (LinearLayout) findViewById(R.id.checkout);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, CheckOutActivity.class);
                startActivity(i);
            }
        });
    }

    public void startShopping(View v) {
        CartActivity.this.finish();
        Intent i = new Intent(CartActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
