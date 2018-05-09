package com.sunday.shangjia.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Account;
import com.sunday.shangjia.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/1/6.
 */

public class ResetPwdActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_old_password)
    ClearEditText editOldPassword;
    @Bind(R.id.edit_new_password)
    ClearEditText editNewPassword;
    @Bind(R.id.edit_confirm_password)
    ClearEditText editConfirmPassword;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        titleView.setText("修改密码");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
    }


    @OnClick(R.id.btn_submit)
    void resetPwd() {
        String oldPwd = editOldPassword.getText().toString().trim();
        String newPwd = editNewPassword.getText().toString().trim();
        String confirmPwd = editConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(confirmPwd)) {
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtils.showToast(mContext, "两次新密码输入不一致");
            return;
        }
        if (editNewPassword.length() < 6 || editNewPassword.length() > 16) {
            ToastUtils.showToast(mContext, "请输入6-16位密码");
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO<Account>> call = AppClient.getAppAdapter().saveMember(memberId,null,newPwd,oldPwd);
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                dissMissDialog();
                ResultDO<Account> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0 && resultDO.getResult() != null) {
                    ToastUtils.showToast(mContext,"修改成功");
                    SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

}
