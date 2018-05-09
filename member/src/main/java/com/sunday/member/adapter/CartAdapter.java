package com.sunday.member.adapter;

import android.content.Context;

import com.sunday.common.adapter.CommenAdapter;
import com.sunday.common.adapter.ViewHolder;
import com.sunday.member.entity.Cart;

import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class CartAdapter extends CommenAdapter<Cart> {

    public CartAdapter(Context context, List<Cart> mDatas, int itemLayoutId) {
        super(context,mDatas,itemLayoutId);
    }
    @Override
    public void convert(ViewHolder helper, Cart item,int position) {

    }
}
