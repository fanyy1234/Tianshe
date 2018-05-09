package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.ShopProduct;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/26.
 */

public class ProductTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopProduct> shopProductList;
    private int selectIndex=0;

    public ProductTypeAdapter(Context context, List<ShopProduct> shopProducts) {
        this.mContext = context;
        this.shopProductList = shopProducts;

    }

    public void setSelectIndex(int index){
        this.selectIndex=index;
    }


    @Override
    public int getCount() {
        return shopProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_product_type, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (position==selectIndex){
            viewHolder.llLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.category.setTextColor(mContext.getResources().getColor(R.color.text_pressed));

        }else {
            viewHolder.llLayout.setBackgroundColor(mContext.getResources().getColor(R.color.window_background));
            viewHolder.category.setTextColor(mContext.getResources().getColor(R.color.text_not_pressed));

        }
        ShopProduct item=shopProductList.get(position);
        viewHolder.category.setText(item.getTypeName());
        return convertView;

    }


    static class ViewHolder {
        @Bind(R.id.category)
        TextView category;
        @Bind(R.id.ll_layout)
        RelativeLayout llLayout;
        @Bind(R.id.view)
        View view;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
