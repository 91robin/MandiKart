package com.affordablehomesindia.mandikart;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Arrays;
import java.util.List;

import models.ProductModel;

public class ProductListAdapter extends BaseAdapter {

    List<ProductModel> productModel;
    Context context;
    String[] packArray;
    String[] pack;
    String pack_size;
    DatabaseHelper databaseHelper;
    ContentValues values;
    float packSize;
    float unit;
    String image_url;

    Boolean status[];


    public ProductListAdapter(List<ProductModel> productModel, Context context) {
        this.productModel = productModel;
        this.context = context;
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
        databaseHelper = new DatabaseHelper(context);
        databaseHelper.open();
        pack_size = "unit";
        status = new Boolean[productModel.size()];
        Arrays.fill(status, false);
        values = new ContentValues();
    }

    @Override
    public int getCount() {
        return productModel.size();
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_list_item_view, null);
        }
        final Cursor cursor = databaseHelper.getSelect(productModel.get(position).getId());
        packArray = new String[2];
        pack = new String[2];
        pack[0] = productModel.get(position).getUnit_pack_size();
        pack[1] = productModel.get(position).getShipper_pack_size();
        packArray[0] = "Unit Pack Size - " + pack[0] + " " + productModel.get(position).getUnit_name() + " - ₹" + productModel.get(position).getPrice();
        try {
            packArray[1] = "Shipper Pack Size - " + pack[1] + " Unit" + " - ₹" + Float.parseFloat(productModel.get(position).getPrice()) * Float.parseFloat(pack[1]);
        } catch (Exception e) {
            packArray[1] = "Shipper Pack Size - " + pack[1] + " Unit";
        }
        image_url = productModel.get(position).getPrimary_image();
        ImageView productImage = (ImageView) convertView.findViewById(R.id.product_image);
        final Button addButton = (Button) convertView.findViewById(R.id.addButton);
        final Button addedButton = (Button) convertView.findViewById(R.id.addedButton);
        final TextView productName = (TextView) convertView.findViewById(R.id.product_title);
        TextView productBrand = (TextView) convertView.findViewById(R.id.product_brand);
        final TextView mrp = (TextView) convertView.findViewById(R.id.mrp);
        final TextView mandiPrice = (TextView) convertView.findViewById(R.id.mandi_price);
        Button increament = (Button) convertView.findViewById(R.id.increment);
        Button decreament = (Button) convertView.findViewById(R.id.decrement);
        final EditText quantityText = (EditText) convertView.findViewById(R.id.quantity);
        final Spinner spinner = (Spinner) convertView.findViewById(R.id.packSizeSpinner);
        ImageLoader.getInstance().displayImage(image_url, productImage);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(context, R.layout.custom_spinner, packArray);
        spinner.setAdapter(adapter);
        packSize = Float.valueOf(productModel.get(position).getUnit_pack_size().toString());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    unit = 1;
                    pack_size = "unit";
                    packSize = Float.valueOf(productModel.get(position).getUnit_pack_size().toString());

                } else if (pos == 1) {
                    pack_size = "shipper";
                    packSize = Float.valueOf(productModel.get(position).getShipper_pack_size().toString());
                    unit = packSize;
                            }
                addButton.setVisibility(View.VISIBLE);
                addedButton.setVisibility(View.INVISIBLE);
                status[position] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (spinner.getSelectedItemPosition() == 1) {
            unit = Float.valueOf(productModel.get(1).getShipper_pack_size().toString());
        } else {
            unit = 1;
        }
        if (cursor.getCount() > 0) {
            status[position] = true;
        } else {
            status[position] = false;
        }
        if (status[position]) {
            cursor.moveToFirst();
            quantityText.setText("" + cursor.getString(cursor.getColumnIndex("quantity")));
            addedButton.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            try {
                mrp.setText("₹ " + Float.valueOf(productModel.get(position).getMrp()) * cursor.getFloat(cursor.getColumnIndex("quantity")));
                mandiPrice.setText("MandiKart: ₹ " + Float.valueOf(productModel.get(position).getPrice()) * cursor.getFloat(cursor.getColumnIndex("quantity")));
            } catch (Exception e) {
            }
        } else {
            quantityText.setText("" + 0);
            mrp.setText("₹ " + productModel.get(position).getMrp());
            mandiPrice.setText("MandiKart: ₹ " + productModel.get(position).getPrice());
            addedButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.VISIBLE);
        }

        productName.setText(productModel.get(position).getName());
        productBrand.setText(productModel.get(position).getBrand_name());
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SingleItemView.class);
                context.startActivity(i);
            }
        });
        increament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float i = Float.valueOf(quantityText.getText().toString());
                i++;
                quantityText.setText("" + i);
                try {
                    mrp.setText("₹ " + Float.valueOf(productModel.get(position).getMrp()) * i * unit);
                    mandiPrice.setText("MandiKart: ₹ " + Float.valueOf(productModel.get(position).getPrice()) * i * unit);
                } catch (Exception e) {
                }
                status[position] = false;
                if (status[position]) {
                    addedButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                } else {
                    addedButton.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                }
            }
        });
        decreament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float i = Float.valueOf(quantityText.getText().toString());
                if (i > 0.99) {
                    i--;
                    quantityText.setText("" + i);
                }
                if (i < 0.99) {
                    i = 1;
                }
                status[position] = false;
                try {
                    mrp.setText("₹ " + Float.valueOf(productModel.get(position).getMrp()) * i * unit);
                    mandiPrice.setText("MandiKart: ₹ " + Float.valueOf(productModel.get(position).getPrice()) * i * unit);
                } catch (Exception e) {
                }
                if (status[position]) {
                    addedButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                } else {
                    addedButton.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                }

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float q = Float.valueOf(quantityText.getText().toString());
                if (q >= 1) {
                    updateValue(productModel.get(position).getId(), packSize, q, position, pack_size);
                    addButton.setVisibility(View.INVISIBLE);
                    addedButton.setVisibility(View.VISIBLE);
                    status[position] = true;
                }
            }
        });

        addedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status[position] = false;
                deleteValue(productModel.get(position).getId());
                addButton.setVisibility(View.VISIBLE);
                addedButton.setVisibility(View.INVISIBLE);
            }
        });

        if (status[position]) {
            addButton.setVisibility(View.INVISIBLE);
            addedButton.setVisibility(View.VISIBLE);
        } else {
            addButton.setVisibility(View.VISIBLE);
            addedButton.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public class CustomSpinnerAdapter extends ArrayAdapter<String> {

        String[] options;
        Context ctx;

        public CustomSpinnerAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            options = objects;
            ctx = context;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_spinner, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.spi);
            textView.setText(options[position]);
            return view;
        }
    }

    public void updateValue(String productId, float packSize, float quantity, int p, String size) {
        Cursor c = databaseHelper.getSelect(productId);
        if (c.moveToFirst()) {
            databaseHelper.updateValue(Integer.parseInt(productId), packSize, quantity, size,
                    productModel.get(p).getPrimary_image(),
                    productModel.get(p).getBrand_name(),
                    productModel.get(p).getName(),
                    productModel.get(p).getUnit_name());
        } else {
            values.put("product_id", productId);
            values.put("quantity", "" + quantity);
            values.put("selected_size", "" + packSize);
            values.put("ammount", "" + productModel.get(p).getPrice());
            values.put("image", "" + productModel.get(p).getPrimary_image());
            values.put("brand", "" + productModel.get(p).getBrand_name());
            values.put("name", "" + productModel.get(p).getName());
            values.put("unit_name", "" + productModel.get(p).getUnit_name());
            values.put("pack_size", pack_size);
            databaseHelper.insertData(databaseHelper.TABLE_NAME, values);

        }
    }

    public void deleteValue(String id) {
        databaseHelper.deleteValue(id);

    }
}
