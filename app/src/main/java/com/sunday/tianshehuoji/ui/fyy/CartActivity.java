package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.sunday.member.R;
import com.sunday.member.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通用购物车页面
 * Created by Administrator on 2016/2/26.
 */
public class CartActivity extends BaseActivity {


    @Bind(R.id.cart)
    FrameLayout cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.cart, CartFragment1.newInstance(true, true)).commit();
    }
}
