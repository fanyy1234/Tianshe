package com.sunday.member.ui;

import android.os.Bundle;

import com.sunday.member.R;
import com.sunday.member.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 通用购物车页面
 * Created by Administrator on 2016/2/26.
 */
public class CartActivity  extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
    }
}
