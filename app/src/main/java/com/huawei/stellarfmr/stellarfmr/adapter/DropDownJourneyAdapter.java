package com.huawei.stellarfmr.stellarfmr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.listdata.DropDownJourneyList;
import com.huawei.stellarfmr.stellarfmr.utils.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ${Azahar} on 12/20/2015.
 */
public class DropDownJourneyAdapter extends BaseAdapter {
    ArrayList<DropDownJourneyList> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public DropDownJourneyAdapter(Context context, ArrayList<DropDownJourneyList> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public DropDownJourneyList getItem(int position) {
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

        DropDownJourneyList currentListData = getItem(position);

        mViewHolder.name.setText(currentListData.getName());
        mViewHolder.address.setText(currentListData.getAddress());
        mViewHolder.time.setText(currentListData.getTime());
        String listCurrentDate=currentListData.getTime().substring(0,11);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String SystemCurrentDate = df.format(c.getTime());
        if(listCurrentDate.equals(Config.userDetails.get(14)))
        {
            mViewHolder.relLayoutbg.setBackgroundColor(Color.parseColor("#ffffff"));
            mViewHolder.relLayoutbg.setClickable(false);
        }
        else
        {
            mViewHolder.relLayoutbg.setBackgroundColor(Color.parseColor("#d3d3d3"));
            mViewHolder.relLayoutbg.setClickable(true);

        }



        return convertView;
    }

    private class MyViewHolder {
        TextView name, address, time;
        RelativeLayout relLayoutbg;


        public MyViewHolder(View item, Context context) {
            name = (TextView) item.findViewById(R.id.jr_name);
            address = (TextView) item.findViewById(R.id.jr_address);
            time = (TextView) item.findViewById(R.id.jr_time);
            relLayoutbg=(RelativeLayout)item.findViewById(R.id.journeyRowRelativelayout);

        }
    }
}
