package com.sunday.tianshehuoji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;
import com.sunday.tianshehuoji.utils.ImgUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/26.
 */

public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<SellerShopProductListBean> productList;
    private int appWidth;
    private View.OnClickListener onClickListener;
    //有一个购物车的data来控制数量的显示和增减
    private SparseArray<List<SellerShopProductListBean>> cartList;

    public ProductListAdapter(Context context, List<SellerShopProductListBean> productList,
                              SparseArray<List<SellerShopProductListBean>> carts) {
        this.mContext = context;
        this.productList = productList;
        this.appWidth= DeviceUtils.getDisplay(mContext).widthPixels;
        this.cartList=carts;

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

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_product_item, null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        SellerShopProductListBean item=productList.get(position);
        if (!TextUtils.isEmpty(item.getLogo())){
            Picasso.with(mContext)
                    .load(ImgUtils.ReplaceStr(item.getLogo()))
                    .error(R.mipmap.login_logo)
                    .resize(appWidth/6,appWidth/6)
                    .centerCrop()
                    .into(viewHolder.productImage);
        }
        viewHolder.productTitle.setText(item.getName());
        viewHolder.productPrice.setText(String.format("￥%s",item.getPrice()));

        List<SellerShopProductListBean> cart=cartList.get(Integer.parseInt(item.getId()));
        if (cart!=null){
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.productNum.setVisibility(View.VISIBLE);
            viewHolder.productNum.setText(String.format("%d",cart.size()));
        }else {
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.productNum.setVisibility(View.GONE);
        }

        viewHolder.add.setOnClickListener(onClickListener);
        viewHolder.delete.setOnClickListener(onClickListener);
        viewHolder.add.setTag(item);
        viewHolder.delete.setTag(item);

        return convertView;

    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    static class ViewHolder {
        @Bind(R.id.product_image)
        ImageView productImage;
        @Bind(R.id.product_title)
        TextView productTitle;
        @Bind(R.id.product_price)
        TextView productPrice;
        @Bind(R.id.add)
        ImageView add;
        @Bind(R.id.product_num)
        TextView productNum;
        @Bind(R.id.delete)
        ImageView delete;
        @Bind(R.id.btn_reserve)
        TextView btnReserve;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }
}
