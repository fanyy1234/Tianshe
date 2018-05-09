package com.sunday.tianshehuoji.holder;

import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheProduct;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class TiansheProductViewHolder extends BaseViewHolder<TiansheProduct> {
    public TiansheProductViewHolder(View itemView) {
        super(itemView);
    }
    @Override
    public void setUpView(final TiansheProduct model, int position, MultiTypeAdapter adapter) {
        final TextView name = (TextView) getView(R.id.name);
        final ImageView img = (ImageView) getView(R.id.img);
        final TextView newPrice = (TextView) getView(R.id.new_price);
        final TextView oldPrice = (TextView) getView(R.id.old_price);
        final View rootView = getView(R.id.root_view);
        name.setText(model.getName());
        newPrice.setText(model.getNewPrice());
        oldPrice.setText(model.getOldPrice());
        oldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
//        Picasso.with(adapter.getmContext()).load(model.getImg()).into(img);
    }
}
