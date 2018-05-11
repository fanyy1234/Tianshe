package com.sunday.tianshehuoji.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class SubmitOrderProductViewHolder extends BaseViewHolder<SubmitOrderProduct> {
    public SubmitOrderProductViewHolder(View itemView) {
        super(itemView);
    }
    @Override
    public void setUpView(final SubmitOrderProduct model, int position, MultiTypeAdapter adapter) {
        final TextView name = (TextView) getView(R.id.product_name);
        final ImageView img = (ImageView) getView(R.id.product_image);
        final TextView guige = (TextView) getView(R.id.product_guige);
        final TextView num = (TextView) getView(R.id.product_num);
        final TextView price = (TextView) getView(R.id.product_price);
        name.setText(model.getName());
        guige.setText(model.getGuige());
        num.setText("x "+model.getNum()+"");
        price.setText(model.getPrice());
        Picasso.with(adapter.getmContext()).load(model.getImg()).error(R.mipmap.default_img).into(img);
    }
}
