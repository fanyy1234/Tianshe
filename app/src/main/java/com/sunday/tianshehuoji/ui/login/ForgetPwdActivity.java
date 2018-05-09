package com.sunday.tianshehuoji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/1/6.
 */

public class ForgetPwdActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.user_phone)
    ClearEditText userPhone;
    @Bind(R.id.edit_code)
    ClearEditText editCode;
    @Bind(R.id.send_code)
    TextView sendCode;
    @Bind(R.id.et_newPwd)
    ClearEditText etNewPwd;
    @Bind(R.id.et_confirmPwd)
    ClearEditText etConfirmPwd;
    @Bind(R.id.btn_resetPwd)
    Button btnResetPwd;

    TimeCount timeCount;
    String memberId,mobileStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        titleView.setText("忘记密码");
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
    }

    @OnClick(R.id.send_code)
    void sendCode(){
        mobileStr = userPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr) || !StringUtils.isMobileNO(mobileStr)) {
            ToastUtils.showToast(mContext, "请输入正确手机号");
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().sendMobileCode(2,mobileStr);
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
                    timeCount = new TimeCount(10000,1000,sendCode);
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

    @OnClick(R.id.btn_resetPwd)
    void submit(){
        mobileStr = userPhone.getText().toString().trim();
        String code = editCode.getText().toString().trim();
        String newPwd = etNewPwd.getText().toString().trim();
        String confirmPwd = etConfirmPwd.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr) || !StringUtils.isMobileNO(mobileStr)) {
            ToastUtils.showToast(mContext, "请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmPwd)) {
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.showToast(mContext, "两次新密码输入不一致");
            return;
        }
        if (newPwd.length() < 6 || confirmPwd.length() > 16) {
            ToastUtils.showToast(mContext, "请输入6-16位密码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(mContext, "请输入验证码");
            return;
        }


        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().updateMobileOrPassword(memberId,mobileStr,newPwd,code);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    return;
                }
                if (resultDO.getCode() == 0) {
                    ToastUtils.showToast(mContext,"修改成功");
                    intent=new Intent(mContext,LoginActivity.class);
                    startActivity(intent);
                    finish();
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
