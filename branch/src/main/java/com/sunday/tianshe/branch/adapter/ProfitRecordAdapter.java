package com.sunday.tianshe.branch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.Record;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/12/23.
 */

public class ProfitRecordAdapter extends RecyclerView.Adapter {


    private List<Record> list;
    private int colorGreen;
    private int colorRed;
    private Context mContext;


    private String amount = "0";
    private int type;

    public ProfitRecordAdapter(Context context, List<Record> list, int type) {
        this.list = list;
        this.type = type;
        this.mContext = context;
        colorGreen = mContext.getResources().getColor(R.color.item_bg);
        colorRed = mContext.getResources().getColor(R.color.white);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_total, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new TitleHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profit_record, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==1){
            TitleHolder viewHolder = (TitleHolder) holder;
            viewHolder.money.setText("￥"+amount);
        }else{
            ViewHolder viewHolder = (ViewHolder) holder;
            if (position % 2 == 0) {
                viewHolder.itemView.setBackgroundColor(colorGreen);
            } else {
                viewHolder.itemView.setBackgroundColor(colorRed);
            }
            Record item = list.get(position-1);
            viewHolder.money.setText("￥"+item.money);
            viewHolder.orderNo.setText("订单号:"+item.orderNo);
            if(type==1){
                viewHolder.buyName.setText(item.memberName);
            }else{
                viewHolder.buyName.setText("购买人:"+item.memberName);
            }
            viewHolder.dateTime.setText(item.time);
        }


    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else {
            return 2;
        }
    }


    public class TitleHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.total_amount)
        TextView money;

        public TitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.money)
        TextView money;
        @Bind(R.id.order_no)
        TextView orderNo;
        @Bind(R.id.buy_name)
        TextView buyName;
        @Bind(R.id.date_time)
        TextView dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
