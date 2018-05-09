package com.sunday.shangjia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;

import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Product;
import com.sunday.shangjia.util.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2017/1/3.
 */

public class ProductListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Product> dataSet;
    private View.OnClickListener onClickListener;
    private int appWidth;

    public ProductListAdapter(Context context, List<Product> data) {
        this.mContext = context;
        this.dataSet = data;
        appWidth = DeviceUtils.getDisplay(mContext).widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_product_item, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ListHolder listHolder = (ListHolder) holder;
        Product item = dataSet.get(position);
        if (!TextUtils.isEmpty(item.getLogo())) {
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .resize(appWidth / 5, appWidth / 5)
                    .centerInside()
                    .into(listHolder.productImg);
        }
        listHolder.productTitle.setText(item.getName());
        listHolder.productPrice.setText(String.format("￥%s",item.getPrice()));
        switch (item.getShow()){
            case 1:
                listHolder.btn1.setText("下架");
                listHolder.btn2.setText("定价");
                break;
            case 2:
                listHolder.btn1.setText("删除");
                listHolder.btn2.setText("上架");
                break;
        }
        listHolder.btn1.setTag(item);
        listHolder.btn2.setTag(item);
        listHolder.btn1.setOnClickListener(onClickListener);
        listHolder.btn2.setOnClickListener(onClickListener);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

     class ListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.btn1)
        TextView btn1;
        @Bind(R.id.btn2)
        TextView btn2;
        @Bind(R.id.product_item)
        RelativeLayout productItem;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
