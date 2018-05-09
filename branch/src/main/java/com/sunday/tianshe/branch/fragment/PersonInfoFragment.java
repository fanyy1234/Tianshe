
package com.sunday.tianshe.branch.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.adapter.MakeCommentImageAdapter;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.ui.ChildAccountListActivity;
import com.sunday.tianshe.branch.ui.LoginActivity;
import com.sunday.tianshe.branch.ui.PersonInfoActivity;
import com.sunday.tianshe.branch.ui.SettingActivity;
import com.sunday.tianshe.branch.ui.UpdatePwdActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class PersonInfoFragment extends BaseFragment {

    @Bind(R.id.avater)
    ImageView avater;
    @Bind(R.id.shop_name)
    TextView shopName;
    @Bind(R.id.user_name)
    TextView userName;
    int width;
    public static PersonInfoFragment newInstance() {
        PersonInfoFragment fragment = new PersonInfoFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_person_info, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        width = PixUtils.dip2px(mContext,80);
        setInfo();
        getShopInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            getShopInfo();
        }
    }

    @OnClick(R.id.relative_layout)
    void relativeLayout(){
        intent = new Intent(mContext, PersonInfoActivity.class);
        startActivityForResult(intent,0x111);
    }

    @OnClick(R.id.child_account_manage)
    void childAccountManage(){
        intent = new Intent(mContext, ChildAccountListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.update_pwd)
    void updatePwd(){
        intent = new Intent(mContext, UpdatePwdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.setting)
    void setting(){
        intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }


    private void setInfo(){
        if(!TextUtils.isEmpty(BaseApp.getInstance().getMember().logo)){
            Picasso.with(mContext)
                    .load(BaseApp.getInstance().getMember().logo.replace("https","http"))
                    .resize(width,width)
                    .into(avater);
        }

        userName.setText(BaseApp.getInstance().getMember().userName);
        shopName.setText(BaseApp.getInstance().getMember().sellerName);

    }



    private void getShopInfo(){

        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().getShopInfo(BaseApp.getInstance().getMember().id);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                if (isFinish) return;
                if (response.body() == null) {
                    ToastUtils.showToast(mContext, response.message());
                    return;
                }
                dissMissDialog();
                if (response.body().getCode() == 0) {
                    Gson gson = new Gson();
                    String member = gson.toJson(response.body().getResult());
                    SharePerferenceUtils.getIns(mContext).putString("member",member);
                    BaseApp.getInstance().setMember(response.body().getResult());
                    setInfo();
                } else {
                    ToastUtils.showToast(mContext, "网络不给力！" + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                if (isFinish) return;
                dissMissDialog();
                ToastUtils.showToast(mContext, "系统异常");
            }
        });
    }


}
