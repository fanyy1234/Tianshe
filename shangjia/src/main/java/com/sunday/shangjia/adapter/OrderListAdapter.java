package com.sunday.shangjia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.test.FlakyTest;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.NoScrollListview;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Const;
import com.sunday.shangjia.entity.Order;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.order.QrcodeVerificationActivity;
import com.sunday.shangjia.util.DateUtils;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.sunday.shangjia.R.id.cancel;
import static com.sunday.shangjia.R.id.list_view;
import static com.sunday.shangjia.R.id.txt1;
import static com.sunday.shangjia.R.id.txt2;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class OrderListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Order> dataSet;
    private int appWidth;
    String memberId;
    private OrderProductAdapter adapter;

    public OrderListAdapter(Context context) {
        this.mContext = context;
    }

    public OrderListAdapter(Context context, List<Order> data) {
        this.mContext = context;
        this.dataSet = data;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_order_item, null);
        return new ServiceOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceOrderHolder viewHolder = (ServiceOrderHolder) holder;
        Order item = dataSet.get(position);
        viewHolder.userName.setText(item.getLinkName());
        viewHolder.userMobile.setText(item.getLinkMobile());
        //这3个需区分shopType
        if (item.getShopType().equals(Const.TYPE4)) {
            viewHolder.txt1.setText(String.format("美容师:%s", item.getChooseWaiterName()));
            viewHolder.txt2.setText(String.format("房间类型:%s", item.getChooseServiceName()));
        } else if (item.getShopType().equals(Const.TYPE9) || item.getShopType().equals(Const.TYPE10)) {
            viewHolder.txt1.setText(String.format("美容师:%s", item.getChooseWaiterName()));
        } else if (item.getShopType().equals(Const.TYPE2) || item.getShopType().equals(Const.TYPE3)
                || item.getShopType().equals(Const.TYPE7) || item.getShopType().equals(Const.TYPE11)) {
            if (item.getStartTime()==null){
                viewHolder.txt2.setText(String.format("入住时间:%s", ""));
            }else {
                viewHolder.txt1.setText(String.format("入住时间:%s", DateUtils.dateToStr1(DateUtils.strToDate(item.getStartTime()))));
            }
            if (item.getEndTime()==null){
                viewHolder.txt2.setText(String.format("离店时间:%s", ""));
            }else {
                viewHolder.txt2.setText(String.format("离店时间:%s", DateUtils.dateToStr1(DateUtils.strToDate(item.getEndTime()))));
            }

        } else {
            viewHolder.txt1.setVisibility(View.GONE);
            viewHolder.txt2.setVisibility(View.GONE);
        }
        viewHolder.txt1.setVisibility(View.GONE);
        viewHolder.txt2.setVisibility(View.GONE);
        viewHolder.txt3.setText(DateUtils.dateToStr1(DateUtils.strToDate(item.getPayTime())));

        if (item.getOrderIteam() != null && item.getOrderIteam().size() > 0) {
            adapter = new OrderProductAdapter(mContext, item.getOrderIteam());
            viewHolder.listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        viewHolder.orderNo.setText(String.format("订单号:%s", item.getOrderNo()));
        viewHolder.orginMoney.setText(String.format("合计:￥%1s\n\n优惠:￥%2s", item.getTotalMoney().setScale(2, RoundingMode.HALF_UP),
                item.getDiscountPrice().setScale(2,RoundingMode.HALF_UP)));
        viewHolder.totalMoney.setText(String.format("实付:￥%s", item.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.desc.setText(String.format("备注:%s", item.getDesc()));

        switch (item.getStatus()) {
            case 1:
                viewHolder.orderBtn.setText("核销");
                viewHolder.orderBtn.setBackground(mContext.getResources().getDrawable(R.drawable.common_blue_radius_shape));
                viewHolder.orderBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case 2:
                viewHolder.orderBtn.setText("已核销");
                break;
            case 3:
                break;
        }
        viewHolder.orderBtn.setTag(item);
        viewHolder.linearLayout.setTag(item);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ServiceOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.order_layout)
        LinearLayout linearLayout;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.user_mobile)
        TextView userMobile;
        @Bind(R.id.txt1)
        TextView txt1;
        @Bind(R.id.txt2)
        TextView txt2;
        @Bind(R.id.txt3)
        TextView txt3;
        @Bind(R.id.txt4)
        TextView txt4;
        @Bind(R.id.list_view)
        NoScrollListview listview;
        @Bind(R.id.total_money)
        TextView totalMoney;
        @Bind(R.id.origin_money)
        TextView orginMoney;
        @Bind(R.id.order_no)
        TextView orderNo;
        @Bind(R.id.order_btn)
        TextView orderBtn;
        @Bind(R.id.desc)
        TextView desc;

        public ServiceOrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            orderBtn.setOnClickListener(this);
            linearLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.order_btn:
                    Order item = (Order) v.getTag();
                    if (item.getStatus() == 1) {
                        Intent intent = new Intent(mContext, QrcodeVerificationActivity.class);
                        mContext.startActivity(intent);
                    }
                    break;
                case R.id.order_layout:
                    Order item1 = (Order) v.getTag();
                    if (item1.getStatus() == null) {
                        return;
                    }
                    if (item1.getStatus() == 1) {
                        Intent intent = new Intent(mContext, QrcodeVerificationActivity.class);
                        mContext.startActivity(intent);
                    }
                    break;
            }
        }

    }
}
