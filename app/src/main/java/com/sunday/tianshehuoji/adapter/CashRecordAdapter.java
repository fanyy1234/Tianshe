package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.CashRecord;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



/**
 * Created by 刘涛 on 2016/12/28.
 */

public class CashRecordAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CashRecord> dataSet;


    public CashRecordAdapter(Context context, List<CashRecord> datas) {
        this.mContext = context;
        this.dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_statistic_item, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder = (ListHolder) holder;
        CashRecord item = dataSet.get(position);
        viewHolder.txtMoney.setText(String.format("￥%s", item.getWithdrawsMoney().setScale(2, RoundingMode.HALF_UP)));
        switch (item.getType()) {
            case 1:
                viewHolder.txtTime.setText("已提交");
               viewHolder.txtTime.setTextColor(mContext.getResources().getColor(R.color.text_pressed));
                break;
            case 2:
                viewHolder.txtTime.setText("提现中");
                viewHolder.txtTime.setTextColor(mContext.getResources().getColor(R.color.text_pressed));
                break;
            case 3:
                viewHolder.txtTime.setText("已到账");
                viewHolder.txtTime.setTextColor(mContext.getResources().getColor(R.color.text_black));
                break;
        }
        viewHolder.txtPerson.setText(item.getBankName());
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
            ButterKnife.bind(this, itemView);

        }
    }
}
