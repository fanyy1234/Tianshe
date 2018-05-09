package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshe.branch.MainActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2016/12/21.
 * 分店登录界面
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.user_name)
    EditText userName;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.login_button)
    TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.for_get_pwd)
    void forgetPwd(){
        intent = new Intent(mContext,ForgetPwdActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.login_button)
    void loginButton(){
        String name = userName.getText().toString();
        String pwdTxt = pwd.getText().toString();
        if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(pwdTxt)){
            ToastUtils.showToast(mContext,"请填写用户名密码");
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO<Member>> call = ApiClient.getApiAdapter().login(name,pwdTxt);
        call.enqueue(new Callback<ResultDO<Member>>() {
            @Override
            public void onResponse(Call<ResultDO<Member>> call, Response<ResultDO<Member>> response) {
                dissMissDialog();
                if(response.body()!=null){
                    ResultDO<Member> resultDO = response.body();
                    if(resultDO.getCode()==0){
                        SharePerferenceUtils.getIns(mContext).putBoolean("is_login",true);
                        Gson gson = new Gson();
                        String member = gson.toJson(resultDO.getResult());
                        SharePerferenceUtils.getIns(mContext).putString("member",member);
                        BaseApp.getInstance().setMember(resultDO.getResult());
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        ToastUtils.showToast(mContext,resultDO.getMessage());
                    }
                }else{
                    ToastUtils.showToast(mContext,"网络连接失败");
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Member>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,"网络连接失败");
            }
        });
    }
}
