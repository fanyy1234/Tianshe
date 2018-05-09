package com.sunday.tianshehuoji.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class OrderBaseFragment extends BaseFragment {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    String[] type = {"未使用", "未评价", "已完成", "全部"};
    int status[] = {1, 2, 3, -1};
    private int witch = -1;//默认显示全部 0：待付款 1：未使用 2：待评价 3：已完成
    boolean isInit = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.layout_tab_view_pager, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leftBtn.setVisibility(View.GONE);
        titleView.setText("我的订单");
        initView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&!isInit){
           initView();
            isInit=true;
        }
    }

    private void initView() {
        viewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(witch);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class TabAdapter extends FragmentPagerAdapter {
        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return type.length;
        }

        @Override
        public Fragment getItem(int poaition) {
            OrderListFragment fragment = new OrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("status", status[poaition]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return type[position];
        }
    }

}
