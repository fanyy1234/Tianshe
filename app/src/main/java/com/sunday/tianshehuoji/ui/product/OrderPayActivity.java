package com.sunday.tianshehuoji.ui.product;

import android.os.Bundle;

import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;

import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class OrderPayActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        ButterKnife.bind(this);
    }


}
