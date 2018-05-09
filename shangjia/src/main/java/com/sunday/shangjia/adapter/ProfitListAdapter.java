package com.sunday.shangjia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.ProfitRecord;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class ProfitListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<ProfitRecord> dataSet;


    public ProfitListAdapter(Context context,List<ProfitRecord> datas) {
        this.mContext = context;
        this.dataSet=datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_profit_detail, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder= (ListHolder) holder;
        ProfitRecord item=dataSet.get(position);
        viewHolder.txtMoney.setText(String.format("￥%s",item.getMoney().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.txtPerson.setText(item.getMemberName());
        if (!TextUtils.isEmpty(item.getOrderNo())){
            viewHolder.txtTime.setText(String.format("订单号:%s",item.getOrderNo()));
        }
        viewHolder.txtCard.setText(item.getTime());

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
            ButterKnife.bind(this,itemView);
        }
    }

}
