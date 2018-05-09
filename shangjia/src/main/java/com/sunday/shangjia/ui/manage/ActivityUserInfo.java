package com.sunday.shangjia.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.event.EventBus;
import com.sunday.common.imageselector.MultiImageSelectorActivity;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ImageUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.event.ExitApp;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.member.utils.UploadUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Account;
import com.sunday.shangjia.entity.Image;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.login.LoginActivity;
import com.sunday.shangjia.ui.login.ResetMobileFirstStepActivity;
import com.sunday.shangjia.util.ImgUtils;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/21.
 */


public class ActivityUserInfo extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.image)
    CircleImageView image;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.phone_number)
    TextView tvChangePhone;
    @Bind(R.id.logo_layout)
    RelativeLayout logoLayout;
    @Bind(R.id.rl_name)
    RelativeLayout rlName;
    @Bind(R.id.member_level)
    TextView memberLevel;
    @Bind(R.id.btn_exit)
    TextView btnExit;


    Account member;
    int appWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        titleView.setText("个人资料");
        member = (Account) getIntent().getSerializableExtra("account");
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        initView();


    }

    private void initView() {
        if (!TextUtils.isEmpty(member.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(member.getLogo()))
                    .resize(appWidth / 7, appWidth / 7)
                    .centerInside()
                    .into(image);
        }
        if (!TextUtils.isEmpty(member.getShopName())) {
            tvName.setText(member.getShopName());
        }
        if (StringUtils.isMobileNO(member.getMobile())) {
            String a = member.getMobile().substring(0, 3) + "****" + member.getMobile().substring(7, 11);
            tvChangePhone.setText(String.format("手机号   %s", a));
        } else {
            tvChangePhone.setText(String.format("手机号   %s", member.getMobile()));
        }
        memberLevel.setText(member.getUserName());


    }

    private final static int REQUEST_CHOOSE_IMAGE = 0x1111;

    @OnClick(R.id.logo_layout)
    void selectLogo() {
        intent = new Intent(mContext, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
    }

    private final static int REQUEST_NAME = 10;



    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ArrayList<String> mCompressPath = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CHOOSE_IMAGE:
                mSelectPath.clear();
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mCompressPath.clear();
                for (String item : mSelectPath) {
                    mCompressPath.add(ImageUtils.getCompressImagePath(item));
                }
                updateImageView();
                break;

        }
    }

    String saveUrl;

    private void updateImageView() {
        if (TextUtils.isEmpty(mCompressPath.get(0))) {
            return;
        }
        Picasso.with(mContext)
                .load(new File(mCompressPath.get(0)))
                .placeholder(R.mipmap.logo)
                .error(R.mipmap.logo)
                .resize(appWidth / 5, appWidth / 5)
                .centerInside()
                .into(image);
        showLoadingDialog(0);
        Call<ResultDO<Image>> call = AppClient.getAppAdapter().saveImage(UploadUtils.getRequestBody(mCompressPath.get(0)));
        call.enqueue(new Callback<ResultDO<Image>>() {
            @Override
            public void onResponse(Call<ResultDO<Image>> call, Response<ResultDO<Image>> response) {
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    saveUrl = response.body().getResult().getViewUrl();
                    updateInfo(saveUrl);
                } else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Image>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }

    private void updateInfo(String str1){
        showLoadingDialog(0);
        Call<ResultDO<Account>> call= AppClient.getAppAdapter().saveMember(String.valueOf(member.getId()),str1,null,null);
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                dissMissDialog();
                ResultDO<Account> resultDO=response.body();
                if (resultDO==null){return;}
                if (resultDO.getCode()==0&&resultDO.getResult()!=null){
                    member.setLogo(resultDO.getResult().getLogo());
                    SharePerferenceUtils.getIns(mContext).saveOAuth(member);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    @OnClick(R.id.rl_phone)
    void resetMobile() {
        intent=new Intent(mContext, ResetMobileFirstStepActivity.class);
        startActivity(intent);
    }







    @OnClick(R.id.btn_exit)
    void logOut() {
        SharePerferenceUtils.getIns(mContext).removeKey("oAuth");
        SharePerferenceUtils.getIns(mContext).removeKey(Constants.IS_LOGIN);
        SharePerferenceUtils.getIns(mContext).removeKey(Constants.MEMBERID);
        SharePerferenceUtils.getIns(mContext).removeKey("validTime");
        SharePerferenceUtils.getIns(mContext).removeKey("shopId");
        EventBus.getDefault().post(new ExitApp());
        finish();
        intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);

    }

}
