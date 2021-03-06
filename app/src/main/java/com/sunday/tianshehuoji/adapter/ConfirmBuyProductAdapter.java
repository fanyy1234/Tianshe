package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/4.
 */

public class ConfirmBuyProductAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderConfirm.IteamListBean> dataSet=new ArrayList<>();

    public ConfirmBuyProductAdapter(Context context, List<OrderConfirm.IteamListBean> datas) {
        this.mContext = context;
        this.dataSet = datas;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_detail_product, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        OrderConfirm.IteamListBean item=dataSet.get(position);
        viewHolder.productName.setText(item.getName());
        viewHolder.productNum.setText(String.format("x%d",item.getNum()));
        viewHolder.productPrice.setText(String.format("%s",item.getPrice().setScale(2, RoundingMode.HALF_UP)));


        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_num)
        TextView productNum;
        @Bind(R.id.product_price)
        TextView productPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
