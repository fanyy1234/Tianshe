package com.sunday.shangjia.ui.fragment;

import android.content.Intent;
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

import com.sunday.member.base.BaseFragment;
import com.sunday.shangjia.R;
import com.sunday.shangjia.ui.order.OrderSearchActivity;
import com.sunday.shangjia.ui.order.QrcodeVerificationActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2016/12/20.
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
    @Bind(R.id.right_btn)
    ImageView rightBtn;


    String[] type = {"未核销", "已核销", "全部"};
    int status[] = {1, 2, -1};

    private int witch = -1;//默认显示全部

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
        leftBtn.setImageResource(R.mipmap.top_scan);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.mipmap.top_search);
        titleView.setText("订单核销");
        initView();

    }

    private void initView() {
        viewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setOffscreenPageLimit(3);
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

    @OnClick(R.id.left_btn)
    void checkCode(){
        intent=new Intent(mContext, QrcodeVerificationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.right_btn)
    void search(){
        intent=new Intent(mContext, OrderSearchActivity.class);
        startActivity(intent);
    }

}
