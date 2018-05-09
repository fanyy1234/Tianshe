package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;

import com.sunday.common.widgets.NoScrollListview;

import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.order.Order;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.order.OrderDetailActivity;
import com.sunday.tianshehuoji.ui.product.OrderPayActivity;
import com.sunday.tianshehuoji.ui.product.ProductCommentActivity;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class OrderListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Order> dataSet;
    private int appWidth;
    String memberId;
    private OrderProductListAdapter adapter;


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
        viewHolder.orderNo.setText(item.getShopName());
        viewHolder.totleMoney.setText(String.format("￥%s", item.getRealMoney().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.orderTime.setText(item.getPayTime());
        if (item.getOrderIteam()!=null&&item.getOrderIteam().size()>0){
            adapter=new OrderProductListAdapter(mContext,item.getOrderIteam());
            viewHolder.noScrollListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        viewHolder.orderBtn1.setTag(item);
        viewHolder.itemView.setTag(item);

        switch (item.getStatus()) {
            case 1:
                viewHolder.tvStatus.setText("未使用");
                viewHolder.orderBtn1.setText("使用");
                break;
            case 2:
                viewHolder.tvStatus.setText("未评价");
                viewHolder.orderBtn1.setText("评价");
                break;
            case 3:
                viewHolder.tvStatus.setText("已完成");
                viewHolder.orderBtn1.setText("已完成");
                break;
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ServiceOrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.order_no)
        TextView orderNo;
        @Bind(R.id.tvStatus)
        TextView tvStatus;
        @Bind(R.id.no_scroll_list_view)
        NoScrollListview noScrollListView;
        @Bind(R.id.rl_total_layout)
        RelativeLayout rlTotalLayout;
        @Bind(R.id.order_time)
        TextView orderTime;
        @Bind(R.id.productNumTotal)
        TextView productNumTotal;
        @Bind(R.id.totleMoney)
        TextView totleMoney;
        @Bind(R.id.order_btn1)
        Button orderBtn1;
        @Bind(R.id.item_view)
        LinearLayout itemView;

        public ServiceOrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            orderBtn1.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           switch (v.getId()) {
                case R.id.item_view:
                    //查看服务订单详情
                    Order item = (Order) v.getTag();
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("orderNo", item.getOrderNo());
                    mContext.startActivity(intent);
                    break;
                case R.id.order_btn1:
                    Order item1= (Order) v.getTag();
                    if (item1.getStatus()==1){
                        Intent intent1 = new Intent(mContext, OrderDetailActivity.class);
                        intent1.putExtra("orderNo", item1.getOrderNo());
                        mContext.startActivity(intent1);
                    }else if (item1.getStatus()==2){
                        Intent intent2 = new Intent(mContext, ProductCommentActivity.class);
                        intent2.putExtra("orderId", item1.getOrderNo());
                        intent2.putExtra("memberId",item1.getMemberId());
                        intent2.putExtra("shopId",item1.getShopId());
                        mContext.startActivity(intent2);
                    }
                    break;

            }
        }
    }

}
