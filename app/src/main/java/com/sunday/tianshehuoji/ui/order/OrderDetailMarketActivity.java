package com.sunday.tianshehuoji.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.adapter.OrderDetailProductAdapter;
import com.sunday.tianshehuoji.entity.OrderDetail;
import com.sunday.tianshehuoji.entity.OrderResult;
import com.sunday.tianshehuoji.entity.order.OrderItem;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.Visitable;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class OrderDetailMarketActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.order_status)
    TextView orderStatus;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_mobile)
    TextView userMobile;
    @Bind(R.id.user_addr)
    TextView userAddr;
    @Bind(R.id.cloth_list)
    RecyclerView clothList;
    @Bind(R.id.price_sum)
    TextView priceSum;
    @Bind(R.id.discount_money)
    TextView discountMoney;
    @Bind(R.id.real_money)
    TextView realMoney;
    @Bind(R.id.size)
    TextView size;
    @Bind(R.id.size_info)
    LinearLayout sizeInfo;
    @Bind(R.id.order_no)
    TextView orderNo;
    @Bind(R.id.create_time)
    TextView createTime;
    @Bind(R.id.pay_time)
    TextView payTime;
    @Bind(R.id.send_time)
    TextView sendTime;
    @Bind(R.id.btn_cancel)
    TextView btnCancel;
    @Bind(R.id.btn_buy)
    TextView btnBuy;
    @Bind(R.id.commit_btn)
    LinearLayout commitBtn;

    private int orderId;
    OrderResult orderResult;
    OrderDetail orderDetail;
    int appWidth;
    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;
//    0代付款1未发货2待评价3已完成4配送中
    private String[] statusArr = {"待付款","未发货","待评价","已完成","配送中"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_market);
        ButterKnife.bind(this);
        titleView.setText("订单详情");
        orderId = getIntent().getIntExtra("orderId", 0);
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        adapter = new MultiTypeAdapter(models, this);
        layoutManager = new LinearLayoutManager(this);
        clothList.setLayoutManager(layoutManager);
        clothList.setAdapter(adapter);
        getData();
    }

    private void getData() {
        Call<ResultDO<OrderResult>> call = AppClient.getAppAdapter().getOrderDetailByOrderId(orderId);
        call.enqueue(new Callback<ResultDO<OrderResult>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderResult>> call, Response<ResultDO<OrderResult>> response) {
                ResultDO<OrderResult> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    orderResult = resultDO.getResult();
                    orderDetail = orderResult.getOrder();
                    updateView();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<OrderResult>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void updateView() {
        if (orderDetail == null) {
            return;
        }
        orderStatus.setText(statusArr[orderDetail.getStatus()]);
        if (orderDetail.getStatus()==1){
            btnBuy.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
        else {
            btnBuy.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
        priceSum.setText(String.format("￥%s", orderDetail.getTotalMoney().setScale(2, RoundingMode.HALF_UP)));
        realMoney.setText(String.format("￥%s", orderDetail.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        discountMoney.setText(String.format("￥%s", orderDetail.getDiscountPrice().setScale(2, RoundingMode.HALF_UP)));
//        textRecord.setText(String.format("备注：%s", orderDetail.getDesc()));
        orderNo.setText(orderDetail.getOrderNo());
        createTime.setText(orderDetail.getCreateTime());
        payTime.setText(orderDetail.getPayTime());
        userName.setText(orderDetail.getReceiveName());
        userMobile.setText(orderDetail.getReceiveMobile());
        userAddr.setText(orderDetail.getReceiveAddress());
        List<OrderItem> orderItems = orderDetail.getOrderItemList();
        for (OrderItem item : orderItems){
            SubmitOrderProduct product = new SubmitOrderProduct();
            product.setName(item.getName());
            product.setNum(item.getNum());
            product.setGuige("");
            product.setImg(item.getLogo()==null?"000":item.getLogo());
            product.setPrice("￥"+item.getPrice());
            models.add(product);
        }
        adapter.notifyDataSetChanged();
    }

}
