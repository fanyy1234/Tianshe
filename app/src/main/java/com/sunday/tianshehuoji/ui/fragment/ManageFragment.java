package com.sunday.tianshehuoji.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.WebViewActivity;
import com.sunday.tianshehuoji.ui.fyy.AddressListActivity;
import com.sunday.tianshehuoji.ui.manage.ActivityUserInfo;
import com.sunday.tianshehuoji.ui.manage.AddAccountActivity;
import com.sunday.tianshehuoji.ui.manage.ApplyCashActivity;
import com.sunday.tianshehuoji.ui.manage.BonusProfitActivity;
import com.sunday.tianshehuoji.ui.manage.CashRecordActivity;
import com.sunday.tianshehuoji.ui.manage.ConsumeListActivity;
import com.sunday.tianshehuoji.ui.manage.FeedBackActivity;
import com.sunday.tianshehuoji.ui.manage.ProfitStatisticActivity;
import com.sunday.tianshehuoji.ui.manage.ReChargeActivity;
import com.sunday.tianshehuoji.ui.manage.SettingActivity;
import com.sunday.tianshehuoji.ui.manage.ShareProfitActivity;
import com.sunday.tianshehuoji.ui.manage.VotingListActivity;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class ManageFragment extends BaseFragment {

    @Bind(R.id.user_logo)
    CircleImageView userLogo;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_level)
    TextView userLevel;
    @Bind(R.id.consume_money)
    TextView consumeMoney;


    Account account;
    String memberId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.layout_manage_fragment, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        account = (Account) SharePerferenceUtils.getIns(mContext).getOAuth();
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        initView();


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

    private void initView() {
        if (account == null) {
            return;
        }
        if (!TextUtils.isEmpty(account.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(account.getLogo()))
                    .into(userLogo);
        }
        if (!TextUtils.isEmpty(account.getRealName())) {
            userName.setText(account.getRealName());
        }
        userLevel.setText(account.getDLevel());
        consumeMoney.setText(String.format("￥%s", account.getEnergyBean().setScale(2, RoundingMode.HALF_UP)));

    }

    @Override
    public void onResume() {
        super.onResume();
        getMemberDetail();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && BaseApplication.getInstance().isConsumed()) {
            getMemberDetail();
            BaseApplication.getInstance().setConsumed(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //个人信息
    @OnClick({R.id.user_logo, R.id.user_name, R.id.user_level})
    void userInfo() {
        intent = new Intent(mContext, ActivityUserInfo.class);
        intent.putExtra("account", account);
        startActivity(intent);
    }

    //分红收益统计
    @OnClick(R.id.text1)
    void checkStatistic1() {
        intent = new Intent(mContext, BonusProfitActivity.class);
        startActivity(intent);
    }

    //分享收益统计
    @OnClick(R.id.text2)
    void checkStatistic2() {
        intent = new Intent(mContext, ShareProfitActivity.class);
        startActivity(intent);
    }

    //消费记录
    @OnClick(R.id.consume_money)
    void consumeDetail(){
        intent=new Intent(mContext, ConsumeListActivity.class);
        startActivity(intent);
    }



   /* @OnClick(R.id.withdraw_money)
    void applyCash(){
        intent = new Intent(mContext, ApplyCashActivity.class);
        intent.putExtra("canApplyCash",String.format("%s",account.getNowTotalMoney().setScale(2,RoundingMode.HALF_UP)));
        startActivity(intent);
    }*/

    //设置提现账户
    @OnClick(R.id.menu1)
    void menu1() {
        intent = new Intent(mContext, AddAccountActivity.class);
        startActivity(intent);
    }

    //提现记录
    @OnClick(R.id.menu2)
    void menu2() {
        intent = new Intent(mContext, CashRecordActivity.class);
        startActivity(intent);
    }

    //股权书
    @OnClick(R.id.menu3)
    void menu3() {
//        intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra("url", "https://day-mobile.tiansheguoji.com/member/memberStock?memberId=" + memberId);
//        startActivity(intent);
        intent = new Intent(mContext, ReChargeActivity.class);
        startActivity(intent);
    }

    //投票
    @OnClick(R.id.menu4)
    void menu4() {
        intent = new Intent(mContext, VotingListActivity.class);
        startActivity(intent);
    }

    //反馈
    @OnClick(R.id.menu5)
    void menu5() {
        intent = new Intent(mContext, FeedBackActivity.class);
        startActivity(intent);
    }

    //设置
    @OnClick(R.id.menu6)
    void menu6() {
        intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }
    //收货地址
    @OnClick(R.id.menu7)
    void menu7() {
        intent = new Intent(mContext, AddressListActivity.class);
        startActivity(intent);
    }


}
