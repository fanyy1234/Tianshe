package com.sunday.tianshehuoji.holder;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.ClothProduct;
import com.sunday.tianshehuoji.model.TiansheProduct;
import com.sunday.tianshehuoji.ui.fyy.ClothingDetailActivity;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class ClothProductViewHolder extends BaseViewHolder<ClothProduct> {
    public ClothProductViewHolder(View itemView) {
        super(itemView);
    }
    @Override
    public void setUpView(final ClothProduct model, int position, final MultiTypeAdapter adapter) {
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
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adapter.getmContext(), ClothingDetailActivity.class);
                adapter.getmContext().startActivity(intent);
            }
        });
    }
}
