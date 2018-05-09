package com.sunday.tianshehuoji.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.order.OrderItem;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.math.RoundingMode;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/27.
 */

public class OrderProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderItem> productList;
    private int appWidth;
    private View.OnClickListener onClickListener;

    public OrderProductListAdapter(Context context, List<OrderItem> productList) {
        this.mContext = context;
        this.productList = productList;
        this.appWidth = DeviceUtils.getDisplay(mContext).widthPixels;

    }


    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_order_product_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderItem item = productList.get(position);
        if (!TextUtils.isEmpty(item.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .error(R.mipmap.login_logo)
                    .resize(appWidth / 5, appWidth / 5)
                    .centerCrop()
                    .into(viewHolder.productImage);
        }
        viewHolder.productName.setText(item.getName());
        viewHolder.productSpec.setText(String.format("￥%s", item.getPrice().setScale(2, RoundingMode.HALF_UP)));
        viewHolder.productPrice.setText(String.format("x%d",item.getNum()));
        return convertView;

    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class ViewHolder {
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_name)
        TextView productName;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.product_params)
        TextView productParams;
        @Bind(R.id.product_num)
        TextView productNum;
        @Bind(R.id.product_spec)
        TextView productSpec;
        @Bind(R.id.pro_layout)
        RelativeLayout proLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
