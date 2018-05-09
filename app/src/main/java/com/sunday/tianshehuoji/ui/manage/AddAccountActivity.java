package com.sunday.tianshehuoji.ui.manage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.CardType;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.io.FileDescriptor.in;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class AddAccountActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.account_name)
    EditText accountName;
    @Bind(R.id.account_no)
    EditText accountNo;
    @Bind(R.id.account_type)
    TextView accountType;

    List<CardType> typeList = new ArrayList<>();
    String memberId;
    CashAccount cashAccount;
    String id="0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ButterKnife.bind(this);
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        getAccountList();
        getData();

    }

    private void getAccountList(){
        Call<ResultDO<List<CashAccount>>> call = AppClient.getAppAdapter().getAccountList(memberId);
        call.enqueue(new Callback<ResultDO<List<CashAccount>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CashAccount>>> call, Response<ResultDO<List<CashAccount>>> response) {
                if (isFinish) {
                    return;
                }
                ResultDO<List<CashAccount>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0 && resultDO.getResult() != null) {
                    if (resultDO.getResult().size()>0){
                        cashAccount=resultDO.getResult().get(0);
                    }
                    initView();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<CashAccount>>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void initView(){
      if (cashAccount==null){
          id="0";
          titleView.setText("添加账户");
          return;
      }
        titleView.setText("修改账户");
        id=cashAccount.getId();
        accountName.setText(cashAccount.getAccountName());
        accountNo.setText(cashAccount.getAccount());
        accountType.setText(cashAccount.getBank());
        accountId=cashAccount.getBankId();
    }


    @OnClick(R.id.bth_pay)
    void save() {
        String name = accountName.getText().toString().trim();
        String account = accountNo.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(account)) {
            return;
        }
        if (TextUtils.isEmpty(accountId)) {
            ToastUtils.showToast(mContext, "请选择开户行");
            return;
        }

        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().saveAccount(id,memberId, accountId,account,name);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "添加成功");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {

            }
        });


    }

    String[] item;
    String accountId;
    String bankName;

    @OnClick(R.id.account_type)
    void selectType() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accountType.setText(item[which]);
                accountId = typeList.get(which).getId();
                bankName = typeList.get(which).getName();
            }
        });
        builder.create().show();
    }


    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<CardType>>> call = AppClient.getAppAdapter().getBankList();
        call.enqueue(new Callback<ResultDO<List<CardType>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CardType>>> call, Response<ResultDO<List<CardType>>> response) {
                dissMissDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0 && response.body().getResult() != null) {
                    typeList.clear();
                    typeList.addAll(response.body().getResult());
                    item = new String[typeList.size()];
                    for (int i = 0; i < typeList.size(); i++) {
                        item[i] = typeList.get(i).getName();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<CardType>>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, "网络错误");
            }
        });
    }


}
