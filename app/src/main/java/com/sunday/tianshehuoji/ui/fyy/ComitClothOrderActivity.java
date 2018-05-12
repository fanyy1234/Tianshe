package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheMarket;
import com.sunday.tianshehuoji.model.Visitable;
import com.sunday.tianshehuoji.ui.product.BuyOrderActivity;
import com.sunday.tianshehuoji.ui.product.ConfirmBuyActivity;
import com.sunday.tianshehuoji.utils.EntityUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fyy on 2018/4/12.
 */

public class ComitClothOrderActivity extends BaseActivity implements View.OnClickListener{
    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;

    private Integer memberId;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.cloth_list)
    RecyclerView clothList;
    @Bind(R.id.commit_btn)
    LinearLayout commitBtn;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_mobile)
    TextView userMobile;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.address_info)
    LinearLayout addressInfo;
    @Bind(R.id.select_user)
    TextView selectUser;
    @Bind(R.id.xuanze)
    TextView xuanze;
    @Bind(R.id.body_name)
    TextView bodyName;
    @Bind(R.id.body_xiongwei)
    TextView bodyXiongwei;
    @Bind(R.id.body_yaowei)
    TextView bodyYaowei;
    @Bind(R.id.body_tunwei)
    TextView bodyTunwei;
    @Bind(R.id.body_yichang)
    TextView bodyYichang;
    @Bind(R.id.body_jiankuan)
    TextView bodyJiankuan;
    @Bind(R.id.body_xiuchang)
    TextView bodyXiuchang;
    @Bind(R.id.order_remark)
    ClearEditText orderRemark;
    @Bind(R.id.goods_sum)
    TextView goodsSum;
    @Bind(R.id.account_dikou)
    TextView accountDikou;
    @Bind(R.id.final_money)
    TextView finalMoney;
    @Bind(R.id.goods_num)
    TextView goodsNum;
    @Bind(R.id.btn_buy)
    TextView btnBuy;

    private Address address;
    private MemberSize memberSize;

    private Integer productId;
    private String shopId,shopType;
    private BigDecimal balance;
    private OrderConfirm orderConfirm;
    private String desc;
    public static ComitClothOrderActivity comitClothOrderActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_ordercommit);
        ButterKnife.bind(this);
        titleView.setText("提交订单");
        memberId = Integer.valueOf(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, ""));
        shopId = ClothingMakeActivity.shopId;
        shopType = ClothingMakeActivity.shopType;
        initView();
        getDefaultAddr();
    }

    private void initView() {
        comitClothOrderActivity = this;
        adapter = new MultiTypeAdapter(models, this);
        layoutManager = new LinearLayoutManager(this);
        clothList.setLayoutManager(layoutManager);
        clothList.setAdapter(adapter);
        productId = getIntent().getIntExtra("id",0);
        SubmitOrderProduct product = new SubmitOrderProduct();
        product.setName(getIntent().getStringExtra("name"));
        product.setNum(1);
        product.setGuige("白色规格");
        product.setPrice("￥"+getIntent().getStringExtra("price"));
        product.setImg(getIntent().getStringExtra("img"));
        models.add(product);
        finalMoney.setText("￥"+getIntent().getStringExtra("price"));
        goodsSum.setText("￥"+getIntent().getStringExtra("price"));
        adapter.notifyDataSetChanged();

        selectUser.setOnClickListener(this);
        xuanze.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        addressInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.address_info://选择收货人
                Intent intent1 = new Intent(this,AddressListActivity.class);
                intent1.putExtra("isSelectMode", true);
                startActivityForResult(intent1,1);
                break;
            case R.id.xuanze://选择尺寸
                Intent intent2 = new Intent(this,SelectMemberSizeActivity.class);
                intent2.putExtra("isSelectMode", true);
                startActivityForResult(intent2,2);
                break;
            case R.id.btn_buy:
                if (address==null){
                    ToastUtils.showToast(mContext,"请选择收货地址");
                    return;
                }
                if (memberSize==null){
                    ToastUtils.showToast(mContext,"请选择人员量体信息");
                    return;
                }
                desc = orderRemark.getText().toString().trim();
                orderConfirm();
                break;
            default:
                break;
        }
    }

    private final static int REQUEST_ADDR = 0x1111;
    private final static int REQUEST_COUNPON = 0x1112;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                address = (Address) data.getSerializableExtra("address");
                setAddr(address);
                break;
            case 2:
                memberSize = (MemberSize) data.getSerializableExtra("address");
                setMemberSize(memberSize);
                break;
        }
    }

    private void getDefaultAddr() {
        showLoadingDialog(0);
        Call<ResultDO<Address>> call = AppClient.getAppAdapter().getDefault(memberId);
        call.enqueue(new Callback<ResultDO<Address>>() {
            @Override
            public void onResponse(Call<ResultDO<Address>> call, Response<ResultDO<Address>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<Address> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    address = resultDO.getResult();
                    setAddr(address);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Address>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void setAddr(Address address) {
        if (address == null) {
            userName.setText("暂无收货地址");
            userMobile.setText("");
            userAddr.setText("请添加收货地址");
        } else {
            userName.setText("姓名:" + address.getName());
            userMobile.setText(address.getMobile());
            userAddr.setText(String.format("收货地址:%1s", address.getAddress()));
        }
    }
    private void setMemberSize(MemberSize memberSize) {
        if (memberSize != null) {
            bodyName.setText(memberSize.getName());
            bodyXiongwei.setText(memberSize.getXiongwei());
            bodyYaowei.setText(memberSize.getYaowei());
            bodyTunwei.setText(memberSize.getTunwei());
            bodyYichang.setText(memberSize.getYichang());
            bodyJiankuan.setText(memberSize.getJiankuan());
            bodyXiuchang.setText(memberSize.getXiuchang());
        }
    }

    private String getJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("productId", productId);
            jsonObject.put("num", 1);
            jsonArray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    private void orderConfirm() {
        Call<ResultDO<OrderConfirm>> call = AppClient.getAppAdapter().OrderConfirm(memberId.toString(), shopId, null, null, null, null,
                null, shopType, getJson());
        call.enqueue(new Callback<ResultDO<OrderConfirm>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderConfirm>> call, Response<ResultDO<OrderConfirm>> response) {
                ResultDO<OrderConfirm> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    orderConfirm = resultDO.getResult();
                    balance = orderConfirm.getCanUseMoney();
                    showPayWindow();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<OrderConfirm>> call, Throwable t) {

            }
        });
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
                    Intent intent = new Intent(ComitClothOrderActivity.this,BuyOrderActivity.class);
                    intent.putExtra("cartId",orderConfirm.getId());
                    intent.putExtra("linkPhone",address.getMobile());
                    intent.putExtra("linkName",address.getName());
                    intent.putExtra("desc",desc);
                    intent.putExtra("addressId",Integer.valueOf(address.getId()));
                    intent.putExtra("sizeId",Integer.valueOf(memberSize.getId()));
                    intent.putExtra("payFlag",4);
                    startActivity(intent);
                }
            }
        });
    }
    private void buyWithBalance(){
        showLoadingDialog(0);
        Call<ResultDO<String>> call = AppClient.getAppAdapter().createOrder(Integer.parseInt(orderConfirm.getId()), address.getMobile(), address.getName(), desc,null,address.getId(),memberSize.getId());
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
