package com.sunday.tianshehuoji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.MobileUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/1/6.
 */

public class ResetMobileFirstStepActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_code)
    ClearEditText editCode;
    @Bind(R.id.desc)
    TextView desc;
    @Bind(R.id.send_code)
    TextView sendCode;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    TimeCount timeCount;
    String mobileNo;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_mobile_step1);
        ButterKnife.bind(this);
        titleView.setText("修改手机号码");
        timeCount=new TimeCount(60000,1000,sendCode);
        account= (Account) SharePerferenceUtils.getIns(mContext).getOAuth();
        mobileNo=account.getMobile();
        desc.setText(String.format("请输入%s收到的短信验证码", MobileUtil.ReplaceStr(mobileNo)));
    }

    @OnClick(R.id.send_code)
    void sendCode(){
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().sendMobileCode(2,mobileNo);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    return;
                }
                if (resultDO.getCode() == 0) {
                    ToastUtils.showToast(mContext, R.string.send_code_success);
                    timeCount.start();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.send_code_failed);
            }
        });
    }


    @OnClick(R.id.btn_submit)
    void submit(){
        String code=editCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)){return;}
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().checkCodeByMobile(mobileNo,code,2);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    return;
                }
                if (resultDO.getCode() == 0) {
                   intent=new Intent(mContext,ResetMobileStepTwoActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.send_code_failed);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timeCount != null) {
            timeCount.cancel();
        }
    }
}
