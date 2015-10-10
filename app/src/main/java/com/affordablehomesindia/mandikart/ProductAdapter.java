package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Intruder Shanky on 9/11/2015.
 */
public class ProductAdapter extends BaseAdapter {

    private Boolean[] status;
    private Context context;
    //   private int[] imageIds;
    private int items;

    public ProductAdapter(Context context, Boolean[] status, int items /* int[] imageIds*/) {
        this.context = context;
        this.status = status;
        this.items = items;
        //     this.imageIds=imageIds;
    }

    @Override
    public int getCount() {
        return items;
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
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_item, null);
        }
        //    ImageView productImage=(ImageView)convertView.findViewById(R.id.product_image);
        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        return convertView;
    }
}
