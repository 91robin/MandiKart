package com.mandikart.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by IntruSoft on 10/12/2015.
 */
public class CategoryListAdapter extends BaseAdapter {

    Context context;
    String items[];

    public CategoryListAdapter(Context context, String items[]) {
        this.context=context;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.length;
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
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.category_list_item_view,null);
        }
        TextView tv= (TextView)convertView.findViewById(R.id.categoryName);
        tv.setText(items[position]);
      //  tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return convertView;
    }

}
