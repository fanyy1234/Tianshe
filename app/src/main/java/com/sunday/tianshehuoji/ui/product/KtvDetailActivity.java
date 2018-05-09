package com.sunday.tianshehuoji.ui.product;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.BannerViewHolder;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.shop.ShopDetail;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.fragment.product.CommentFragment;
import com.sunday.tianshehuoji.ui.fragment.product.DetailFragment;
import com.sunday.tianshehuoji.ui.fragment.product.ProductFragment;
import com.sunday.tianshehuoji.widgets.ScrollableLayout;
import com.sunday.tianshehuoji.widgets.timePeriodSelect.TimePeriodSelectWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/15.
 * <p>
 * 详情
 */

public class KtvDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.rl_select_time)
    RelativeLayout rlSelectTime;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.rootliner)
    ScrollableLayout rootScroller;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    ArrayList<Fragment> fragments = new ArrayList<>();
    @Bind(R.id.time)
    TextView txt_time;
    @Bind(R.id.date)
    TextView txt_date;
    @Bind(R.id.txt1)
    TextView txt1;
    @Bind(R.id.txt2)
    TextView txt2;
    private String shopName;
    private String shopId;
    private String shopType;
    ShopDetail shopDetail;

    private String startTime;
    private String endTime;
    TimePickerView timePickerView;
    Calendar calendar;
    Date date;


    BannerViewHolder bannerViewHolder;
    ViewHolderCreator viewHolderCreator;
    SimpleDateFormat sdf;
    SimpleDateFormat sdf1;
    ProductFragment productFragment;
    CommentFragment commentFragment;
    DetailFragment detailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        shopName = getIntent().getStringExtra("shopName");
        shopId = getIntent().getStringExtra("shopId");
        shopType = getIntent().getStringExtra("shopType");
        titleView.setText(shopName);
        if (shopType.equals(Const.TYPE2) || shopType.equals(Const.TYPE11)) {
            rlSelectTime.setVisibility(View.VISIBLE);
            txt1.setText("入住");
            txt2.setText("离店");
        } else if (shopType.equals(Const.TYPE3) || shopType.equals(Const.TYPE7)) {
            rlSelectTime.setVisibility(View.VISIBLE);
            txt1.setText("日期");
            txt2.setText("时间段");
        }
        getData();
        initFragment();
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productFragment.getData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void initFragment() {
        productFragment = new ProductFragment();
        commentFragment = new CommentFragment();
        detailFragment = new DetailFragment();
        fragments.add(productFragment);
        fragments.add(commentFragment);
        fragments.add(detailFragment);
        for (int i = 0; i < 3; i++) {
            Bundle bundle = new Bundle();
            switch (i) {
                case 0:
                    bundle.putString("shopId", shopId);
                    bundle.putString("shopType", shopType);
                    break;
                case 1:
                    bundle.putString("shopId", shopId);
                    break;
                case 2:
                    bundle.putString("shopId", shopId);
                    break;
            }
            fragments.get(i).setArguments(bundle);
        }
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mContext, fragments, R.array.product_detail));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        rootScroller.getHelper().setCurrentScrollableContainer(productFragment);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rootScroller.getHelper().setCurrentScrollableContainer(productFragment);
                        break;
                    case 1:
                        rootScroller.getHelper().setCurrentScrollableContainer(commentFragment);
                        break;
                    case 2:
                        rootScroller.getHelper().setCurrentScrollableContainer(detailFragment);
                        break;
                }
                if (position == 0) {
                    productFragment.showWindow();
                } else {
                    productFragment.hideWindow();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getData() {
        Call<ResultDO<ShopDetail>> call = AppClient.getAppAdapter().getShopProductDetail(shopId);
        call.enqueue(new Callback<ResultDO<ShopDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<ShopDetail>> call, Response<ResultDO<ShopDetail>> response) {
                ResultDO<ShopDetail> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() != null) {
                        shopDetail = resultDO.getResult();
                        updateView();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResultDO<ShopDetail>> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        if (shopDetail.getImgList() == null) {
            return;
        }
        bannerViewHolder = new BannerViewHolder();
        bannerViewHolder.setNeedFull(true);
        int appWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(appWidth, appWidth * 2/ 5);
        banner.setLayoutParams(params);
        viewHolderCreator = new ViewHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createHolder() {
                return bannerViewHolder;
            }
        };
        banner.setPages(viewHolderCreator, shopDetail.getImgList())
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        banner.notifyDataSetChanged();
        banner.startTurning(3000);
    }


    @OnClick(R.id.date)
    void selectStartTime() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(false);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                txt_date.setText(sdf.format(date));
                if (shopType.equals(Const.TYPE2) || shopType.equals(Const.TYPE11)) {
                    startTime = String.format("%s 00:00:01", txt_date.getText().toString());
                } else if (shopType.equals(Const.TYPE3) || shopType.equals(Const.TYPE7)) {
                    if (TextUtils.isEmpty(startPeriod1) || TextUtils.isEmpty(endPeriod1)) {
                        return;
                    }
                    startTime = String.format("%1s %2s", txt_date.getText().toString(), startPeriod1);
                    endTime = String.format("%1s %2s", txt_date.getText().toString(), endPeriod1);
                }
                productFragment.showWindow();
            }
        });
        timePickerView.show();
        productFragment.hideWindow();
    }

    private String startPeriod1, endPeriod1;

    @OnClick(R.id.time)
    void selectEndTime() {
        if (shopType.equals(Const.TYPE2) || shopType.equals(Const.TYPE11)) {
            timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
            timePickerView.setCyclic(false);
            timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    txt_time.setText(sdf.format(date));
                    endTime = String.format("%s 23:59:59", txt_time.getText().toString());
                    productFragment.showWindow();
                }
            });
            timePickerView.show();
            productFragment.hideWindow();
        } else if (shopType.equals(Const.TYPE3) || shopType.equals(Const.TYPE7)) {
            TimePeriodSelectWindow timePeriodSelectWindow = new TimePeriodSelectWindow(mContext, productFragment.getProductSettleWindow());
            timePeriodSelectWindow.setOnSelectListener(new TimePeriodSelectWindow.OnSelectListener() {
                @Override
                public void onSelect(String startPeriod, String endPeriod) {
                    if (TextUtils.isEmpty(startPeriod) || TextUtils.isEmpty(endPeriod)) {
                        return;
                    }
                    txt_time.setText(String.format("%1s-%2s", startPeriod, endPeriod));
                    if (TextUtils.isEmpty(txt_date.getText().toString())) {
                        return;
                    }
                    startPeriod1 = startPeriod;
                    endPeriod1 = endPeriod;
                    startTime = String.format("%1s %2s", txt_date.getText().toString(), startPeriod);
                    endTime = String.format("%1s %2s", txt_date.getText().toString(), endPeriod);
                }
            });
            timePeriodSelectWindow.show(banner);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        banner.stopTurning();
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
