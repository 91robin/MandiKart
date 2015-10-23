package com.mandikart.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.CheapProductModel;

public class MainActivity extends AppCompatActivity {


    private NavigationDrawerFragment navigationDrawerFragment;
    private Toolbar toolbar;
    ProgressDialog dialog;
    RelativeLayout brand, category, farming, agriculture, cheapProducts;
    ListView cheapProductList;

    EditText searchText;
    List<CheapProductModel> cheapList;
    CheapProductModel model;
    String cheapProductUrl = "http://mandikart.com/mapi/rawdata/today_cheap_products/";
    private String cheapProductJson;
    ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkConnection connection = new NetworkConnection(MainActivity.this);
        if (connection.isConnected()) {

            cheapList = new ArrayList<>();
            view = (ListView) findViewById(R.id.cheapProductsList);
            searchText = (EditText) findViewById(R.id.searchText);
            searchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);
                }
            });
            // mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
            //mainLayout.setVisibility(View.INVISIBLE);
            brand = (RelativeLayout) findViewById(R.id.brandTile);
            category = (RelativeLayout) findViewById(R.id.categoryTile);
            agriculture = (RelativeLayout) findViewById(R.id.agriTile);
            farming = (RelativeLayout) findViewById(R.id.farmingTile);
            cheapProducts = (RelativeLayout) findViewById(R.id.cheapProducts);
            dialog = new ProgressDialog(this);
            toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.mainFragment);
            DrawerLayout mainLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
            navigationDrawerFragment.setUp(mainLayout, toolbar);
            CheapProductFetching fetching = new CheapProductFetching();
            fetching.execute(cheapProductUrl);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Network Connection Error");
            builder.setMessage("This app requires an internet connection. Make sure you are connected to a wifi network or have switched to your network data.");
            builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                }
            });
            builder.show();

        }

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("type", "brand");
                startActivity(i);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("type", "grocery");
                startActivity(i);
            }
        });

        agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("type", "agri");
                i.putExtra("id", 3);
                startActivity(i);
            }
        });

        farming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CategoryActivity.class);
                i.putExtra("type", "farming");
                i.putExtra("id", 4);
                startActivity(i);
            }
        });

    }

    public class CheapProductFetching extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(params[0]);

            HttpResponse httpResponse = null;
            try {
                httpResponse = client.execute(post);
                cheapProductJson = EntityUtils.toString(httpResponse.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return cheapProductJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    model = new CheapProductModel();
                    model.setId(object.getString("id"));
                    model.setName(object.getString("name"));
                    model.setPrice(object.getString("price"));
                    model.setCheapPrice(object.getString("cheap_price"));
                    cheapList.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            CheapListAdapter adapter = new CheapListAdapter(MainActivity.this, cheapList);
            view.setAdapter(adapter);
            dialog.dismiss();
        }
    }

    public class CheapListAdapter extends BaseAdapter {

        List<CheapProductModel> list;
        Context context;

        public CheapListAdapter(Context applicationContext, List<CheapProductModel> cheapList) {
            this.context = applicationContext;
            this.list = cheapList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cheap_product_item, null);
            TextView name = (TextView) view.findViewById(R.id.cheap_product_detail);
            TextView price = (TextView) view.findViewById(R.id.price);
            TextView cheap_price = (TextView) view.findViewById(R.id.cheap_price);
            cheap_price.setText(list.get(position).getCheapPrice());
            name.setText(list.get(position).getName());
            price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price.setText(list.get(position).getPrice());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivity.this, SingleItemView.class);
                    in.putExtra("id", list.get(position).getId());
                    startActivity(in);
                }
            });
            return view;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.single_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
