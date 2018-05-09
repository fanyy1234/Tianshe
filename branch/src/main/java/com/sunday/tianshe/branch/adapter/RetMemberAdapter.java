package com.sunday.tianshe.branch.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.ui.MemberAuthActivity;
import com.sunday.tianshe.branch.ui.RecommondMemberActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2016/12/23.
 */

public class RetMemberAdapter extends RecyclerView.Adapter {


    private List<BaseMember> list;
    private int colorGreen;
    private int colorRed;
    private Context mContext;

    private BaseMember selectMember;
    private int lastSelect;
    private int width ;

    public RetMemberAdapter(Context context, List<BaseMember> list) {
        this.list = list;
        this.mContext = context;
        colorGreen = mContext.getResources().getColor(R.color.item_bg);
        colorRed = mContext.getResources().getColor(R.color.white);
        width = PixUtils.dip2px(mContext,60);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ret_member_title, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    PixUtils.dip2px(mContext, 58));
            view.setLayoutParams(params);
            return new HeaderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ret_member, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 2) {
            RetMemberAdapter.ViewHolder viewHolder = (RetMemberAdapter.ViewHolder) holder;
            BaseMember item = list.get(position - 1);
            viewHolder.name.setText(item.userName);
            viewHolder.mobile.setText("手机号码:"+item.mobile);
            if(!TextUtils.isEmpty(item.logo)){
                Picasso.with(mContext)
                        .load(item.logo.replace("https","http"))
                        .resize(width,width)
                        .into(viewHolder.avater);
            }
            if(lastSelect==position){
                viewHolder.isSelected.setChecked(true);
            }else{
                viewHolder.isSelected.setChecked(false);
            }
            viewHolder.isSelected.setTag(position);
            if (position % 2 == 0) {
                viewHolder.itemView.setBackgroundColor(colorGreen);
            } else {
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

    public class HeaderHolder extends RecyclerView.ViewHolder implements EditText.OnEditorActionListener{
        @Bind(R.id.query)
        EditText queryTxt;
        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            queryTxt.setOnEditorActionListener(this);
        }


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 当按了搜索之后关闭软键盘
                ((InputMethodManager) queryTxt.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        ((Activity)(mContext)).getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                serchMember(queryTxt.getText().toString());
                return true;
            }
            return false;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener{
        @Bind(R.id.is_selected)
        CheckBox isSelected;
        @Bind(R.id.avater)
        CircleImageView avater;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.mobile)
        TextView mobile;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            isSelected.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int positon = (Integer)buttonView.getTag();
            if(isChecked){
                if(lastSelect!=positon){
                    notifyItemChanged(lastSelect);
                    selectMember = list.get(positon-1);
                    lastSelect = positon;
                }
            }
        }
    }

    public BaseMember getSelectMember() {
        return selectMember;
    }


    private void serchMember(String keyWords){
        if(!TextUtils.isEmpty(keyWords)){
            Call<ResultDO<List<BaseMember>>> call = ApiClient.getApiAdapter().searchMember(keyWords);
            call.enqueue(new Callback<ResultDO<List<BaseMember>>>() {
                @Override
                public void onResponse(Call<ResultDO<List<BaseMember>>> call, Response<ResultDO<List<BaseMember>>> response) {
                    ResultDO<List<BaseMember>> resultDO = response.body();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            list.clear();
                            list.addAll(resultDO.getResult());
                            notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<List<BaseMember>>> call, Throwable t) {

                }
            });
        }else{
            ((RecommondMemberActivity)mContext).pageNo=1;
            ((RecommondMemberActivity)mContext).pageSize=20;
            Call<ResultDO<List<BaseMember>>> call = ApiClient.getApiAdapter().recList(1,20);
            call.enqueue(new Callback<ResultDO<List<BaseMember>>>() {
                @Override
                public void onResponse(Call<ResultDO<List<BaseMember>>> call, Response<ResultDO<List<BaseMember>>> response) {
                    ResultDO<List<BaseMember>> resultDO = response.body();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            list.clear();
                            list.addAll(resultDO.getResult());
                            notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<List<BaseMember>>> call, Throwable t) {
                }
            });
        }

    }

}
