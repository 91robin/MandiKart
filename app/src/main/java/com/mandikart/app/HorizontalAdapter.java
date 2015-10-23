package com.mandikart.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import models.CategoryModel;

public class HorizontalAdapter extends BaseAdapter {

    private Context context;
    int no;
    List<CategoryModel> categoryModel;

    public HorizontalAdapter(Context context, int no, List<CategoryModel> categoryModel){
        this.categoryModel=categoryModel;
        this.context=context;
        this.no=no;
        System.out.println("In Contructor");
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
            convertView = layoutInflater.inflate(R.layout.item_view,null);
        }
        TextView view = (TextView) convertView.findViewById(R.id.name);
        view.setText(categoryModel.get(position).getName());
        System.out.println(categoryModel.get(position).getName());
        return convertView;
    }
}
