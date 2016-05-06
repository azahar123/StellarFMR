package com.huawei.stellarfmr.stellarfmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.listdata.NavigationListData;
import com.huawei.stellarfmr.stellarfmr.R;

import java.util.ArrayList;


public class DrawerListAdapter extends BaseAdapter {

    ArrayList<NavigationListData> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public DrawerListAdapter(Context context, ArrayList<NavigationListData> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public NavigationListData getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_drawer_item, parent, false);
            mViewHolder = new MyViewHolder(convertView, context);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        NavigationListData currentListData = getItem(position);

        mViewHolder.name.setText(currentListData.getName());
        mViewHolder.icon.setImageResource(currentListData.getIcon());

        if(position==10){
            mViewHolder.line.setVisibility(View.VISIBLE);
        }else {
            mViewHolder.line.setVisibility(View.GONE);
        }


        return convertView;
    }

    private class MyViewHolder {
        TextView name, line;
        ImageView icon;

        public MyViewHolder(View item, Context context) {
            name = (TextView) item.findViewById(R.id.rowText);
            line = (TextView) item.findViewById(R.id.drawer_line);
            icon = (ImageView) item.findViewById(R.id.rowIcon);

        }
    }
}
