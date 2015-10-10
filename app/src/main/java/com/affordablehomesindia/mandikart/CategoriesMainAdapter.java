package com.affordablehomesindia.mandikart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CategoriesMainAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private Context context;
    private List<String> head_data;
    private HashMap<String, List<String>> child_data;

    public CategoriesMainAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.head_data = listDataHeader;
        this.child_data = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.child_data.get(this.head_data.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sub_categories, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.sub_text);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.child_data.get(this.head_data.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.head_data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.head_data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.categories_head, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.head_text);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}