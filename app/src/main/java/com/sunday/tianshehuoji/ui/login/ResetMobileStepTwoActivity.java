package com.sunday.tianshehuoji.ui.login;

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
import com.sunday.tianshehuoji.entity.Account;
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

public class ResetMobileStepTwoActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_mobile)
    ClearEditText editMobile;
    @Bind(R.id.edit_code)
    ClearEditText editCode;
    @Bind(R.id.send_code)
    TextView sendCode;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    TimeCount timeCount;
    String mobileStr;
    String memberId;
    String oldMobile;
    Account account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_mobile_step_two);
        ButterKnife.bind(this);
        titleView.setText("修改手机号");
        timeCount=new TimeCount(60000,1000,sendCode);
        account= (Account) SharePerferenceUtils.getIns(mContext).getOAuth();
        oldMobile=account.getMobile();
      memberId=SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
    }


    @OnClick(R.id.send_code)
    void sendCode(){
        mobileStr = editMobile.getText().toString().trim();
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
        mobileStr = editMobile.getText().toString().trim();
       String code = editCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobileStr) || !StringUtils.isMobileNO(mobileStr)) {
            ToastUtils.showToast(mContext, "请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(mContext, "请输入验证码");
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().updateMobileOrPassword(memberId,mobileStr,null,code);
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
