package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class CartDetailAdapter extends BaseAdapter {


    private Context mContext;
    private SparseArray<List<SellerShopProductListBean>> dataSet;

    public CartDetailAdapter(Context context, SparseArray<List<SellerShopProductListBean>> datas) {
        this.mContext = context;
        this.dataSet = datas;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(dataSet.keyAt(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_detail_product, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<SellerShopProductListBean> item = dataSet.get(dataSet.keyAt(position));
        if (item != null) {
            viewHolder.productName.setText(item.get(0).getName());
            viewHolder.productNum.setText(String.format("x%d", item.size()));
            viewHolder.productPrice.setText(String.format("%s", item.get(0).getPrice().setScale(2, RoundingMode.HALF_UP)));
        }
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
