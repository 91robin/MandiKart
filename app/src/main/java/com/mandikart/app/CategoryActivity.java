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
import android.widget.AdapterView;
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

import models.BrandModel;
import models.SubCategoryModel;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressDialog dialog;
    CategoryListAdapter adapter;
    String type;
    ListView listView;
    String grocery_ids[];
    String brand_ids[];
    String agri_ids[];
    String farming_ids[];
    String name[];
    SharedPreferences pref;
    String deviceId;
    String JsonResult;
    private String url;
    private SharedPreferences.Editor editor;
    List<BrandModel> brandModel = null;
    List<SubCategoryModel> subCategoryModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        NetworkConnection connection = new NetworkConnection(CategoryActivity.this);
        if (connection.isConnected()) {
            dialog = new ProgressDialog(this);
            listView = (ListView) findViewById(R.id.categorylist);
            listView.setVisibility(View.INVISIBLE);
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
            type = getIntent().getStringExtra("type");

            DoTask task = new DoTask();
            task.execute();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
            builder.setTitle("Network Connection Error");
            builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CategoryActivity.this.finish();
                }
            });
            builder.show();
        }
    }

    private class DoTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
            url = "http://mandikart.com/mapi/rawdata/rawmetaproduct/";
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                pref = getSharedPreferences("USER_DETAIL", Context.MODE_PRIVATE);
                editor = pref.edit();
                deviceId = pref.getString("device_id", "");
                JsonUtil util = new JsonUtil();
                String putJson = util.makeJson(deviceId);
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                StringEntity entity = new StringEntity(putJson);
                post.setEntity(entity);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = client.execute(post);
                JsonResult = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(JsonResult);

                if (type.equals("grocery")) {
                    subCategoryModel = GetJsonData.getSubcategory(JsonResult);
                    int value = 0;
                    for (int i = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("2")) {
                            value++;
                        }
                    }
                    name = new String[value];
                    grocery_ids = new String[value];
                    for (int i = 0, e = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("2")) {
                            name[e] = subCategoryModel.get(i).getName();
                            grocery_ids[e] = subCategoryModel.get(i).getId();
                            e++;
                        }
                    }
                } else if (type.equals("agri")) {
                    subCategoryModel = GetJsonData.getSubcategory(JsonResult);
                    int value = 0;
                    for (int i = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("3")) {
                            value++;
                        }
                    }
                    name = new String[value];
                    agri_ids = new String[value];
                    for (int i = 0, e = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("3")) {
                            name[e] = subCategoryModel.get(i).getName();
                            agri_ids[e] = subCategoryModel.get(i).getId();
                            e++;
                        }
                    }
                } else if (type.equals("farming")) {
                    subCategoryModel = GetJsonData.getSubcategory(JsonResult);
                    int value = 0;
                    for (int i = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("4")) {
                            value++;
                        }
                    }
                    name = new String[value];
                    farming_ids = new String[value];
                    for (int i = 0, e = 0; i < subCategoryModel.size(); i++) {
                        if (subCategoryModel.get(i).getCategoryId().equals("4")) {
                            name[e] = subCategoryModel.get(i).getName();
                            farming_ids[e] = subCategoryModel.get(i).getId();
                            e++;
                        }
                    }
                } else {
                    brandModel = GetJsonData.getBrand(JsonResult);
                    name = new String[brandModel.size()];
                    brand_ids = new String[brandModel.size()];
                    for (int i = 0; i < brandModel.size(); i++) {
                        name[i] = brandModel.get(i).getName();
                        brand_ids[i] = brandModel.get(i).getId();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dialog.dismiss();
            listView.setAdapter(new CategoryListAdapter(CategoryActivity.this, name));
            listView.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(CategoryActivity.this, ProductList.class);
                    if (type.equals("grocery")) {
                        i.putExtra("type", "sub_category");
                        i.putExtra("type_id", grocery_ids[position]);
                        i.putExtra("type_name", "");
                        System.out.println(subCategoryModel.get(position).getId());
                    } else if (type.equals("agri")) {
                        i.putExtra("type", "sub_category");
                        i.putExtra("type_id", agri_ids[position]);
                        i.putExtra("type_name", "");
                        System.out.println(subCategoryModel.get(position).getId());
                    } else if (type.equals("farming")) {
                        i.putExtra("type", "sub_category");
                        i.putExtra("type_id", farming_ids[position]);
                        i.putExtra("type_name", "");
                        System.out.println(subCategoryModel.get(position).getId());
                    } else {
                        i.putExtra("type", "brand");
                        i.putExtra("type_id", brandModel.get(position).getId());
                        i.putExtra("type_name", brandModel.get(position).getName());
                    }
                    startActivity(i);
                }
            });
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
                Intent i = new Intent(CategoryActivity.this, CartActivity.class);
                startActivity(i);
            }
            return true;
            case R.id.action_search: {
                Intent i = new Intent(CategoryActivity.this, SearchActivity.class);
                startActivity(i);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
