package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class TiansheMarketActivity extends BaseActivity implements View.OnClickListener{


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
    @Bind(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    @Bind(R.id.tianshe_market_img)
    ImageView tiansheMarketImg;
    @Bind(R.id.tianshe_market_text)
    TextView tiansheMarketText;
    @Bind(R.id.tianshe_market)
    LinearLayout tiansheMarket;
    @Bind(R.id.tianshe_cart_img)
    ImageView tiansheCartImg;
    @Bind(R.id.tianshe_cart_text)
    TextView tiansheCartText;
    @Bind(R.id.tianshe_cart)
    LinearLayout tiansheCart;
    @Bind(R.id.tianshe_myinfo_img)
    ImageView tiansheMyinfoImg;
    @Bind(R.id.tianshe_myinfo_text)
    TextView tiansheMyinfoText;
    @Bind(R.id.tianshe_myinfo)
    LinearLayout tiansheMyinfo;
    private int selectColor, unselectColor;
    private TsMarketFragment marketFragment = new TsMarketFragment();
    private TsCartFragment cartFragment = new TsCartFragment();
    private TsMyinfoFragment myinfoFragment = new TsMyinfoFragment();
    private Fragment[] fragments;
    private int currentIndex = 0;
    private int oldIndex;
    private String shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tianshe_market);
        ButterKnife.bind(this);
        titleView.setText("天奢商城");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        initView();
    }

    private void initView() {
        shopId = getIntent().getStringExtra("shopId");
        Bundle bundle = new Bundle();
        bundle.putString("shopId", shopId);
        selectColor = ContextCompat.getColor(this, R.color.appcolor);
        unselectColor = ContextCompat.getColor(this, R.color.fontgrey);
        tiansheMarket.setOnClickListener(this);
        tiansheCart.setOnClickListener(this);
        tiansheMyinfo.setOnClickListener(this);

        marketFragment.setArguments(bundle);
        cartFragment.setArguments(bundle);
        myinfoFragment.setArguments(bundle);
        fragments = new Fragment[]{marketFragment,cartFragment,myinfoFragment};
        FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
        ftr.add(R.id.fragment_container, marketFragment);
        ftr.show(marketFragment).commit();
    }

    private void changeFragment() {
        if (oldIndex != currentIndex) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[oldIndex]);
            if (!fragments[currentIndex].isAdded()) {
                trx.add(R.id.fragment_container, fragments[currentIndex]);
            }
            trx.show(fragments[currentIndex]).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tianshe_market:
                oldIndex = currentIndex;
                currentIndex = 0;
                tiansheMarketText.setTextColor(selectColor);
                tiansheCartText.setTextColor(unselectColor);
                tiansheMyinfoText.setTextColor(unselectColor);
//                tiansheMarketImg.setImageResource(R.mipmap.asset_select);
//                tiansheCartImg.setImageResource(R.mipmap.tools_unselect);
//                tiansheMyinfoImg.setImageResource(R.mipmap.tools_unselect);
                changeFragment();
                break;
            case R.id.tianshe_cart:
                oldIndex = currentIndex;
                currentIndex = 1;
                tiansheMarketText.setTextColor(unselectColor);
                tiansheCartText.setTextColor(selectColor);
                tiansheMyinfoText.setTextColor(unselectColor);
                changeFragment();
                break;
            case R.id.tianshe_myinfo:
                oldIndex = currentIndex;
                currentIndex = 2;
                tiansheMarketText.setTextColor(unselectColor);
                tiansheCartText.setTextColor(unselectColor);
                tiansheMyinfoText.setTextColor(selectColor);
                changeFragment();
                break;
            default:
                break;
        }
    }
}
