package com.sunday.tianshehuoji.ui.order;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.ConfirmBuyProductAdapter;
import com.sunday.tianshehuoji.adapter.OrderDetailProductAdapter;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.order.OrderDetail;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.math.RoundingMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class OrderDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.txt1)
    TextView txt1;
    @Bind(R.id.txt2)
    TextView txt2;
    @Bind(R.id.txt3)
    TextView txt3;
    @Bind(R.id.txt4)
    TextView txt4;
    @Bind(R.id.txt_code)
    TextView txtCode;
    @Bind(R.id.code_img)
    ImageView codeImg;
    @Bind(R.id.text_QR_code)
    TextView textQRCode;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.actual_money)
    TextView actualMoney;
    @Bind(R.id.text_record)
    TextView textRecord;
    @Bind(R.id.order_no)
    TextView txt_orderNo;
    @Bind(R.id.order_create_time)
    TextView orderCreateTime;
    @Bind(R.id.txt)
    TextView txt;
    @Bind(R.id.order_txt_addr)
    TextView orderTxtAddr;
    @Bind(R.id.list_view)
    NoScrollListview listView;

    private String orderNo;
    OrderDetail orderDetail;
    int appWidth;
    private OrderDetailProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        titleView.setText("订单详情");
        orderNo = getIntent().getStringExtra("orderNo");
        appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
        getData();
    }

    private void getData() {
        Call<ResultDO<OrderDetail>> call = AppClient.getAppAdapter().getOrderDetail(orderNo);
        call.enqueue(new Callback<ResultDO<OrderDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderDetail>> call, Response<ResultDO<OrderDetail>> response) {
                ResultDO<OrderDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
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
        if (orderDetail.getShopType().equals(Const.TYPE4)) {
            txt1.setText(String.format("美容师:%s", orderDetail.getChooseWaiterName()));
            txt2.setText(String.format("房间类型:%s", orderDetail.getChooseServiceName()));
        } else if (orderDetail.getShopType().equals(Const.TYPE9) || orderDetail.getShopType().equals(Const.TYPE10)) {
            txt1.setText(String.format("美容师:%s", orderDetail.getChooseWaiterName()));
        } else if (orderDetail.getShopType().equals(Const.TYPE2) || orderDetail.getShopType().equals(Const.TYPE3)
                || orderDetail.getShopType().equals(Const.TYPE7) || orderDetail.getShopType().equals(Const.TYPE11)) {
            txt1.setText(String.format("入住时间:%s", orderDetail.getArriveTime()));
            txt2.setText(String.format("离店时间:%s", orderDetail.getLeaveTime()));
        }else {
            txt1.setVisibility(View.GONE);
            txt2.setVisibility(View.GONE);
        }
        txt3.setText(String.format("商铺类型:%s", orderDetail.getShopName()));
        switch (orderDetail.getStatus()) {
            case 1:
                txt4.setText("未使用");
                break;
            case 2:
                txt4.setText("待评价");
                break;
            case 3:
                txt4.setText("已完成");
                break;
        }
        txtCode.setText(String.format("核销码:%s", orderDetail.getCancelNo()));
        if (!TextUtils.isEmpty(orderDetail.getTwocodeUrl())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(orderDetail.getTwocodeUrl()))
                    .resize(appWidth/5,appWidth/5)
                    .centerCrop()
                    .into(codeImg);
        }
        totalMoney.setText(String.format("总价:￥%1s\n\n优惠金额:￥%2s", orderDetail.getTotalMoney().setScale(2, RoundingMode.HALF_UP)
                , orderDetail.getDiscountPrice().setScale(2, RoundingMode.HALF_UP)));
        actualMoney.setText(String.format("￥%s", orderDetail.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        textRecord.setText(String.format("备注：%s", orderDetail.getDesc()));
        txt_orderNo.setText(orderDetail.getOrderNo());
        orderCreateTime.setText(orderDetail.getCreateTime());
        txt.setText(orderDetail.getShopName());
        orderTxtAddr.setText(orderDetail.getShopAddress());
        adapter = new OrderDetailProductAdapter(mContext, orderDetail.getProduct());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.call_shop)
    void callShop(){
        if (!TextUtils.isEmpty(orderDetail.getShopMobile()))
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + orderDetail.getShopMobile()));
        if (ActivityCompat.checkSelfPermission(mContext, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                    CALL_PHONE
            }, 0);

            return;
        }
        startActivity(intent);
    }
}
