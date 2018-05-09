package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class AccountListAdapter  extends RecyclerView.Adapter {


    private Context mContext;
    private List<CashAccount> dataSet;
    private View.OnClickListener onClickListener;


    public AccountListAdapter(Context mContext, List<CashAccount> dataSet) {
        this.mContext = mContext;
        this.dataSet = dataSet;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_account_item, null);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AccountHolder viewHolder= (AccountHolder) holder;
        CashAccount item=dataSet.get(position);
        viewHolder.txt.setText(item.getBank());
        viewHolder.txt2.setText(item.getAccountName());
        viewHolder.txt3.setText(item.getAccount());
        if (!TextUtils.isEmpty(item.getBankLogo())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getBankLogo()))
                    .into(viewHolder.image);
        }
        viewHolder.del.setTag(position);
        viewHolder.del.setOnClickListener(onClickListener);
        viewHolder.viewItem.setTag(item);
        viewHolder.viewItem.setOnClickListener(onClickListener);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class AccountHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.txt)
        TextView txt;
        @Bind(R.id.txt2)
        TextView txt2;
        @Bind(R.id.txt3)
        TextView txt3;
        @Bind(R.id.del)
        ImageView del;
        @Bind(R.id.viewItem)
        RelativeLayout viewItem;

        public AccountHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
