package com.sunday.tianshe.branch.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.ui.MemberAuthActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/12/22.
 */

public class MemberAuthAdapter extends RecyclerView.Adapter {


    private List<Member> list;
    private int colorGreen;
    private int colorRed;
    private Context mContext;

    public MemberAuthAdapter(Context context, List<Member> list) {
        this.list = list;
        this.mContext = context;
        colorGreen = mContext.getResources().getColor(R.color.item_bg);
        colorRed = mContext.getResources().getColor(R.color.white);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_title, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    PixUtils.dip2px(mContext, 58));
            view.setLayoutParams(params);
            return new HeaderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_item, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 2) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Member item = list.get(position - 1);
            viewHolder.userName.setText(item.userName);
            viewHolder.type.setText(item.dLevel);
            if(item.type==2){
                viewHolder.job.setText("CEO");
                viewHolder.retName1.setVisibility(View.GONE);
                viewHolder.footLayout.setVisibility(View.VISIBLE);
            }else{
                viewHolder.job.setText("VIP");
                viewHolder.retName1.setVisibility(View.VISIBLE);
                viewHolder.footLayout.setVisibility(View.GONE);
                viewHolder.retName1.setText("推荐人:"+item.recName);
            }

            if(item.status==0){
                viewHolder.status.setVisibility(View.VISIBLE);
                viewHolder.percent.setVisibility(View.GONE);
                viewHolder.status.setText("待审核");
            }else if(item.status==1){
                viewHolder.status.setVisibility(View.GONE);
                viewHolder.percent.setVisibility(View.VISIBLE);
            }else{
                viewHolder.status.setText("审核不通过");
                viewHolder.status.setVisibility(View.VISIBLE);
            }
            viewHolder.percent.setText("股权比例:"+item.proportion);
            if(!TextUtils.isEmpty(item.logo)){
                Picasso.with(mContext)
                        .load(item.logo.replace("https","http"))
                        .resize(300,300)
                        .into(viewHolder.avater);
            }else{
                Picasso.with(mContext)
                        .load(R.mipmap.ic_login_logo)
                        .resize(300,300)
                        .into(viewHolder.avater);
            }
            viewHolder.retName.setText("推荐人:"+item.recName);
            viewHolder.userMobile.setText("手机号码:"+item.mobile);
            if(position%2==0){
                viewHolder.itemView.setBackgroundColor(colorGreen);
            }else{
                viewHolder.itemView.setBackgroundColor(colorRed);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public HeaderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MemberAuthActivity.class);
            mContext.startActivity(intent);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        @Bind(R.id.avater)
        CircleImageView avater;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.job)
        TextView job;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.user_mobile)
        TextView userMobile;
        @Bind(R.id.percent)
        TextView percent;
        @Bind(R.id.ret_name)
        TextView retName;
        @Bind(R.id.status)
        TextView status;
        @Bind(R.id.ret_name_q)
        TextView retName1;
        @Bind(R.id.foot_layout)
        LinearLayout footLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
