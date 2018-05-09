package com.sunday.tianshe.branch.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.ChildAccount;
import com.sunday.tianshe.branch.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加子账号
 */
public class AddChildAccountActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.account)
    EditText account;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.pwd_confirm)
    EditText pwdConfirm;

    private ChildAccount accountEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_account);
        ButterKnife.bind(this);
        accountEntity = (ChildAccount)getIntent().getSerializableExtra("account");
        if(accountEntity!=null){
            titleView.setText("修改子账号");
            account.setText(accountEntity.name);
        }else{
            titleView.setText("新增子账号");
        }


    }

    @OnClick(R.id.submit_btn)
    void onClick() {
        String accountTxt = account.getText().toString();
        String pwdTxt = pwd.getText().toString();
        String pwdConfirmTxt = pwdConfirm.getText().toString();
        if(TextUtils.isEmpty(accountTxt)){
            ToastUtils.showToast(mContext,"请输入用户名");
            return;
        }
        if(TextUtils.isEmpty(pwdTxt)||TextUtils.isEmpty(pwdConfirmTxt)){
            ToastUtils.showToast(mContext,"请输入密码");
            return;
        }
        if(!pwdTxt.equals(pwdConfirmTxt)){
            ToastUtils.showToast(mContext,"两次密码输入不一致");
        }
        showLoadingDialog(0);
        Long id = 0L ;
        if(accountEntity!=null){
            id = accountEntity.id;
        }
        Call<ResultDO> call = ApiClient.getApiAdapter().saveChildAccount(id,BaseApp.getInstance().getMember().sellerId,accountTxt,pwdTxt,null,null);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if(isFinish){
                    return ;
                }
                if(response.body()!=null){
                    if(response.body().getCode()==0){
                        ToastUtils.showToast(mContext,"添加成功");
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        ToastUtils.showToast(mContext,"账号已存在");
                    }
                }else{
                    ToastUtils.showToast(mContext,"网络异常");
                }


            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
}
