package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.event.EventBus;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.PixUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.entity.Image;
import com.sunday.member.event.ExitApp;
import com.sunday.member.utils.UploadUtils;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.http.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonInfoActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.avater)
    CircleImageView avater;
    @Bind(R.id.sub_item)
    ImageView subItem;
    @Bind(R.id.header_layout)
    RelativeLayout headerLayout;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.nickname_sub_item)
    ImageView nicknameSubItem;
    @Bind(R.id.nickname_layout)
    RelativeLayout nicknameLayout;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_mobile_num)
    TextView tvMobileNum;
    @Bind(R.id.mobile_layout)
    RelativeLayout mobileLayout;
    @Bind(R.id.tv_login_pwd)
    TextView tvLoginPwd;
    @Bind(R.id.login_pwd_sub_item)
    ImageView loginPwdSubItem;
    @Bind(R.id.login_pwd_layout)
    RelativeLayout loginPwdLayout;
    @Bind(R.id.login_out)
    TextView loginOut;
    @Bind(R.id.tv_account_num)
    TextView tvAccountNum;

    private final int REQUEST_IMAGE = 10;

    private ArrayList<String> mSelectPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);

        Member member = BaseApp.getInstance().getMember();
        if(!TextUtils.isEmpty(member.logo)){
            int with = PixUtils.dip2px(mContext,60);
            Picasso.with(mContext)
                    .load(member.logo.replace("https","http"))
                    .resize(with,with)
                    .into(avater);
        }
        tvShopName.setText(member.sellerName);
        tvMobileNum.setText(member.mobile);
        tvAccountNum.setText(member.userName);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    int width = (int) getResources().getDimension(R.dimen.app_width_sixty);
                    File imageFile = new File(mSelectPath.get(0));
                    Picasso.with(mContext).load(imageFile)
                            .resize(width, width)
                            .centerInside()
                            .error(R.mipmap.ic_login_logo)
                            .placeholder(R.mipmap.ic_login_logo)
                            .into(avater);
                    if (mSelectPath != null && mSelectPath.size() > 0) {
                        Call<ResultDO<Image>> call = ApiClient.getMemberAdapter().uploadSingleImg(UploadUtils.getRequestBody(mSelectPath));
                        call.enqueue(new Callback<ResultDO<Image>>() {
                            @Override
                            public void onResponse(Call<ResultDO<Image>> call, Response<ResultDO<Image>> response) {
                                ResultDO<Image> resultDO = response.body();
                                if (response != null && resultDO.getCode() == 0) {
                                    updateInfo(resultDO.getResult().getViewUrl());
                                }

                            }

                            @Override
                            public void onFailure(Call<ResultDO<Image>> call, Throwable t) {

                            }
                        });
                    }
                    break;
            }
        }
    }


    @OnClick(R.id.header_layout)
    void headerLayoutClick(){
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @OnClick(R.id.login_out)
    void loginOut(){
        EventBus.getDefault().post(new ExitApp());
        intent = new Intent(mContext,LoginActivity.class);
        startActivity(intent);
    }

    private void updateInfo(String img){
        Call<ResultDO> call = ApiClient.getApiAdapter().updateInfo(BaseApp.getInstance().getMember().id,img,null, null);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish) return;
                if (response.body() == null) {
                    ToastUtils.showToast(mContext, response.message());
                    return;
                }
                dissMissDialog();
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "信息修改成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtils.showToast(mContext, "信息修改失败！" + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                if (isFinish) return;
                dissMissDialog();
                ToastUtils.showToast(mContext, "系统异常");
            }
        });
    }
}
