package com.huawei.stellarfmr.stellarfmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.listdata.MessageList;

import java.util.ArrayList;


public class MessageAdapter extends BaseAdapter {
    ArrayList<MessageList> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MessageAdapter(Context context, ArrayList<MessageList> myList) {
        this.myList = myList;
        this.context = context;

        try {
            inflater = LayoutInflater.from(this.context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public MessageList getItem(int position) {
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
            convertView = inflater.inflate(R.layout.row_journey_plan, parent, false);
            mViewHolder = new MyViewHolder(convertView, context);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        MessageList currentListData = getItem(position);

        mViewHolder.name.setText(currentListData.getName());
        mViewHolder.address.setText(currentListData.getAddress());
        mViewHolder.time.setText(currentListData.getTime());


        String S = currentListData.getName().substring(0, 1);

        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(S, context.getResources().getColor(R.color.lightBlue));

        mViewHolder.icon.setImageDrawable(drawable2);

        return convertView;
    }

    private class MyViewHolder {
        TextView name, address, time;
        ImageView icon;

        public MyViewHolder(View item, Context context) {
            name = (TextView) item.findViewById(R.id.jr_name);
            address = (TextView) item.findViewById(R.id.jr_address);
            time = (TextView) item.findViewById(R.id.jr_time);
            icon = (ImageView) item.findViewById(R.id.jr_loc);

        }
    }
}
