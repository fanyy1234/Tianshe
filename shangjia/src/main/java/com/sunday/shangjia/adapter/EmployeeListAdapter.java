package com.sunday.shangjia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Staff;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.manage.AddEmployeeActivity;
import com.sunday.shangjia.util.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter {



    private Context mContext;
    private List<Staff> dataSet;

    private int appWidth;

     public EmployeeListAdapter(Context context, List<Staff> datas) {
        this.dataSet=datas;
        this.mContext = context;
         this.appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_emplyee_item, null);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder viewHolder= (ListHolder) holder;
        Staff item=dataSet.get(position);
        if (!TextUtils.isEmpty(item.getLogo())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .resize(appWidth/5,appWidth/5)
                    .centerInside()
                    .into(viewHolder.image);
        }
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(String.format("手机号:%s",item.getMobile()));
        viewHolder.edit.setTag(position);
        viewHolder.delete.setTag(position);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.image)
        CircleImageView image;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.delete)
        TextView delete;
        @Bind(R.id.edit)
        TextView edit;
        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            delete.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p= (int) v.getTag();
            Staff item= dataSet.get(p);
            switch (v.getId()){
                case R.id.delete:
                    delete(item,p);
                    break;
                case R.id.edit:
                    Intent intent=new Intent(mContext, AddEmployeeActivity.class);
                    intent.putExtra("isEdit",true);
                    intent.putExtra("staff",item);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }

    private void delete(Staff item,final int p){
        Call<ResultDO> call= AppClient.getAppAdapter().deleteStaff(item.getId());
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
