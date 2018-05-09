package com.sunday.shangjia.ui.manage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.ChildAccount;
import com.sunday.shangjia.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/30.
 */

public class AddChildAccountActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_child_account_name)
    ClearEditText editChildAccountName;
    @Bind(R.id.edit_set_pwd)
    ClearEditText editSetPwd;
    @Bind(R.id.edit_confirm_pwd)
    ClearEditText editConfirmPwd;
    @Bind(R.id.btn_all)
    RadioButton btnAll;
    @Bind(R.id.btn_order)
    RadioButton btnOrder;
    @Bind(R.id.rdg)
    RadioGroup rdg;
    @Bind(R.id.edit_old_pwd)
    ClearEditText editOldPwd;
    @Bind(R.id.ll_old_pwd)
    LinearLayout llOldPwd;
    @Bind(R.id.btn_submit)
    Button btnSubmit;


    private String id = "0";
    private String memberId;
    private String shopId;
    int type;
    private boolean isEdit = false;
    private ChildAccount childAccount;
    private String oldPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_account);
        ButterKnife.bind(this);
        titleView.setText("添加子帐号");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        shopId = SharePerferenceUtils.getIns(mContext).getString("shopId", "");
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        childAccount = (ChildAccount) getIntent().getSerializableExtra("childAccount");
        if (isEdit) {
            initView();
        }
    }

    private void initView() {
        if (childAccount == null) {
            return;
        }
        editChildAccountName.setText(childAccount.getName());
        if (childAccount.getType() == 1) {
            btnAll.setChecked(true);
        } else {
            btnOrder.setChecked(true);
        }
        llOldPwd.setVisibility(View.VISIBLE);
        id = childAccount.getId();
    }


    @OnClick(R.id.btn_submit)
    void submit() {
        String userName = editChildAccountName.getText().toString().trim();
        String pwd = editSetPwd.getText().toString().trim();
        String confirmPwd = editConfirmPwd.getText().toString().trim();
        oldPwd =editOldPwd.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(confirmPwd)) {
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            ToastUtils.showToast(mContext, "两次密码输入不一致!");
            return;
        }
        if (isEdit&&TextUtils.isEmpty(oldPwd)){
            ToastUtils.showToast(mContext, "请输入原密码!");
            return;
        }
        if (rdg.getCheckedRadioButtonId() == R.id.btn_order) {
            type = 2;
        } else {
            type = 1;
        }
        Call<ResultDO> call = AppClient.getAppAdapter().saveChildAccount(id, memberId, shopId, userName, pwd, null, null, type, oldPwd);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "保存成功");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, R.string.network_error);
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });

    }


}
