package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.Seller;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class SellerListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<Seller> dataSet;
    private View.OnClickListener onClickListener;

    public SellerListAdapter(Context context, List<Seller> data) {
        this.mContext = context;
        this.dataSet = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_shop_list_item, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Holder viewHolder = (Holder) holder;
        if (position % 2 == 0) {
            viewHolder.textShop.setBackgroundColor(Color.parseColor("#fafafa"));
        } else {
            viewHolder.textShop.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        Seller shop = dataSet.get(position);
        viewHolder.textShop.setText(shop.getName());
        viewHolder.textShop.setOnClickListener(onClickListener);
        viewHolder.textShop.setTag(shop);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class Holder extends RecyclerView.ViewHolder  {
        @Bind(R.id.text_shop)
        TextView textShop;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
