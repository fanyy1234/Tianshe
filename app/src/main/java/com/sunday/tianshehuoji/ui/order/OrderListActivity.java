package com.sunday.tianshehuoji.ui.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * Created by 刘涛 on 2017/4/24.
 */

public class OrderListActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList<Fragment> commenOrderFragment = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    int[] statusArr = {-1,0,1,2,5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        rightTxt.setVisibility(View.GONE);
        titleView.setText("我的订单");
        initFragments();
    }

    private void initFragments(){
        tabLayout.setTabMode(MODE_SCROLLABLE);
        //0代付款1未发货2待评价3已完成4配送中
        commenOrderFragment.add(OrderListMarketFragment.newInstance(0));//待付款
        commenOrderFragment.add(OrderListMarketFragment.newInstance(1));//待发货
        commenOrderFragment.add(OrderListMarketFragment.newInstance(2));//待收货
        commenOrderFragment.add(OrderListMarketFragment.newInstance(3));//待收货
        commenOrderFragment.add(OrderListMarketFragment.newInstance(4));//已完成
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),mContext,
                commenOrderFragment,R.array.order_title));
        tabLayout.setupWithViewPager(viewPager);
        int page = getIntent().getIntExtra("page",0);
        viewPager.setCurrentItem(page,false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((OrderListMarketFragment)commenOrderFragment.get(position)).refresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
