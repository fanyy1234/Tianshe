package com.sunday.tianshe.branch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.ChildAccount;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.ui.AddChildAccountActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2016/12/23.
 */

public class ChildAccountAdapter extends RecyclerView.Adapter {



    private List<ChildAccount> list;
    private int colorGreen;
    private int colorRed;
    private Context mContext;


    private String account;
    public ChildAccountAdapter(Context context, List<ChildAccount> list) {
        this.list = list;
        this.mContext = context;
        colorGreen = mContext.getResources().getColor(R.color.item_bg);
        colorRed = mContext.getResources().getColor(R.color.white);
        account = "子账号 : %1$s";
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_child_account, null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position % 2 == 0) {
            viewHolder.itemView.setBackgroundColor(colorGreen);
        } else {
            viewHolder.itemView.setBackgroundColor(colorRed);
        }
        ChildAccount childAccount = list.get(position);
        viewHolder.childAccount.setText(String.format(account,childAccount.name));
        viewHolder.edit.setTag(childAccount);
        viewHolder.delete.setTag(childAccount.id);
        viewHolder.delete.setTag(R.id.cb_item_tag,position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.child_account)
        TextView childAccount;
        @Bind(R.id.edit)
        TextView edit;
        @Bind(R.id.delete)
        TextView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.edit){
                ChildAccount childAccount = (ChildAccount) v.getTag();
                Intent intent = new Intent(mContext, AddChildAccountActivity.class);
                intent.putExtra("account",childAccount);
                mContext.startActivity(intent);
            }else if(v.getId()==R.id.delete){
                final int position = (int)v.getTag(R.id.cb_item_tag);
                final long id  = (long)v.getTag();
                final UIAlertView delDialog = new UIAlertView(mContext, "温馨提示", "确定删除该账号?",
                        "取消", "确定");
                delDialog.show();
                delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                                               @Override
                                               public void doLeft() {
                                                   delDialog.dismiss();
                                               }

                                               @Override
                                               public void doRight() {
                                                   delDialog.dismiss();
                                                   deleteChild(position,id);
                                               }
                                           }
                );
            }
        }
    }


    private void deleteChild(final int position,Long id){
        Call<ResultDO> call = ApiClient.getApiAdapter().deleteChildAccount(id);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if(response.body()!=null){
                    if(response.body().getCode()==0){
                        list.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }


}
