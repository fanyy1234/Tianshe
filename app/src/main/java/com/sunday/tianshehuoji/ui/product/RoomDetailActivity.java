package com.sunday.tianshehuoji.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.widgets.banner.ConvenientBanner;
import com.sunday.common.widgets.banner.holder.ViewHolderCreator;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.BannerViewHolder;
import com.sunday.tianshehuoji.entity.shop.SellerShopProductListBean;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2017/1/5.
 */

public class RoomDetailActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.banner)
    ConvenientBanner banner;
    @Bind(R.id.room_name)
    TextView roomName;
    @Bind(R.id.room_price)
    TextView roomPrice;
    @Bind(R.id.add)
    ImageView add;
    @Bind(R.id.product_num)
    TextView productNum;
    @Bind(R.id.delete)
    ImageView delete;
    @Bind(R.id.webView)
    WebView webView;

    private SellerShopProductListBean detail;
    BannerViewHolder bannerViewHolder;
    ViewHolderCreator viewHolderCreator;
    int number=0;
    List<SellerShopProductListBean> dataSet=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        ButterKnife.bind(this);
        titleView.setText("详情");
        detail= (SellerShopProductListBean) getIntent().getSerializableExtra("detail");
        number=getIntent().getIntExtra("num",0);
        updateView();
    }

    private void updateView(){
        if (detail==null){return;}
        bannerViewHolder = new BannerViewHolder();
        bannerViewHolder.setNeedFull(true);
        int appWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(appWidth, appWidth * 3 / 5);
        banner.setLayoutParams(params);
        viewHolderCreator = new ViewHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createHolder() {
                return bannerViewHolder;
            }
        };
        banner.setPages(viewHolderCreator, detail.getImages())
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
        banner.notifyDataSetChanged();
        banner.startTurning(3000);
        roomName.setText(detail.getName());
        roomPrice.setText(String.format("￥%s",detail.getPrice().setScale(2, RoundingMode.HALF_UP)));
        checkProductNum();
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadData(detail.getDetail(), "text/html", "UTF-8");

    }

    private void checkProductNum(){
        if (number==0){
            delete.setVisibility(View.GONE);
            productNum.setVisibility(View.GONE);
        }else {
            delete.setVisibility(View.VISIBLE);
            productNum.setVisibility(View.VISIBLE);
            productNum.setText(String.format("%d",number));
            for (int i=0;i<number;i++){
                dataSet.add(detail);
            }
        }
    }

    @OnClick(R.id.add)
    void addProduct(){
        dataSet.add(detail);
        number++;
        delete.setVisibility(View.VISIBLE);
        productNum.setVisibility(View.VISIBLE);
        productNum.setText(String.format("%d",number));

    }

    @OnClick(R.id.delete)
    void subProduct(){
            dataSet.remove(0);
            number--;
        if (number>0){
            productNum.setText(String.format("%d",number));
        }else {
            delete.setVisibility(View.GONE);
            productNum.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.left_btn)
    void back(){
        Intent data=new Intent();
        data.putExtra("detail",detail);
        data.putExtra("num",number);
        setResult(RESULT_OK,data);
        finish();
    }



    @Override
    protected void onPause() {
        super.onPause();
        banner.stopTurning();
    }
}
