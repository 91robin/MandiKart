package com.mandikart.app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SingleItemView extends AppCompatActivity {

    String name;
    String id;
    String producname;
    String pack_size = "unit";
    String brandName;
    String unitname;
    String finalJson = "";
    String unit = "";
    String image_url = "";
    ProgressDialog progressDialog;
    String description_url = "http://mandikart.com/mapi/search/product/";
    String final_description_url = "";
    TextView product_name;
    TextView product_brand;
    TextView our_price;
    TextView real_price;
    TextView product_description;
    ImageView product_image;
    ImageView backImage;
    String[] pack_sizes = new String[2];
    RadioButton shipperRadio,unitRadio;
    String price;
    String mrp;
    Button add, added, increament, decreament;
    EditText quantity;
    DatabaseHelper helper;
    private RelativeLayout ll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);
        ll = (RelativeLayout) findViewById(R.id.mainLayout);
        ll.setVisibility(View.INVISIBLE);

        helper = new DatabaseHelper(SingleItemView.this);
        helper.open();


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);


        Intent i = getIntent();
        id = i.getStringExtra("id");

        final_description_url = description_url + id;
        product_name = (TextView) findViewById(R.id.product_name);
        product_brand = (TextView) findViewById(R.id.product_brand);
        our_price = (TextView) findViewById(R.id.our_price);
        real_price = (TextView) findViewById(R.id.real_price);
        product_description = (TextView) findViewById(R.id.product_description);
        product_image = (ImageView) findViewById(R.id.product_primary_image);
        backImage = (ImageView) findViewById(R.id.backImage);
        add = (Button) findViewById(R.id.addButton);
        added = (Button) findViewById(R.id.addedButton);
        increament = (Button) findViewById(R.id.increment);
        decreament = (Button) findViewById(R.id.decrement);
        quantity = (EditText) findViewById(R.id.quantity);
        unitRadio=(RadioButton)findViewById(R.id.unit);
        shipperRadio = (RadioButton)findViewById(R.id.shipper);
        DescriptionLoader loader = new DescriptionLoader();
        loader.execute(final_description_url);
        quantity.setText("" + 0);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float q = Float.valueOf(quantity.getText().toString());
                if (q >= 1) {
                    add.setVisibility(View.INVISIBLE);
                    added.setVisibility(View.VISIBLE);
                    try {
                        updateValue(id, Float.valueOf(pack_sizes[1]), Float.valueOf(quantity.getText().toString()), pack_size);
                    }catch (Exception e){
                        Toast.makeText(SingleItemView.this,e.toString()+ "  "+ pack_sizes[1],Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteValue(id);
                add.setVisibility(View.VISIBLE);
                added.setVisibility(View.INVISIBLE);
            }
        });
        increament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float i = Float.valueOf(quantity.getText().toString());
                i++;
                quantity.setText("" + i);
                try {
                    real_price.setText("₹ " + Float.valueOf(mrp) * i);
                    our_price.setText("MandiKart: ₹ " + Float.valueOf(price) * i);
                } catch (Exception e) {
                }
                added.setVisibility(View.INVISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        });
        decreament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float i = Float.valueOf(quantity.getText().toString());
                if (i > 0.99) {
                    i--;
                    quantity.setText("" + i);
                }
                if (i < 0.99) {
                    i = 1;
                }
                try {
                    real_price.setText("₹ " + Float.valueOf(mrp) * i);
                    our_price.setText("MandiKart: ₹ " + Float.valueOf(price) * i);
                } catch (Exception e) {
                }
                added.setVisibility(View.INVISIBLE);
                add.setVisibility(View.VISIBLE);
            }
        });


    }

    public class DescriptionLoader extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SingleItemView.this);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(post);
                finalJson = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return finalJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                image_url = object.getString("primary_image");
                producname = object.getString("name");
                brandName = object.getString("brand_name");
                unitname = object.getString("unit_name");
                price = object.getString("price");
                mrp = object.getString("mrp");
                product_name.setText(producname);
                product_brand.setText(brandName);
                product_description.setText(object.getString("description"));
                our_price.setText("MandiKart Price : " + price);
                real_price.setText("MRP : " + mrp);
                real_price.setPaintFlags(real_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                pack_sizes[0] = object.getString("unit_pack_size");
                pack_sizes[1] = object.getString("shipper_pack_size");
                unit = object.getString("unit_name");
                unitRadio.setChecked(true);
                unitRadio.setText("Unit Pack Size - " + pack_sizes[0] + " " + unit + " - ₹" + price);
                try{
                    shipperRadio.setText("Shipper Pack Size - " + pack_sizes[1] + " Unit" + " - ₹" + Float.parseFloat(price) * Float.parseFloat(pack_sizes[1]));
                }catch(Exception e){
                    shipperRadio.setText("Shipper Pack Size - " + pack_sizes[1] + " Unit" + " - ₹");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageLoader.getInstance().displayImage(image_url, product_image);
            ImageLoader.getInstance().displayImage(image_url, backImage);
            ll.setVisibility(View.VISIBLE);
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
                Intent i = new Intent(SingleItemView.this, CartActivity.class);
                startActivity(i);
            }
            return true;
            case R.id.action_search: {
                Intent i = new Intent(SingleItemView.this, SearchActivity.class);
                startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateValue(String productId, float packSize, float quantity, String size) {
        ContentValues values = new ContentValues();
        Cursor c = helper.getSelect(productId);
        if (c.moveToFirst()) {
            helper.updateValue(Integer.parseInt(productId), packSize, quantity, size,
                    image_url, brandName, producname, unitname);
        } else {
            values.put("product_id", productId);
            values.put("quantity", "" + quantity);
            values.put("selected_size", "" + packSize);
            values.put("ammount", "" + price);
            values.put("image", "" + image_url);
            values.put("brand", "" + brandName);
            values.put("name", "" + producname);
            values.put("unit_name", "" + unitname);
            values.put("pack_size", pack_size);
            helper.insertData(helper.TABLE_NAME, values);

        }
    }

    public void deleteValue(String id) {
        helper.deleteValue(id);

    }

    public void packsize(View v) {
        Boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.unit:
                if (checked) {
                    added.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.VISIBLE);
                    pack_size = "unit";
                }
                break;
            case R.id.shipper:
                if (checked) {
                    added.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.VISIBLE);
                    pack_size = "shipper";
                }
                break;
        }
    }


}