package com.sunday.tianshehuoji.ui.login;

import android.content.Intent;
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
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.MainActivity;
import com.sunday.tianshehuoji.ui.fyy.ComitClothOrderActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2016/12/12.
 */

public class LoginActivity extends BaseActivity {


    @Bind(R.id.edit_username)
    ClearEditText editUsername;
    @Bind(R.id.edit_password)
    ClearEditText editPassword;
    @Bind(R.id.text_findPwd)
    TextView textFindPwd;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    boolean isLogin;
    @Bind(R.id.text_regist)
    TextView textRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_submit)
    void submit() {
        String userName = editUsername.getText().toString().trim();
        String userPwd = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPwd)) {
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO<Account>> call = AppClient.getAppAdapter().login(userName, userPwd);
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                dissMissDialog();
                ResultDO<Account> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    long nowTime = System.currentTimeMillis() / 1000;
                    long validTime = nowTime + 30 * 24 * 60 * 60;
                    SharePerferenceUtils.getIns(mContext).saveOAuth(resultDO.getResult());
                    SharePerferenceUtils.getIns(mContext).putBoolean(Constants.IS_LOGIN, true);
                    SharePerferenceUtils.getIns(mContext).putLong("validTime", validTime);
                    SharePerferenceUtils.getIns(mContext).putString(Constants.MEMBERID, String.valueOf(resultDO.getResult().getId()));
                    BaseApplication.getInstance().setAdminUser(resultDO.getResult());
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, "网络异常");
                intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.text_findPwd)
    void findPwd() {
        intent = new Intent(mContext, ForgetPwdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.text_regist)
    void gotoRegist() {
//        intent = new Intent(mContext, ComitClothOrderActivity.class);
        intent = new Intent(mContext, RegisterActivity.class);
        startActivity(intent);
    }

}
