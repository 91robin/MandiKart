package com.mandikart.app;

import java.util.ArrayList;
import java.util.Locale;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    ListView list;
    ListViewAdapter adapter;
    EditText editsearch;
    ArrayList<SearchProduct> arraylist = new ArrayList<SearchProduct>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        helper.open();
        Cursor cursor = helper.getData("search");
        cursor.moveToFirst();

        list = (ListView) findViewById(R.id.searchResults);

        for (int i = 0; i < cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            SearchProduct sp = new SearchProduct(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("name")), cursor.getString(cursor.getColumnIndex("brand_name")), cursor.getString(cursor.getColumnIndex("category_id")), cursor.getString(cursor.getColumnIndex("type")));
            arraylist.add(sp);
        }

        adapter = new ListViewAdapter(this, arraylist);

        list.setAdapter(adapter);
        list.setVisibility(View.INVISIBLE);

        editsearch = (EditText) findViewById(R.id.searchText);

        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                if(editsearch.getText().toString().length() >= 2) {
                    list.setVisibility(View.VISIBLE);
                    String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
                } else {
                    list.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }
        });
    }

}
