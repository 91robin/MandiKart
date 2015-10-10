package com.affordablehomesindia.mandikart;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    boolean isAboveJellyBean = false;
    int statusHeight;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    TextView app_bar_name;
    int[] ids;
    CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAboveJellyBean = true;
        }

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/djb_custom_bold.ttf");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        app_bar_name = (TextView) findViewById(R.id.app_bar_name);
        app_bar_name.setTypeface(custom_font);

        statusHeight = (int) getResources().getDimension(R.dimen.statusHeight);
        LinearLayout statusColor = (LinearLayout) findViewById(R.id.statusColor);
        LinearLayout topColor = new LinearLayout(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight);
        topColor.setLayoutParams(layoutParams);
        topColor.setBackgroundColor(Color.parseColor("#0277BD"));

        if (isAboveJellyBean) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            statusColor.addView(topColor);
        }

        NavigationDrawerActivity drawerActivity = (NavigationDrawerActivity) getSupportFragmentManager().findFragmentById(R.id.fragment);
        drawerLayout = (DrawerLayout) findViewById(R.id.mainDrawer);
        drawerActivity.setUp(drawerLayout, toolbar, fab);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        AdAdapter adapter = new AdAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        HorizontalListView categoryView = (HorizontalListView) findViewById(R.id.category);
        prepareCategory();
        categoryAdapter = new CategoryAdapter(this,ids);
        categoryView.setAdapter(categoryAdapter);
        categoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
                i.putExtra("position", position);
                startActivity(i);
            }
        });
    }

    private void prepareCategory() {

        ids = new int[]{R.drawable.cart_active,
                R.drawable.cart_active,
                R.drawable.cart_active,
                R.drawable.cart_active,
                R.drawable.cart_active};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.search) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
