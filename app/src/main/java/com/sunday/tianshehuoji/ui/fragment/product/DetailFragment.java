package com.sunday.tianshehuoji.ui.fragment.product;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.ShopDetail;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.URLImageGetter;
import com.sunday.tianshehuoji.widgets.ScrollableHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by 刘涛 on 2016/12/15.
 */

public class DetailFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {

    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.shop_address)
    TextView shopAddress;
    @Bind(R.id.call_shop)
    ImageView callShop;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.desc)
    TextView desc;

    private String shopId;
    ShopDetail shopDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_product_detail, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            shopId = getArguments().getString("shopId");
        }
        getData();

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
        shopAddress.setText(shopDetail.getAddress());
        desc.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        desc.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        URLImageGetter imageGetter = new URLImageGetter(getActivity(),desc);
        desc.setText(Html.fromHtml(shopDetail.getDetail(), imageGetter, null));
//        desc.setText(shopDetail.getDetail());

    }

    @OnClick(R.id.call_shop)
    void callShop() {
        if (!TextUtils.isEmpty(shopDetail.getMobile()))
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + shopDetail.getMobile()));
        if (ActivityCompat.checkSelfPermission(mContext, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                    CALL_PHONE
            }, 0);
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View getScrollableView() {
        return scrollView;
    }
}
