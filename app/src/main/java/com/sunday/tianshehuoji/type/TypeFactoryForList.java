package com.sunday.tianshehuoji.type;

import android.view.View;

import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.holder.BaseViewHolder;
import com.sunday.tianshehuoji.holder.ClothProductViewHolder;
import com.sunday.tianshehuoji.holder.SubmitOrderProductViewHolder;
import com.sunday.tianshehuoji.holder.TiansheProductViewHolder;
import com.sunday.tianshehuoji.model.ClothProduct;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheProduct;


/**
 * Created by yq05481 on 2016/12/30.
 */

public class TypeFactoryForList implements TypeFactory {
    private final int TYPE_RESOURCE_SUBMITORDERPRODUCT = R.layout.item_submit_order;
    private final int TYPE_RESOURCE_TIANSHEPRODUCT = R.layout.item_tianshe_product;
    private final int TYPE_RESOURCE_CLOTHPRODUCT = R.layout.item_clothing;

    @Override
    public int type(SubmitOrderProduct submitOrderProduct) {
        return TYPE_RESOURCE_SUBMITORDERPRODUCT;
    }
    @Override
    public int type(TiansheProduct tiansheProduct) {
        return TYPE_RESOURCE_TIANSHEPRODUCT;
    }
    @Override
    public int type(ClothProduct clothProduct) {
        return TYPE_RESOURCE_CLOTHPRODUCT;
    }

    @Override
    public BaseViewHolder createViewHolder(int type, View itemView) {

        if(TYPE_RESOURCE_SUBMITORDERPRODUCT == type){
            return new SubmitOrderProductViewHolder(itemView);
        }else if (TYPE_RESOURCE_TIANSHEPRODUCT == type){
            return new TiansheProductViewHolder(itemView);
        }else if (TYPE_RESOURCE_CLOTHPRODUCT == type){
            return new ClothProductViewHolder(itemView);
        }

        return null;
    }
}
