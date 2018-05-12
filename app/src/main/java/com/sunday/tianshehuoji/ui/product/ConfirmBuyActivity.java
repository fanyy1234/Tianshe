package com.sunday.tianshehuoji.ui.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.common.widgets.NoScrollListview;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.dialog.ACProgressFlower;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.ConfirmBuyProductAdapter;
import com.sunday.tianshehuoji.alipay.PayResult;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.DateUtils;
import com.sunday.tianshehuoji.utils.EntityUtil;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2016/12/19.
 */

public class ConfirmBuyActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.product_title)
    TextView productTitle;
    @Bind(R.id.txt1)
    TextView txt1;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.txt2)
    TextView txt2;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.rl_select_time)
    RelativeLayout rlSelectTime;
    @Bind(R.id.product_type)
    TextView productType;
    @Bind(R.id.list_view)
    NoScrollListview listView;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.actual_money)
    TextView actualMoney;
    @Bind(R.id.room_type)
    TextView roomType;
    @Bind(R.id.rl_room_type)
    RelativeLayout rlRoomType;
    @Bind(R.id.service_man)
    TextView serviceMan;
    @Bind(R.id.rl_spa_man)
    RelativeLayout rlSpaMan;
    @Bind(R.id.txt_contact)
    TextView txtContact;
    @Bind(R.id.edit_contact)
    ClearEditText editContact;
    @Bind(R.id.txt_contact_phone)
    TextView txtContactPhone;
    @Bind(R.id.edit_contact_phone)
    ClearEditText editContactPhone;
    @Bind(R.id.edit_remark)
    ClearEditText editRemark;
    @Bind(R.id.bottom_total_money)
    TextView bottomTotalMoney;
    @Bind(R.id.btn_confirm_buy)
    Button btnConfirmBuy;
    @Bind(R.id.desk_number)
    TextView deskNumber;
    @Bind(R.id.select_desk)
    LinearLayout selectDesk;

    private String shopType;
    private String shopName;
    private OrderConfirm orderConfirm;
    private ConfirmBuyProductAdapter adapter;
    private BigDecimal balance;
    private Account account;
    private String alipayInfo;
    List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

    public static ConfirmBuyActivity confirmBuyActivity;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy);
        ButterKnife.bind(this);
        titleView.setText("确认购买");
        shopType = getIntent().getStringExtra("shopType");
        shopName = getIntent().getStringExtra("shopName");
        orderConfirm = (OrderConfirm) getIntent().getSerializableExtra("orderConfirm");
        account = (Account) SharePerferenceUtils.getIns(mContext).getOAuth();
        balance = orderConfirm.getCanUseMoney();

        for (int i = 0; i < 50; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            listem.put("deskNo", i+1);
            listems.add(listem);
        }
        selectDesk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopwindow(selectPayTypeView());
            }
        });
        updateView();
    }

    private void updateView() {
        api = WXAPIFactory.createWXAPI(this, com.sunday.tianshehuoji.wxapi.Constants.APP_ID);
        api.registerApp(com.sunday.tianshehuoji.wxapi.Constants.APP_ID);
        confirmBuyActivity = this;

        editContact.setText(account.getRealName());
        editContactPhone.setText(account.getMobile());
        if (orderConfirm == null) {
            return;
        }
        productTitle.setText(shopName);
        if (shopType.equals(Const.TYPE2) || shopType.equals(Const.TYPE11)) {
            rlSelectTime.setVisibility(View.VISIBLE);
            txt1.setText("入住");
            txt2.setText("离店");
            date.setText(DateUtils.dateToStr1(DateUtils.strToDate(orderConfirm.getStartTime())));
            time.setText(DateUtils.dateToStr1(DateUtils.strToDate(orderConfirm.getStartTime())));
        } else if (shopType.equals(Const.TYPE3) || shopType.equals(Const.TYPE7)) {
            rlSelectTime.setVisibility(View.VISIBLE);
            txt1.setText("日期");
            txt2.setText("时间段");
            date.setText(DateUtils.dateToStr1(DateUtils.strToDate(orderConfirm.getStartTime())));
            time.setText(String.format("%1s-%2s", DateUtils.dateToStr2(DateUtils.strToDate(orderConfirm.getStartTime())),
                    DateUtils.dateToStr2(DateUtils.strToDate(orderConfirm.getEndTime()))));
        }
        adapter = new ConfirmBuyProductAdapter(mContext, orderConfirm.getIteamList());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        totalMoney.setText(String.format("总价:￥%1s\n\n优惠金额:￥%2s", orderConfirm.getTotalMoney().setScale(2, RoundingMode.HALF_UP)
                , orderConfirm.getDiscountPrice().setScale(2, RoundingMode.HALF_UP)));
        actualMoney.setText(String.format("￥%s", orderConfirm.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        if (shopType.equals(Const.TYPE4)) {
            rlRoomType.setVisibility(View.VISIBLE);
            rlSpaMan.setVisibility(View.VISIBLE);
            roomType.setText(orderConfirm.getChooseServiceName());
            serviceMan.setText(orderConfirm.getChooseWaiterName());
        } else if (shopType.equals(Const.TYPE9) || shopType.equals(Const.TYPE10)) {
            rlSpaMan.setVisibility(View.VISIBLE);
            serviceMan.setText(orderConfirm.getChooseWaiterName());
        }
        bottomTotalMoney.setText(String.format("订单金额:￥%s", orderConfirm.getRealMoney()));
        if (shopType.equals(Const.TYPE8)) {
            editRemark.setHint(R.string.desc);
        }

    }


    //余额
    private void getBalance() {
        String memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        Call<ResultDO<Account>> call = AppClient.getAppAdapter().getMemberById(memberId);
        call.enqueue(new Callback<ResultDO<Account>>() {
            @Override
            public void onResponse(Call<ResultDO<Account>> call, Response<ResultDO<Account>> response) {
                ResultDO<Account> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    balance = resultDO.getResult().getEnergyBean();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Account>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    String linkName;
    String linkPhone;
    String desc;

    @OnClick(R.id.btn_confirm_buy)
    void confirmBuy() {
        linkName = editContact.getText().toString().trim();
        linkPhone = editContactPhone.getText().toString().trim();
        desc = editRemark.getText().toString().trim();
        if (TextUtils.isEmpty(linkName) || TextUtils.isEmpty(linkPhone)) {
            ToastUtils.showToast(mContext, "请填写联系人姓名和电话");
            return;
        }
        if (!StringUtils.isMobileNO(linkPhone)) {
            ToastUtils.showToast(mContext, "请填写联系人正确电话");
            return;
        }
//        if (shopType.equals(Const.TYPE8)) {
//            String deskNumberStr = deskNumber.getText().toString();
//            if (deskNumberStr.equals("")){
//                ToastUtils.showToast(mContext, "请选择桌号");
//                return;
//            }
//            desc = deskNumberStr+"号桌"+desc;
//        }

        showPayWindow();

    }

    private void showPayWindow() {
        if (balance == null) {
            return;
        }
        final UIAlertView view = new UIAlertView(mContext, "确认支付", String.format("当前可用余额:%s", balance.setScale(2, RoundingMode.HALF_UP)),
                "取消", "确定");
        view.show();
        view.setClicklistener(new UIAlertView.ClickListenerInterface() {
            @Override
            public void doLeft() {
                view.dismiss();
            }

            @Override
            public void doRight() {
                view.dismiss();
                if (balance.compareTo(orderConfirm.getRealMoney())>=0){
                    buyWithBalance();
                }
                else {
                    Intent intent = new Intent(ConfirmBuyActivity.this,BuyOrderActivity.class);
                    intent.putExtra("cartId",orderConfirm.getId());
                    intent.putExtra("linkPhone",linkPhone);
                    intent.putExtra("linkName",linkName);
                    intent.putExtra("desc",desc);
                    intent.putExtra("payFlag",2);
                    startActivity(intent);
                }
            }
        });
    }

    public void showLoadingDialog(int res) {
        if(progressDialog!=null  &&  progressDialog.isShowing()){
            progressDialog.cancel();
        }
        progressDialog = new ACProgressFlower.Builder(this)
                .text(res == 0 ? "正在加载中..." : getString(res))
                .build();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dissMissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //设置屏幕背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    private PopupWindow popupWindow;
    private void showPopwindow(View view) {
        backgroundAlpha(0.7f);
        popupWindow = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        popupWindow.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
//        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        popupWindow.showAtLocation(
                this.findViewById(R.id.select_desk),
                Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }
    private View selectPayTypeView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_selectdesk, null);
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.item_selectdesk, new String[] { "deskNo" },
                new int[] {R.id.desk_no});

        ListView lt1=(ListView) view.findViewById(R.id.desk_list);
        lt1.setAdapter(simplead);
        lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deskNumber.setText(i+1+"");
                popupWindow.dismiss();
            }
        });
        return view;
    }

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
                        ToastUtils.showToast(mContext, "支付成功");
                        BaseApplication.getInstance().setConsumed(true);//余额变动
                        finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (resultStatus.equals("8000")) {
                            ToastUtils.showToast(mContext,"支付结果确认中");
                        }
                        else if (resultStatus.equals("6001")) {
                            ToastUtils.showToast(mContext,"支付已取消");
                            finish();
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
                            finish();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    private void buyWithBalance(){
        showLoadingDialog(0);
        Call<ResultDO<String>> call = AppClient.getAppAdapter().createOrder(Integer.parseInt(orderConfirm.getId()), linkPhone, linkName, desc,null,null,null);
        call.enqueue(new Callback<ResultDO<String>>() {
            @Override
            public void onResponse(Call<ResultDO<String>> call, Response<ResultDO<String>> response) {
                if (response.body() == null) {
                    dissMissDialog();
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonResult = EntityUtil.ObjectToJson3(response.body());

                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext, "支付成功");
                    BaseApplication.getInstance().setConsumed(true);//余额变动
                    finish();
                }
                else {
                    dissMissDialog();
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<String>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
}
