package com.mandikart.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<SearchProduct> productlist = null;
    private ArrayList<SearchProduct> arraylist;

    public ListViewAdapter(Context context, List<SearchProduct> productlist) {
        mContext = context;
        this.productlist = productlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchProduct>();
        this.arraylist.addAll(productlist);
    }

    public class ViewHolder {
        TextView id;
        TextView name;
        TextView brand_name;
        TextView category_id;
    }

    @Override
    public int getCount() {
        return productlist.size();
    }

    @Override
    public SearchProduct getItem(int position) {
        return productlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.brand_name = (TextView) view.findViewById(R.id.brand_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(productlist.get(position).getName());
        if (productlist.get(position).getBrandName().equals("")) {
            holder.brand_name.setText("");
        } else {
            holder.brand_name.setText(" by " + productlist.get(position).getBrandName());
        }

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (productlist.get(position).getType().toString().equals("product")) {
                    Intent intent = new Intent(mContext, SingleItemView.class);
                    intent.putExtra("id", (productlist.get(position).getId()));
                    intent.putExtra("name", (productlist.get(position).getName()));
                    mContext.startActivity(intent);
                }

                if (productlist.get(position).getType().toString().equals("sub_category")) {
                    Intent intent = new Intent(mContext, ProductList.class);
                    intent.putExtra("type_id", productlist.get(position).getId());
                    intent.putExtra("type", productlist.get(position).getType());
                    intent.putExtra("type_name", productlist.get(position).getName());
                    mContext.startActivity(intent);
                }

                if (productlist.get(position).getType().toString().equals("brand")) {
                    Intent intent = new Intent(mContext, ProductList.class);
                    intent.putExtra("type_id", productlist.get(position).getId());
                    intent.putExtra("type", productlist.get(position).getType());
                    intent.putExtra("type_name", productlist.get(position).getName());
                    mContext.startActivity(intent);
                }


            }
        });

        return view;
    }

    // Filter Class

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productlist.clear();
        if (charText.length() == 0) {
            productlist.addAll(arraylist);
        } else {
            for (SearchProduct wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
