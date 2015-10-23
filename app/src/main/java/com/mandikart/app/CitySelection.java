package com.mandikart.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CitySelection extends AppCompatActivity {

    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_selection);

        Bundle bundle = getIntent().getExtras();
        phone_number = bundle.getString("phone_number");


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Button proceed = (Button) findViewById(R.id.buttonProceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CitySelection.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String[] spinnerValues = {"Gurgaon", "Faridabad", "Mewat", "Rewari", "Mahendergarh", "Alwer"};

        MyAdapter adapter = new MyAdapter(CitySelection.this, R.layout.custom_spinner_phnone, spinnerValues);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class MyAdapter extends ArrayAdapter<String> {

        String[] cityNames;

        public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
            cityNames = objects;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.custom_spinner_phnone, parent, false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.cityName);
            main_text.setText(cityNames[position]);

            return mySpinner;
        }
    }
}
