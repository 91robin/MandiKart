package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CategoryAdapter extends BaseAdapter {

    public int[] ids;
    private Context context;


    public CategoryAdapter(Context context, int[] ids){
        this.context=context;
        this. ids=ids;
    }
    @Override
    public int getCount() {
        return ids.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_view,null);
        }
        ImageView img= (ImageView)convertView.findViewById(R.id.image);
        img.setImageResource(ids[position]);
        return convertView;
    }
}
