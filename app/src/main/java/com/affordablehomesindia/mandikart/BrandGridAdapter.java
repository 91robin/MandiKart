package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import models.BrandModel;
import models.CategoryModel;

public class BrandGridAdapter extends BaseAdapter {

    private Context context;
    int no;
    List<BrandModel> brandModel;

    public BrandGridAdapter (Context context, int no, List<BrandModel> brandModel){
        this.brandModel=brandModel;
        this.context=context;
        this.no=no;
    }

    @Override
    public int getCount() {
        return no;
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
        if(convertView==null){
            LayoutInflater layoutInflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.brand_item_view,null);
        }
        TextView view = (TextView) convertView.findViewById(R.id.brandName);
        view.setText(brandModel.get(position).getName());
        return convertView;
    }
}
