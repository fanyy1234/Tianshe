package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePwdActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.et_old_pwd)
    ClearEditText etOldPwd;
    @Bind(R.id.et_new_pwd)
    ClearEditText etNewPwd;
    @Bind(R.id.et_new_pwd_confirm)
    ClearEditText etNewPwdConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        ButterKnife.bind(this);
        titleView.setText("修改密码");
    }

    @OnClick(R.id.btn_next)
    void nextStepClick() {
        String old = etOldPwd.getText().toString();
        if(TextUtils.isEmpty(old)){
            ToastUtils.showToast(mContext, "请输入旧密码！");
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

       Call<ResultDO> call = ApiClient.getApiAdapter().updateInfo(BaseApp.getInstance().getMember().id,null,old, newPwd);
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
                    ToastUtils.showToast(mContext, "密码重置成功！");
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtils.showToast(mContext, "密码重置失败！" + response.body().getMessage());
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
