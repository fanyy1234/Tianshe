package com.sunday.tianshe.branch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sunday.common.utils.PixUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.Shop;
import com.sunday.tianshe.branch.ui.MemberAuthActivity;
import com.sunday.tianshe.branch.ui.ShopDetailActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/12/23.
 */

public class ShopManageAdapter extends RecyclerView.Adapter {


    private List<Shop> list;
    private int colorGreen;
    private int colorRed;
    private Context mContext;

    private String str1,str2;

    public ShopManageAdapter(Context context, List<Shop> list) {
        this.list = list;
        this.mContext = context;
        colorGreen = mContext.getResources().getColor(R.color.item_bg);
        colorRed = mContext.getResources().getColor(R.color.white);
        str1 = "总营业额:￥%1$s元";
        str2 = "总利润:￥%1$s元";
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shop_manage, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ShopManageAdapter.ViewHolder viewHolder = (ShopManageAdapter.ViewHolder) holder;
            if (position % 2 == 0) {
                viewHolder.itemView.setBackgroundColor(colorGreen);
            } else {
                viewHolder.itemView.setBackgroundColor(colorRed);
            }
            Shop item = list.get(position);
            viewHolder.shopName.setText(item.getShopName());
            viewHolder.totalAmount.setText(String.format(str1,String.valueOf(item.getOtherMonthTotalMoney())));
            viewHolder.totalProfit.setText(String.format(str2,String.valueOf(item.getOtherMonthTotalProfitMoney())));
            viewHolder.itemView.setTag(R.id.cb_item_tag,String.valueOf(item.getShopId()));
            viewHolder.itemView.setTag(R.id.account,item.getShopName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.shop_name)
        TextView shopName;
        @Bind(R.id.total_amount)
        TextView totalAmount;
        @Bind(R.id.total_profit)
        TextView totalProfit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String shopId = (String)v.getTag(R.id.cb_item_tag);
            String shopName = (String)v.getTag(R.id.account);
            Intent intent = new Intent(mContext, ShopDetailActivity.class);
            intent.putExtra("name",shopName);
            intent.putExtra("shopId",shopId);
            mContext.startActivity(intent);
        }
    }



}
