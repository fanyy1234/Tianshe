package com.sunday.tianshehuoji.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.CircleImageView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.ImgUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/19.
 * 提现
 */

public class ApplyCashActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.img)
    CircleImageView img;
    @Bind(R.id.tv_bank_name)
    TextView tvBankName;
    @Bind(R.id.tv_bank_card)
    TextView tvBankCard;
    @Bind(R.id.l_select)
    LinearLayout lSelect;
    @Bind(R.id.tv_usable_count)
    TextView tvUsableCount;
    @Bind(R.id.tv_count)
    EditText tvCount;
    @Bind(R.id.btn_getcash)
    Button btnGetcash;

    CashAccount account;
    String memberId;
    String totalMoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_cash);
        ButterKnife.bind(this);
        titleView.setText("申请提现");
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        totalMoney=getIntent().getStringExtra("canApplyCash");
        tvUsableCount.setText(String.format("可提现金额 %s",totalMoney));
        lSelect.setVisibility(View.GONE);

    }

    final static int REQUEST_ACCOUNT=0x111;
    @OnClick(R.id.txt_select_account)
    void selectAccount(){
        intent=new Intent(mContext,AccountListActivity.class);
        intent.putExtra("isSelect",true);
        startActivityForResult(intent,REQUEST_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){return;}
        if (requestCode==REQUEST_ACCOUNT){
            account= (CashAccount) data.getSerializableExtra("account");
            updateView();

        }
    }

    private void updateView(){
        lSelect.setVisibility(View.VISIBLE);
        tvBankName.setText(account.getBank());
        tvBankCard.setText(String.format("%1s  %2s",account.getAccountName(),account.getAccount()));
        if (!TextUtils.isEmpty(account.getBankLogo())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(account.getBankLogo()))
                    .into(img);
        }

    }

    @OnClick(R.id.btn_getcash)
    void submit(){
        if (account==null){
            ToastUtils.showToast(mContext,"请选择账户");
            return;
        }
        String money=tvCount.getText().toString().trim();
        if (TextUtils.isEmpty(money)){
            ToastUtils.showToast(mContext,"请填写提现金额");
            return;
        }
        if (Double.parseDouble(money)>Double.parseDouble(totalMoney)){
            ToastUtils.showToast(mContext,"超出最大提现金额！");
            return;
        }

        Call<ResultDO> call= AppClient.getAppAdapter().applyCash(memberId,money,account.getAccountName(),account.getBank(),account.getAccount());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"申请成功");
                }else {
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });


    }
}
