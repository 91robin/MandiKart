package com.affordablehomesindia.mandikart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    CategoriesMainAdapter listAdapter;
    AnimatedExpandableListView expListView;
    List<String> listDataHeader;
    Toolbar toolbar;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        expListView = (AnimatedExpandableListView) findViewById(R.id.main_categories);
        Intent i = getIntent();
        int pos = i.getIntExtra("position", 0);
        prepareListData();
        listAdapter = new CategoriesMainAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(pos, true);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return false;
            }
        });
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent i= new Intent(CategoriesActivity.this,ProductList.class);
                startActivity(i);
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Fruits & Vegetables");
        listDataHeader.add("Grocery & Staples");
        listDataHeader.add("Bread Dairy & Eggs");
        listDataHeader.add("Beverages");
        listDataHeader.add("Branded Foods");


        List<String> fruits = new ArrayList<String>();
        fruits.add("Cut Fruits & Vegetables");
        fruits.add("Fresho Ayurveda");
        fruits.add("F&V Combo");
        fruits.add("Organic F&V");
        fruits.add("Fruits");
        fruits.add("Vegetables");

        List<String> grocery = new ArrayList<String>();
        grocery.add("Ayurvedic");
        grocery.add("Dals & Pulses");
        grocery.add("Dry Fruits");
        grocery.add("Edible Oils & Ghee");
        grocery.add("Sooji & Flours");
        grocery.add("Masalas & Spices");
        grocery.add("Organic Staples");
        grocery.add("Rice & Products");
        grocery.add("Salt, Sugar & Jaggery");

        List<String> dairy = new ArrayList<String>();
        dairy.add("Bread & Bakery");
        dairy.add("Dairy Products");
        dairy.add("Eggs");
        dairy.add("Fresh Food & Meals");

        List<String> beverages = new ArrayList<String>();
        beverages.add("Ayurvedic");
        beverages.add("Energy & Health Drinks");
        beverages.add("Fruit Drinks & Juices");
        beverages.add("Mineral Water");
        beverages.add("Organic Beverages");
        beverages.add("Soft Drinks");
        beverages.add("Tea & Coffee");

        List<String> brandFood = new ArrayList<String>();
        brandFood.add("Ayurvedic Food");
        brandFood.add("Baby Food");
        brandFood.add("Baking & Dessert Items ");
        brandFood.add("Biscuits");
        brandFood.add("Breakfast Cereals ");
        brandFood.add("Canned Food ");
        brandFood.add("Chocolates & Sweets ");
        brandFood.add("Health & Nutrition");

        listDataChild.put(listDataHeader.get(0), fruits);
        listDataChild.put(listDataHeader.get(1), grocery);
        listDataChild.put(listDataHeader.get(2), dairy);
        listDataChild.put(listDataHeader.get(3), beverages);
        listDataChild.put(listDataHeader.get(4), brandFood);
    }
}