package com.sunday.shangjia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Account;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.login.ResetPwdActivity;
import com.sunday.shangjia.ui.manage.ActivityUserInfo;
import com.sunday.shangjia.ui.manage.ChildAccountManageActivity;
import com.sunday.shangjia.ui.manage.CustomerCommentActivity;
import com.sunday.shangjia.ui.manage.EmployeeManageActivity;
import com.sunday.shangjia.ui.manage.SettingActivity;
import com.sunday.shangjia.util.ImgUtils;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.io.FileDescriptor.in;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class ManageFragment extends BaseFragment {


    @Bind(R.id.user_logo)
    CircleImageView userLogo;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_account)
    TextView userAccount;
    @Bind(R.id.check_user_info)
    ImageView checkUserInfo;
    @Bind(R.id.menu1)
    TextView menu1;
    @Bind(R.id.menu2)
    TextView menu2;
    @Bind(R.id.menu3)
    TextView menu3;
    @Bind(R.id.menu4)
    TextView menu4;
    @Bind(R.id.menu5)
    TextView menu5;

    Account account;
    String memberId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_management, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = (Account) SharePerferenceUtils.getIns(mContext).getOAuth();
        initView();
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");

    }

    private void initView() {
        if (account == null) {
            return;
        }
        if (!TextUtils.isEmpty(account.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(account.getLogo()))
                    .into(userLogo);
        }
        if (!TextUtils.isEmpty(account.getShopName())) {
            userName.setText(account.getShopName());
        }
        userAccount.setText(String.format("当前账号:%s", account.getUserName()));

    }

    //个人信息
    @OnClick({R.id.user_logo, R.id.user_name, R.id.user_account, R.id.check_user_info})
    void userInfo() {
        intent = new Intent(mContext, ActivityUserInfo.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    @OnClick(R.id.menu1)
    void menu1() {
        intent = new Intent(mContext, EmployeeManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu2)
    void menu2() {
        intent = new Intent(mContext, ChildAccountManageActivity.class);
        startActivity(intent);

    }

    //评价
    @OnClick(R.id.menu3)
    void menu3() {
        intent = new Intent(mContext, CustomerCommentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu4)
    void menu4() {
        intent = new Intent(mContext, ResetPwdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.menu5)
    void menu5() {
        intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }

    private void getMemberDetail() {
        Call<ResultDO<Account>> call = AppClient.getAppAdapter().getMemberById(memberId);
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                ResultDO<Account> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    account = resultDO.getResult();
                    initView();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getMemberDetail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
