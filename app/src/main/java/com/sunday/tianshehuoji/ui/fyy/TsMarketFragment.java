package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.order.Order;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.TiansheMarket;
import com.sunday.tianshehuoji.ui.fragment.OrderListFragment;
import com.sunday.tianshehuoji.utils.EntityUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fyy on 2018/4/12.
 */

public class TsMarketFragment extends BaseFragment {

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    String[] type ;
    int status[] ;
    private int witch = -1;//默认显示全部 0：待付款 1：未使用 2：待评价 3：已完成
    private String shopId;
    List<Map<String,Object>> mapList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_tianshe_market, null);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        shopId = getArguments().getString("shopId");
        getInfo();
    }

    private void getInfo(){
        Call<ResultDO> call = AppClient.getAppAdapter().getTiansheShopDetail(shopId);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO = response.body();
                if (resultDO==null){
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonResult = EntityUtil.ObjectToJson(response.body());
                if (resultDO.getCode() == 0) {
                    JSONArray jsonArray = jsonResult.getJSONArray("product");
                    int length = jsonArray.size();
                    type = new String[length];

                    for (int i=0;i<length;i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String,Object> map = new HashMap<>();
                        type[i] = jsonObject.getString("typeName");
                        JSONArray productArray = jsonObject.getJSONArray("sellerShopProductList");
                        map.put("productArray",productArray);
                        mapList.add(map);
                    }

                    viewPager.setAdapter(new TabAdapter(getActivity().getSupportFragmentManager()));
                    tabLayout.setupWithViewPager(viewPager);
                    viewPager.setOffscreenPageLimit(length);
                    viewPager.setCurrentItem(0);
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
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
        public Fragment getItem(int position) {
            TsProductFragment fragment = new TsProductFragment();
            Bundle bundle = new Bundle();
            Parcelable productArray = new TiansheMarket(mapList.get(position));
            bundle.putParcelable("productArray", productArray);
            bundle.putInt("status",0);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return type[position];
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
