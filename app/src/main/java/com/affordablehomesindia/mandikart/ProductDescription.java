package com.affordablehomesindia.mandikart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import models.ProductDescriptionModel;

public class ProductDescription extends AppCompatActivity {

    TextView ProductNameText;
    TextView ProductDescriptionText;
    TextView ProductBarndNameText;
    TextView MrpPriceText;
    TextView MandiPriceText;
    Spinner packSizeSpinner;

    String ProductName;
    String ProductDescription;
    String ProductBrandName;
    String PrimaryImageUrl;
    String MrpPrice;
    String MandiPrice;
    String UnitPackSize;
    String ShipperPackSize;
    String UnitName;
    String[] PackSize;
    String Id;
    String JsonUrl = "http://mandikart.com/mapi/search/product/";

    ImageView ProductImage;

    Intent i;

    ProgressDialog dialog;

    ProductDescriptionModel productDescriptionModel;

    String JsonResult;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        toolbar = (Toolbar) findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dialog = new ProgressDialog(this);
        ProductNameText = (TextView) findViewById(R.id.productTitle);
        ProductBarndNameText = (TextView) findViewById(R.id.productBrand);
        packSizeSpinner = (Spinner) findViewById(R.id.packSizeSpinner);
        MrpPriceText = (TextView) findViewById(R.id.productMrp);
        MandiPriceText = (TextView) findViewById(R.id.productMandiPrice);
        ProductDescriptionText = (TextView) findViewById(R.id.productDescription);
        ProductImage = (ImageView) findViewById(R.id.productImage);

        DoTask doTask = new DoTask();
        doTask.execute();


    }

    public class DoTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                i = getIntent();
                String id = i.getStringExtra("Id");
                JsonUrl = JsonUrl + id + "/";
                URL url = new URL(JsonUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JsonResult = buffer.toString();
                productDescriptionModel = GetJsonData.getProductDescription(JsonResult);
                ProductName = productDescriptionModel.getProductName();
                ProductBrandName = productDescriptionModel.getBrandName();
                ProductDescription = productDescriptionModel.getProductDescription();
                MrpPrice = productDescriptionModel.getMrp();
                MandiPrice = productDescriptionModel.getPrice();
                PrimaryImageUrl = productDescriptionModel.getPrimaryImageUrl();
                UnitPackSize = productDescriptionModel.getUnitPackSize();
                ShipperPackSize = productDescriptionModel.getShiperPackSize();
                UnitName = productDescriptionModel.getUnitName();
                PackSize = new String[2];
                PackSize[0] = UnitPackSize + "  " + UnitName;
                PackSize[1] = ShipperPackSize + "  " + UnitPackSize;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            ProductBarndNameText.setText(ProductBrandName);
            ProductNameText.setText(ProductName);
            MrpPriceText.setText("mrp : Rs." + MrpPrice);
            MrpPriceText.setPaintFlags(MandiPriceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            MandiPriceText.setText("Mandi Price: Rs." + MandiPrice);
            ProductDescriptionText.setText(ProductDescription);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Intent i = new Intent(ProductDescription.this, CartActivity.class);
                startActivity(i);
            }
            return true;
            case R.id.action_search: {
                Intent i = new Intent(ProductDescription.this, SearchActivity.class);
                startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
