package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.shop.MemberSize;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/4/21.
 */

public class MemberSizeListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<MemberSize> dataSet;
    private View.OnClickListener onClickListener;


    public MemberSizeListAdapter(Context context, List<MemberSize> datas) {
        mContext = context;
        dataSet = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_membersize_item, null);
        return new ListHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder= (ListHolder) holder;
        MemberSize item=dataSet.get(position);
        listHolder.name.setText(item.getName());
        listHolder.xiongwei.setText(item.getXiongwei());
        listHolder.yaowei.setText(item.getYaowei());
        listHolder.tunwei.setText(item.getTunwei());
        listHolder.yichang.setText(item.getYichang());
        listHolder.jiankuan.setText(item.getJiankuan());
        listHolder.xiuchang.setText(item.getXiuchang());
//        if (item.isDeleted()){
//            listHolder.choice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.addr_select,0,0,0);
//        }else{
//            listHolder.choice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select_no,0,0,0);
//        }

        listHolder.edite.setOnClickListener(onClickListener);
        listHolder.delete.setOnClickListener(onClickListener);
        listHolder.choice.setOnClickListener(onClickListener);
        listHolder.addressLayout.setOnClickListener(onClickListener);
        listHolder.edite.setTag(position);
        listHolder.delete.setTag(position);
        listHolder.choice.setTag(position);
        listHolder.addressLayout.setTag(item);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.xiongwei)
        TextView xiongwei;
        @Bind(R.id.yaowei)
        TextView yaowei;
        @Bind(R.id.tunwei)
        TextView tunwei;
        @Bind(R.id.yichang)
        TextView yichang;
        @Bind(R.id.jiankuan)
        TextView jiankuan;
        @Bind(R.id.xiuchang)
        TextView xiuchang;
        @Bind(R.id.address_layout)
        LinearLayout addressLayout;
        @Bind(R.id.choice)
        TextView choice;
        @Bind(R.id.edite)
        TextView edite;
        @Bind(R.id.delete)
        TextView delete;
//        @Bind(R.id.default_layout)
//        RelativeLayout defaultLayout;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
