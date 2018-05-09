package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.Visitable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class ComitClothOrderActivity extends BaseActivity implements View.OnClickListener{
    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;

    String memberId;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_ordercommit);
        ButterKnife.bind(this);
        titleView.setText("提交订单");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        initView();
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
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.select_user://选择收货人
                Intent intent1 = new Intent(this,AddressListActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.xuanze://选择尺寸
                Intent intent2 = new Intent(this,SelectMemberSizeActivity.class);
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
            case REQUEST_ADDR:
                Address address = (Address) data.getSerializableExtra("address");
                setAddr(address);
                break;
//            case REQUEST_COUNPON:
//                voucherId = data.getIntExtra("id", 0);
//                couponMoney = data.getDoubleExtra("money", 0);
//                reCountTotalMoney();
//                break;
        }
    }

    private void setAddr(Address address) {
        if (address == null) {
            userName.setText("暂无收货地址");
            userMobile.setText("");
            userAddr.setText("请添加收货地址");
        } else {
            userName.setText("姓名:" + address.getName());
            userMobile.setText(address.getMobile());
            userAddr.setText(String.format("收货地址:%1s%2s", address.getCityDetail(), address.getAddress()));

        }
    }
}
