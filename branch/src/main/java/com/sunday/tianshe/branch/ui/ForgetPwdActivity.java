package com.sunday.tianshe.branch.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.TimeCount;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPwdActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.mobile)
    EditText mobile;
    @Bind(R.id.validate_code)
    EditText validateCode;
    @Bind(R.id.get_code)
    TextView getCode;
    @Bind(R.id.et_new_pwd)
    ClearEditText etNewPwd;
    @Bind(R.id.et_new_pwd_confirm)
    ClearEditText etNewPwdConfirm;
    @Bind(R.id.btn_next)
    Button btnNext;


    private TimeCount timeCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);

        timeCount = new TimeCount(60000, 1000, getCode);
    }

    @OnClick(R.id.get_code)
    void sendValicateCode() {
        sendValidateCode();
    }


    private void sendValidateCode() {
        String mobileTxt = mobile.getText().toString();
        if (TextUtils.isEmpty(mobileTxt)) {
            ToastUtils.showToast(mContext, "请填写手机号码");
            return;
        }
        Call<ResultDO> call = ApiClient.getApiAdapter().sendValidateCode(1, mobileTxt);
        showLoadingDialog(0);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (timeCount != null && response.body() != null && response.body().getCode() == 0) {
                    timeCount.start();
                } else {
                    ToastUtils.showToast(mContext, "发送验证码失败");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
    }


    private void findPwd(){
        String mobileTxt = mobile.getText().toString();
        if (TextUtils.isEmpty(mobileTxt)) {
            ToastUtils.showToast(mContext, "请填写手机号码");
            return;
        }

        String code = validateCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showToast(mContext, "请输入验证码");
            return;
        }
        String newPwd = etNewPwd.getText().toString();
        String confirmPwd = etNewPwdConfirm.getText().toString();
        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.showToast(mContext, "请输入新密码！");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.showToast(mContext, "两次密码不一致！");
            return;
        }
        Call<ResultDO> call = ApiClient.getApiAdapter().findPwd(mobileTxt,newPwd,code);
        showLoadingDialog(0);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (timeCount != null && response.body() != null && response.body().getCode() == 0) {
                    timeCount.start();
                } else {
                    ToastUtils.showToast(mContext, "密码重置成功");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });

    }
}
