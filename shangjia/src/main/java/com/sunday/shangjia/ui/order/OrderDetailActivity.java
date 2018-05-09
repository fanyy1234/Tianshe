package com.sunday.shangjia.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.sunday.member.base.BaseActivity;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.OrderDetailProductAdapter;
import com.sunday.shangjia.entity.Const;
import com.sunday.shangjia.entity.Order;
import com.sunday.shangjia.entity.OrderDetail;
import com.sunday.shangjia.http.AppClient;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/29.
 */

public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.user_mobile)
    TextView userMobile;
    @Bind(R.id.txt1)
    TextView txt1;
    @Bind(R.id.txt2)
    TextView txt2;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.origin_money)
    TextView origin_money;
    @Bind(R.id.order_no)
    TextView orderNo;
    @Bind(R.id.btn_cancel)
    TextView btnCancel;
    @Bind(R.id.btn_confirm)
    TextView btnConfirm;
    @Bind(R.id.list_view)
    NoScrollListview listView;

    private String json;
    private Order order;
    private OrderDetail orderDetail;

    private OrderDetailProductAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        titleView.setText("核销扫描结果");
        getOrder();

    }

    private void getOrder() {
        json = getIntent().getStringExtra("result");
        Gson gson = new Gson();
        order = gson.fromJson(json, Order.class);
        getOrderDetail();
    }

    @OnClick(R.id.btn_cancel)
    void cancel() {
        finish();
    }

    private void getOrderDetail() {
        Call<ResultDO<OrderDetail>> call = AppClient.getAppAdapter().getOrderDetail(order.getOrderNo());
        call.enqueue(new Callback<ResultDO<OrderDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderDetail>> call, Response<ResultDO<OrderDetail>> response) {
                ResultDO<OrderDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    orderDetail = resultDO.getResult();
                    updateView();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<OrderDetail>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }


    private void updateView() {
        if (orderDetail == null) {
            return;
        }
        userName.setText(orderDetail.getLinkName());
        userMobile.setText(orderDetail.getLinkMobile());
        if (orderDetail.getShopType().equals(Const.TYPE4)) {
            txt1.setText(String.format("美容师:%s", orderDetail.getChooseWaiterName()));
            txt2.setText(String.format("房间类型:%s", orderDetail.getChooseServiceName()));
        } else if (orderDetail.getShopType().equals(Const.TYPE9) || orderDetail.getShopType().equals(Const.TYPE10)) {
            txt1.setText(String.format("美容师:%s", orderDetail.getChooseWaiterName()));
        } else if (orderDetail.getShopType().equals(Const.TYPE2) || orderDetail.getShopType().equals(Const.TYPE3)
                || orderDetail.getShopType().equals(Const.TYPE7) || orderDetail.getShopType().equals(Const.TYPE11)) {
            txt1.setText(String.format("入住时间:%s", orderDetail.getArriveTime()));
            txt2.setText(String.format("离店时间:%s", orderDetail.getLeaveTime()));
        } else {
            txt1.setVisibility(View.GONE);
            txt2.setVisibility(View.GONE);
        }

        adapter = new OrderDetailProductAdapter(mContext, orderDetail.getProduct());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        origin_money.setText(String.format("总价:￥%1s\n\n优惠金额:￥%2s", orderDetail.getTotalMoney().setScale(2, RoundingMode.HALF_UP)
                , orderDetail.getDiscountPrice().setScale(2, RoundingMode.HALF_UP)));
        totalMoney.setText(String.format("实付:￥%s",orderDetail.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        orderNo.setText(String.format("订单号:%s",orderDetail.getOrderNo()));


    }

    @OnClick(R.id.btn_confirm)
    void confirm() {
        Call<ResultDO> call = AppClient.getAppAdapter().orderVerificationNo(order.getOrderNo());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    ToastUtils.showToast(mContext, "核销成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
}
