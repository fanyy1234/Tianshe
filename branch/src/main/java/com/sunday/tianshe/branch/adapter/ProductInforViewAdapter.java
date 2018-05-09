package com.sunday.tianshe.branch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.PixUtils;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.Product;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class ProductInforViewAdapter extends RecyclerView.Adapter<ProductInforViewAdapter.ViewHolder> {

    private  List<Product> mValues;
    private int width ;
    private Context mContext;

    public ProductInforViewAdapter(Context context, List<Product> items) {
        mValues = items;
        mContext = context;
        width = PixUtils.dip2px(context,60);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Product product = mValues.get(position);
        holder.nameTxt.setText(product.name);
        holder.priceTxt.setText("￥:"+product.price);
        if(!TextUtils.isEmpty(product.logo)){
            Picasso.with(mContext)
                    .load(product.logo.replace("https","http"))
                    .resize(width,width)
                    .into(holder.productImg);
        }

/*        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);*/


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.product_img)
        ImageView productImg;
        @Bind(R.id.name)
        TextView nameTxt;
        @Bind(R.id.price_txt)
        TextView priceTxt;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,itemView);
        }


    }
}
