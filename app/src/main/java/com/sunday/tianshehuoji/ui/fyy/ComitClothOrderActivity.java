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
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.Visitable;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_ordercommit);
        ButterKnife.bind(this);
        titleView.setText("提交订单");
        memberId = Integer.valueOf(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, ""));
        initView();
        getDefaultAddr();
    }

    private void initView() {
        adapter = new MultiTypeAdapter(models, this);
        layoutManager = new LinearLayoutManager(this);
        clothList.setLayoutManager(layoutManager);
        clothList.setAdapter(adapter);
        for (int i = 0; i < 3; i++) {
            SubmitOrderProduct product = new SubmitOrderProduct();
            product.setName("男士衬衫sssssssssssssssssssssssssssssss" + i);
            product.setNum(1);
            product.setGuige("白色Lssssssssssssss");
            product.setPrice("￥610");
            models.add(product);
        }
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
                Address address = (Address) data.getSerializableExtra("address");
                setAddr(address);
                break;
            case 2:
                MemberSize memberSize = (MemberSize) data.getSerializableExtra("address");
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
}
