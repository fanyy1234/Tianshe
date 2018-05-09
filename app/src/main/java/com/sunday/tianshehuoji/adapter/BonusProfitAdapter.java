package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.BonusRecord;
import com.sunday.tianshehuoji.ui.manage.BonusDetailListActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2017/1/9.
 */

public class BonusProfitAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<BonusRecord> dataSet;

    public BonusProfitAdapter(Context context, List<BonusRecord> datas) {
        this.mContext = context;
        this.dataSet = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_bonus_item, null);
        RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        BonusRecord item = dataSet.get(position);
        listHolder.money.setText(String.format("￥%s",item.getMoney()));
        listHolder.time.setText(item.getDate());
        listHolder.linearLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.money)
        TextView money;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.ll_layout)
        LinearLayout linearLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           BonusRecord item= (BonusRecord) v.getTag();
            Intent intent=new Intent(mContext, BonusDetailListActivity.class);
            intent.putExtra("startTime",item.getStart());
            intent.putExtra("endTime",item.getEnd());
            intent.putExtra("totalMoney",String.format("%s",item.getMoney().setScale(2, RoundingMode.HALF_UP)));
            mContext.startActivity(intent);
        }
    }
}
