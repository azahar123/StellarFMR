package com.huawei.stellarfmr.stellarfmr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.listdata.StocksListData;

import java.util.ArrayList;

public class SalesAdapter extends BaseAdapter {

    ArrayList<StocksListData> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SalesAdapter(Context context, ArrayList<StocksListData> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public StocksListData getItem(int position) {
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
            convertView = inflater.inflate(R.layout.row_sale_stock_order, parent, false);
            mViewHolder = new MyViewHolder(convertView, context);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        StocksListData listData = getItem(position);

        mViewHolder.model.setText(listData.getModel());

        mViewHolder.sale.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int itemIndex = v.getId();
//                    String enteredPrice = ((EditText) v).getText()
//                            .toString();
//                    selItems.put(itemIndex, enteredPrice);
                }
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        TextView model;
        EditText sale, stock, order;

        public MyViewHolder(View item, Context context) {
            model = (TextView) item.findViewById(R.id.model);
            sale = (EditText) item.findViewById(R.id.sale_data);
            stock = (EditText) item.findViewById(R.id.stock_data);
            order = (EditText) item.findViewById(R.id.order_data);

        }
    }
}
