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
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.entity.Account;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.CartItem;
import com.sunday.tianshehuoji.entity.CartListItem;
import com.sunday.tianshehuoji.entity.CartTotal;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.Visitable;
import com.sunday.tianshehuoji.ui.product.BuyOrderActivity;
import com.sunday.tianshehuoji.utils.EntityUtil;

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

public class ComitOrderActivity extends BaseActivity implements View.OnClickListener{
    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;

    Integer memberId;
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
    private int cartId;
    private String desc;
    private BigDecimal balance;
    private Integer realMoney;
    public static ComitOrderActivity comitOrderActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comit_order);
        ButterKnife.bind(this);
        titleView.setText("提交订单");
        memberId = Integer.valueOf(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,""));
        initView();
        getDefaultAddr();
    }

    private void initView() {
        comitOrderActivity = this;
        adapter = new MultiTypeAdapter(models, this);
        layoutManager = new LinearLayoutManager(this);
        clothList.setLayoutManager(layoutManager);
        clothList.setAdapter(adapter);
        getData();

        selectUser.setOnClickListener(this);
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
            case R.id.btn_buy:
                if (address==null){
                    ToastUtils.showToast(mContext,"请选择收货地址");
                    return;
                }
                desc = orderRemark.getText().toString().trim();
                showPayWindow();
                break;
            default:
                break;
        }
    }

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

    public void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<CartTotal>>> call = AppClient.getAppAdapter().getCartList(memberId);
        call.enqueue(new Callback<ResultDO<List<CartTotal>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CartTotal>>> call, Response<ResultDO<List<CartTotal>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                ResultDO<List<CartTotal>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    if (resultDO.getResult().get(0).getCartItem() != null && !resultDO.getResult().get(0).getCartItem().isEmpty()) {
                        int sumPrice = 0;
                        for (int i = 0; i < resultDO.getResult().get(0).getCartItem().size(); i++) {
                            CartItem item = resultDO.getResult().get(0).getCartItem().get(i);
                            SubmitOrderProduct product = new SubmitOrderProduct();
                            product.setName(item.getName());
                            product.setNum(item.getNum());
                            product.setGuige("");
                            product.setImg(item.getLogo()==null?"000":item.getLogo());
                            product.setPrice("￥"+item.getPrice());
                            sumPrice += item.getPrice()*item.getNum();
                            models.add(product);
                            cartId = item.getCartId();
                        }
                        goodsSum.setText("￥"+ sumPrice);
                        finalMoney.setText("￥"+ sumPrice);
                        realMoney = sumPrice;
                    }
                    adapter.notifyDataSetChanged();
                    getMemberDetail();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<CartTotal>>> call, Throwable t) {
                dissMissDialog();
            }
        });
    }
    private void getMemberDetail() {
        Call<ResultDO<Account>> call = AppClient.getAppAdapter().getMemberById(memberId.toString());
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
                if (balance.compareTo(BigDecimal.valueOf(realMoney))>=0){
                    buyWithBalance();
                }
                else {
                    Intent intent = new Intent(ComitOrderActivity.this,BuyOrderActivity.class);
                    intent.putExtra("cartId",cartId+"");
                    intent.putExtra("linkPhone",address.getMobile());
                    intent.putExtra("linkName",address.getName());
                    intent.putExtra("desc",desc);
                    intent.putExtra("addressId",Integer.valueOf(address.getId()));
                    intent.putExtra("addressId",3);
                    startActivity(intent);
                }
            }
        });
    }
    private void buyWithBalance(){
        showLoadingDialog(0);
        Call<ResultDO<String>> call = AppClient.getAppAdapter().createOrder(cartId, address.getMobile(), address.getName(), desc,null,address.getId(),null);
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
