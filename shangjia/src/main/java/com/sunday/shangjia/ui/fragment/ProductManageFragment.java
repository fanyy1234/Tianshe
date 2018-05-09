package com.sunday.shangjia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.widgets.ViewPager;
import com.sunday.member.base.BaseFragment;
import com.sunday.shangjia.R;
import com.sunday.shangjia.ui.product.ProductListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class ProductManageFragment extends BaseFragment {


    @Bind(R.id.title1)
    TextView title1;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.title2)
    TextView title2;
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind({R.id.title1, R.id.title2})
    List<TextView> tabViews;

    private ArrayList<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.product_manage_fragment, null);
        ButterKnife.bind(this, rootView);
        return rootView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragments = new ArrayList<>();
        fragments.add(new ProductListFragment());
        fragments.add(new ProductListFragment());
        for (int i = 0; i < 2; i++) {
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putInt("type", 1);
                    break;
                case 1:
                    bundle.putInt("type", 2);
                    break;
            }
            fragments.get(i).setArguments(bundle);
        }

        tabViews.get(0).setSelected(true);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
//        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabViews.size(); i++) {
                    tabViews.get(i).setSelected(false);
                }
                tabViews.get(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private boolean isUpdate=false;

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
    @OnClick(R.id.title1)
    void checkSell() {
        viewPager.setCurrentItem(0, false);
        if (isUpdate) {
            ((ProductListFragment) fragments.get(0)).getData();
            isUpdate = false;
        }
    }

    @OnClick(R.id.title2)
    void checkFans() {
        viewPager.setCurrentItem(1, false);
        if (isUpdate) {
            ((ProductListFragment) fragments.get(1)).getData();
            isUpdate = false;
        }
    }
}
