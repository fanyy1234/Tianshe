package com.sunday.tianshehuoji.type;

import android.view.View;

import com.sunday.tianshehuoji.holder.BaseViewHolder;
import com.sunday.tianshehuoji.model.ClothProduct;
import com.sunday.tianshehuoji.model.SubmitOrderProduct;
import com.sunday.tianshehuoji.model.TiansheProduct;


/**
 * Created by yq05481 on 2016/12/30.
 */

public interface TypeFactory {
    int type(SubmitOrderProduct submitOrderProduct);
    int type(TiansheProduct tiansheProduct);
    int type(ClothProduct clothProduct);


    BaseViewHolder createViewHolder(int type, View itemView);
}
