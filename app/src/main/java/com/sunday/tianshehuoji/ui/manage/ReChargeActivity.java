package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.alipay.PayResult;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.EntityUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/19.
 * 充值
 */

public class ReChargeActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_recharge)
    ClearEditText editRecharge;
    @Bind(R.id.alipay)
    CheckBox alipay;
    @Bind(R.id.wechat_pay)
    CheckBox wechatPay;
    @Bind(R.id.balance_pay)
    CheckBox balancePay;
    @Bind(R.id.bth_pay)
    Button bthPay;

    int payType=1;//1支付宝2微信
    int memberId=0;
    int money;
    String alipayInfo;
    public static ReChargeActivity reChargeActivity;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        api = WXAPIFactory.createWXAPI(this, com.sunday.tianshehuoji.wxapi.Constants.APP_ID);
        api.registerApp(com.sunday.tianshehuoji.wxapi.Constants.APP_ID);
        reChargeActivity = this;
        String memberIdStr = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        try {
            memberId = Integer.valueOf(memberIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        titleView.setText("充值");
        alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    payType = 1;
                    wechatPay.setChecked(false);
                }
            }
        });
        wechatPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    payType = 2;
                    alipay.setChecked(false);
                }
            }
        });
        bthPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payType==0){
                    ToastUtils.showToast(mContext,"请选择支付方式");
                    return;
                }
                if(memberId!=0){
                    String moneyStr = editRecharge.getText().toString();
                    if (moneyStr.equals("")){
                        ToastUtils.showToast(mContext,"请输入充值金额");
                        return;
                    }
                    try {
                        money = Integer.parseInt(moneyStr);
                    } catch (NumberFormatException e) {
                        ToastUtils.showToast(mContext,"充值金额太大");
                        return;
                    }
                    if (money==0){
                        ToastUtils.showToast(mContext,"请输入充值金额");
                        return;
                    }
                    if (payType==1){
                        recharge();
                    }
                    else if (payType==2){
                        rechargeWx();
                    }
                }
                else {
                    ToastUtils.showToast(mContext,"用户id为0");
                }
            }
        });
    }

    private void recharge(){
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().recharge(memberId,money,payType);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    dissMissDialog();
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonResult = EntityUtil.ObjectToJson3(response.body());
                if (resultDO.getCode() == 0) {
                    alipayInfo = jsonResult.getString("result");
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ReChargeActivity.this);
                            Map<String,String> result = alipay.payV2(alipayInfo,true);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            mAliHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();

                } else {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
    private void rechargeWx(){
        showLoadingDialog(0);
        Call<ResultDO> call = AppClient.getAppAdapter().recharge(memberId,money,payType);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    dissMissDialog();
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonResult = EntityUtil.ObjectToJson3(response.body());
                if (resultDO.getCode() == 0) {
                    JSONObject json = JSONObject.parseObject(jsonResult.getString("result"));
                    PayReq req = new PayReq();
                    req.appId			= json.getString("appId");
                    req.partnerId		= json.getString("machId");
                    req.prepayId		= json.getString("prepayId");
                    req.nonceStr		= json.getString("nonceStr");
                    req.timeStamp		= json.getString("timeStamp");
                    req.packageValue	= "Sign=WXPay";
                    req.sign			= json.getString("paySign");
                    req.signType       = "MD5";
//                    req.extData			= "app data"; // optional
                    BaseApplication.setPayFlag(1);
                    api.sendReq(req);

                } else {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

//    @SuppressLint("HandlerLeak")
    private Handler mAliHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    dissMissDialog();
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (resultStatus.equals("9000")) {
//                        RefreshFlag.setWallet(1);
                        ToastUtils.showToast(mContext,"充值成功");
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (resultStatus.equals("8000")) {
                            ToastUtils.showToast(mContext,"支付结果确认中");
                        }
                        else if (resultStatus.equals("6001")) {
                            ToastUtils.showToast(mContext,"支付已取消");
                        }
                        else if (resultStatus.equals("6002")) {
                            ToastUtils.showToast(mContext,"网络连接出错");
                        }
                        else if (resultStatus.equals("5000")) {
                            ToastUtils.showToast(mContext,"重复请求");
                        }
                        else if (resultStatus.equals("6004")) {
                            ToastUtils.showToast(mContext,"6004");
                        }
                        else {
                            ToastUtils.showToast(mContext,"充值失败"+resultStatus+resultInfo);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
}
