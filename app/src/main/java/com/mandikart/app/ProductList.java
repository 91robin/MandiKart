package com.mandikart.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import models.ProductModel;

public class ProductList extends AppCompatActivity {

    SharedPreferences pref;
    String device_Id;
    String device_Token;
    String type_id;
    String type_name;
    String type;
    String JsonString;
    ListView productList;
    String url;
    ProductListAdapter productListAdapter;
    private Toolbar toolbar;
    List<ProductModel> productModel;
    private ProgressDialog dialog;
    Intent i;
    Boolean status;
    NetworkConnection networkConnection;
    private LinearLayout errorView;
    private LinearLayout noproduct;
    Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        NetworkConnection connection = new NetworkConnection(ProductList.this);
        if (connection.isConnected()) {
            dialog = new ProgressDialog(this);
            productList = (ListView) findViewById(R.id.productList);
            productList.setVisibility(View.INVISIBLE);
            networkConnection = new NetworkConnection(ProductList.this);
            errorView = (LinearLayout) findViewById(R.id.errorView);
            noproduct = (LinearLayout) findViewById(R.id.noproduct);
            retry = (Button) findViewById(R.id.retry);
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
            pref = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
            i = getIntent();
            type_id = i.getStringExtra("type_id");
            type_name = i.getStringExtra("type_name");
            type = i.getStringExtra("type");
            device_Id = pref.getString("device_id", "");
            device_Token = pref.getString("device_token", "");

            DoTask task = new DoTask();
            task.execute();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductList.this);
            builder.setTitle("Network Connection Error");
            builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ProductList.this.finish();
                }
            });
            builder.show();

        }

    }

    public void cart(View v) {
        Intent i = new Intent(ProductList.this, CartActivity.class);
        startActivity(i);
    }

    public class DoTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
            url = "http://mandikart.com/mapi/search/search_product";

        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                status = networkConnection.isConnected();
                if (status) {
                    JsonUtil util = new JsonUtil();
                    String putJson = util.makeJson(device_Id, device_Token, type_id, type_name, type);
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    StringEntity entity = new StringEntity(putJson);
                    post.setEntity(entity);
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-type", "application/json");
                    HttpResponse httpResponse = client.execute(post);
                    JsonString = EntityUtils.toString(httpResponse.getEntity());
                    if (JsonString.equals("ERROR")) {
                        System.out.println(JsonString);
                        return null;
                    } else {
                        productModel = GetJsonData.getProducts(JsonString);
                    }
                } else {
                    return null;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            if (!status) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductList.this);
                dialog.setMessage("Please! Make sure your Network is connected.");
                dialog.setTitle("Attention");
                dialog.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                errorView.setVisibility(View.VISIBLE);
            } else {
                if (productModel.size() < 1) {
                    noproduct.setVisibility(View.VISIBLE);
                    productList.setVisibility(View.INVISIBLE);
                } else {
                    productListAdapter = new ProductListAdapter(productModel, ProductList.this);
                    productList.setAdapter(productListAdapter);
                    productList.setVisibility(View.VISIBLE);
                    noproduct.setVisibility(View.INVISIBLE);
                }
            }
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
                Intent i = new Intent(ProductList.this, CartActivity.class);
                startActivity(i);
            }
            return true;
            case R.id.action_search: {
                Intent i = new Intent(ProductList.this, SearchActivity.class);
                startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}