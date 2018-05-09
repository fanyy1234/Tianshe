package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.TiansheMarket;
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

public class ClothingMakeActivity extends BaseActivity implements View.OnClickListener {


    String memberId;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.left_txt)
    TextView leftTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    String[] type;
    private String shopId;
    List<Map<String, Object>> mapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_make);
        ButterKnife.bind(this);
        titleView.setText("服装定制");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        initView();
    }

    private void initView() {
        shopId = "42";
//        shopId = getArguments().getString("shopId");
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

                    viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
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
            bundle.putInt("status",1);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return type[position];
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}
