package com.sunday.shangjia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.ChildAccount;
import com.sunday.shangjia.entity.Staff;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.manage.AddChildAccountActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.PorterDuff.Mode.ADD;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class ChildAccountListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChildAccount> dataSet;

    public ChildAccountListAdapter(Context context,List<ChildAccount> datas) {
        this.mContext = context;
        this.dataSet=datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_child_account, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder= (ListHolder) holder;
        ChildAccount item=dataSet.get(position);
        viewHolder.name.setText(String.format("子帐号:%s",item.getName()));
        if (item.getType()==1){
            viewHolder.permission.setText("权限：全部");
        }else {
            viewHolder.permission.setText("权限：仅订单");
        }
        viewHolder.delete.setTag(position);
        viewHolder.edit.setTag(position);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.permission)
        TextView permission;
        @Bind(R.id.delete)
        TextView delete;
        @Bind(R.id.edit)
        TextView edit;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            delete.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p= (int) v.getTag();
            ChildAccount item= dataSet.get(p);
            switch (v.getId()){
                case R.id.delete:
                    delete(item,p);
                    break;
                case R.id.edit:
                 Intent intent=new Intent(mContext, AddChildAccountActivity.class);
                    intent.putExtra("isEdit",true);
                    intent.putExtra("childAccount",item);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }
    private void delete(ChildAccount item, final int p){
        Call<ResultDO> call= AppClient.getAppAdapter().deleteChildAccount(item.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"删除成功");
                    dataSet.remove(p);
                    notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });
    }
}
