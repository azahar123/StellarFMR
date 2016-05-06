package com.huawei.stellarfmr.stellarfmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    LayoutInflater inflater;
    String[] data = {"Select Brand", "Huawei", "Samsung"};

    public CustomSpinnerAdapter(Context context, int textViewResourceId
                                ) {
        super(context, textViewResourceId);
        this.context = context;
        //inflater = LayoutInflater.from(this.context);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.custom_spinner_layout, parent, false);
        TextView txtItem = (TextView) row.findViewById(R.id.spinner_text);
        txtItem.setText(data[position]);

        return row;
    }

}