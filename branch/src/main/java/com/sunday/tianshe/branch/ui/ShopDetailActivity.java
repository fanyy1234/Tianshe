package com.sunday.tianshe.branch.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.fragment.ProductInfoFragment;
import com.sunday.tianshe.branch.fragment.ProfitStatisticsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商户详情
 */
public class ShopDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    private ArrayList<Fragment> lists = new ArrayList<>(2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        ButterKnife.bind(this);
        titleView.setText(getIntent().getStringExtra("name"));
        String shopId = getIntent().getStringExtra("shopId");
        lists.add(ProfitStatisticsFragment.newInstance(shopId,1));
        lists.add(ProductInfoFragment.newInstance(shopId));
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),mContext,lists,R.array.shop_titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }
}
