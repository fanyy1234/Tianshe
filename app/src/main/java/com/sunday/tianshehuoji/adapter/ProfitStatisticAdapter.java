package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.ProfitRecord;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class ProfitStatisticAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<ProfitRecord> dataSet;
    private int type;//1:收益 2：消费 3:分红


    public ProfitStatisticAdapter(Context context, List<ProfitRecord> datas, int type) {
        this.mContext = context;
        this.dataSet = datas;
        this.type = type;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_statistic_item, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder = (ListHolder) holder;
        ProfitRecord item = dataSet.get(position);
        if (type == 1) {
            viewHolder.txtMoney.setText(String.format("￥%s", item.getMoney().setScale(2, RoundingMode.HALF_UP)));
           if (item.getRecLevel()!=null) {
               if (item.getRecLevel() == 1) {
                   viewHolder.txtPerson.setText(String.format("分享:一级 %s", item.getFromName()));
               } else {
                   viewHolder.txtPerson.setText(String.format("分享:二级 %s", item.getFromName()));
               }
           }else {
               viewHolder.txtPerson.setText(String.format("分享:%s", item.getFromName()));
           }
            viewHolder.txtCard.setText(item.getTime());
        } else if (type == 2) {
            viewHolder.txtMoney.setText(String.format("￥%s", item.getMoney().setScale(2, RoundingMode.HALF_UP)));
            viewHolder.txtTime.setText(item.getTime());
            viewHolder.txtPerson.setText(String.format("订单号:%s", item.getFromName()));
        } else {
            viewHolder.txtMoney.setText(String.format("￥%s", item.getMoney().setScale(2, RoundingMode.HALF_UP)));
            viewHolder.txtTime.setText(item.getTime());
            viewHolder.txtPerson.setText(item.getFromName());
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ListHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtMoney)
        TextView txtMoney;
        @Bind(R.id.txtTime)
        TextView txtTime;
        @Bind(R.id.txtPerson)
        TextView txtPerson;
        @Bind(R.id.txtCard)
        TextView txtCard;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
