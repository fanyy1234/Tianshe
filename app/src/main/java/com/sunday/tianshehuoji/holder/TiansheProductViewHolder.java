package com.sunday.tianshehuoji.holder;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheProduct;
import com.sunday.tianshehuoji.ui.fyy.ProductDetailActivity;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class TiansheProductViewHolder extends BaseViewHolder<TiansheProduct> {
    public TiansheProductViewHolder(View itemView) {
        super(itemView);
    }
    @Override
    public void setUpView(final TiansheProduct model, int position, final MultiTypeAdapter adapter) {
        final ImageView img = (ImageView) getView(R.id.product_img);
        final TextView newPrice = (TextView) getView(R.id.price_now);
        final TextView oldPrice = (TextView) getView(R.id.price_old);
        final TextView productTitle = (TextView) getView(R.id.product_title);
        final View rootView = getView(R.id.total_layout);
        productTitle.setText(model.getName());
        newPrice.setText("￥"+model.getNewPrice());
//        oldPrice.setText(model.getOldPrice());
//        oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
//        Picasso.with(adapter.getmContext()).load(model.getImg()).into(img);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adapter.getmContext(), ProductDetailActivity.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("id",model.getId());
                intent.putExtra("price",model.getNewPrice());
                intent.putExtra("img",model.getImg());
                adapter.getmContext().startActivity(intent);
            }
        });
    }
}
