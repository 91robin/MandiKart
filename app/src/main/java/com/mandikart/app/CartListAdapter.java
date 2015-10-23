package com.mandikart.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CartListAdapter extends BaseAdapter {

    Context context;
    DatabaseHelper helper;
    Cursor cursor;

    public CartListAdapter(Context context, TextView totalPrice) {
        this.context = context;
        helper = new DatabaseHelper(context);
        helper.open();
        cursor = helper.getData("shop_cart");
        cursor.moveToFirst();
        totalPrice.setText(CalculateTotalPrice());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }


    @Override
    public int getCount() {
        return cursor.getCount();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.cart_item_view, null);
        }

        cursor.moveToPosition(position);
        ImageView productImage = (ImageView) convertView.findViewById(R.id.product_image);
        TextView productName = (TextView) convertView.findViewById(R.id.product_title);
        TextView productBrand = (TextView) convertView.findViewById(R.id.product_brand);
        final TextView packSize = (TextView) convertView.findViewById(R.id.packSize);
        final EditText quantity = (EditText) convertView.findViewById(R.id.quantity);
        final TextView price = (TextView) convertView.findViewById(R.id.mandi_price);
        Button increament = (Button) convertView.findViewById(R.id.increment);
        Button decreament = (Button) convertView.findViewById(R.id.decrement);
        String image_url=cursor.getString(cursor.getColumnIndex("image"));
        final ImageView update = (ImageView) convertView.findViewById(R.id.update);
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        productName.setText(cursor.getString(cursor.getColumnIndex("name")));
        productBrand.setText(cursor.getString(cursor.getColumnIndex("brand")));
        final String pack = cursor.getString(cursor.getColumnIndex("selected_size"));
        float quant = cursor.getFloat(cursor.getColumnIndex("quantity"));
        float pric = cursor.getFloat(cursor.getColumnIndex("ammount"));
        final String pack_type = cursor.getString(cursor.getColumnIndex("pack_size"));
        update.setVisibility(View.INVISIBLE);
        final String unit_name= cursor.getString(cursor.getColumnIndex("unit_name"));

        ImageLoader.getInstance().displayImage(image_url, productImage);


        final float q[] ={quant};
        final float p =pric;

        if(pack_type.equals("shipper")){
            price.setText("Total Price : ₹ "+p*q[0]*Float.parseFloat(pack));
            packSize.setText(Float.valueOf(pack)+" unit \n");
        }else {
            price.setText("Total Price : ₹ " + p * q[0]);
            packSize.setText(Float.valueOf(pack)+"  "+unit_name);
        }
        quantity.setText(""+quant);
        increament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q[0] = Float.valueOf(quantity.getText().toString());
                q[0]++;
                price.setText("Total Price : ₹ " + p * q[0]);
                quantity.setText(""+q[0]);
                update.setVisibility(View.VISIBLE);
            }
        });
        decreament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q[0] = Float.valueOf(quantity.getText().toString());
                if(q[0]>1){
                    q[0]--;
                    price.setText("Total Price : ₹ "+p*q[0]);
                    quantity.setText(""+q[0]);
                    update.setVisibility(View.VISIBLE);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteValue(cursor.getString(cursor.getColumnIndex("product_id")));
                ((Activity) context).finish();
                Intent i = new Intent(context,CartActivity.class);
                context.startActivity(i);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updateQuantity(q[0], cursor.getString(cursor.getColumnIndex("product_id")));
                update.setVisibility(View.INVISIBLE);
                ((Activity) context).finish();
                Intent i = new Intent(context,CartActivity.class);
                context.startActivity(i);
            }
        });
        return convertView;
    }

    public String CalculateTotalPrice(){
        String result="0";
        float total = 0;
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getString(cursor.getColumnIndex("pack_size")).equals("shipper")){
                total= total+( cursor.getFloat(cursor.getColumnIndex("ammount"))*cursor.getFloat(cursor.getColumnIndex("quantity"))*cursor.getFloat(cursor.getColumnIndex("selected_size")));
                System.out.println(total);
            }else{
                total = total+( cursor.getFloat(cursor.getColumnIndex("ammount"))*cursor.getFloat(cursor.getColumnIndex("quantity")));
                System.out.println(total);
            }
        }
        result = ""+total;
        return result;
    }
}
