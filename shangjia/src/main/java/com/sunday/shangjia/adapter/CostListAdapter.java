package com.sunday.shangjia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.shangjia.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class CostListAdapter extends RecyclerView.Adapter {


    private Context mContext;

    public CostListAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_profit_detail, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
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


