package com.sunday.tianshehuoji.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.event.util.AsyncExecutor;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.IndexAdapter;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.shop.IndexShop;
import com.sunday.tianshehuoji.entity.Notice;
import com.sunday.tianshehuoji.entity.shop.SellerShop;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.ui.MainActivity;
import com.sunday.tianshehuoji.ui.index.SellerListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class IndexFragment extends BaseFragment {

    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    GridLayoutManager gridLayoutManager;
    IndexAdapter indexAdapter;
    String latitude;
    String longitude;
    IndexShop indexShop;
    private List<Notice> noticeList = new ArrayList<>();
    private List<Img> imgList = new ArrayList<>();
    private List<SellerShop> sellerShopList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_common_recycler_list, null);
        ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leftBtn.setVisibility(View.GONE);
        titleView.setText("西湖生活馆");
        titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.arrow_down, 0);
        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else if (position == 1) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        indexAdapter = new IndexAdapter(mContext, imgList, noticeList, sellerShopList);
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green, R.color.material_blue, R.color.material_yellow);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(indexAdapter);
        latitude = SharePerferenceUtils.getIns(mContext).getString(Constants.LATITUDE, "");
        longitude = SharePerferenceUtils.getIns(mContext).getString(Constants.LONGITUDE, "");
        if (TextUtils.isEmpty(latitude)) {
            BaseApplication.getInstance().reLocate();
        }
        getData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (indexShop==null){return;}
                if (!TextUtils.isEmpty(indexShop.getSellerId())){
                getDataById(indexShop.getSellerId());}
            }
        });

    }

    final static int REQUEST_SELLER = 0x111;

    @OnClick(R.id.title_view)
    void select() {
        intent = new Intent(mContext, SellerListActivity.class);
        startActivityForResult(intent, 0x111);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_SELLER) {
            getDataById(data.getStringExtra("sellerId"));
        }

    }

    private void getDataById(String sellerId) {
        Call<ResultDO<IndexShop>> call = AppClient.getAppAdapter().getIndexShopBySellerId(sellerId);
        call.enqueue(new Callback<ResultDO<IndexShop>>() {
            @Override
            public void onResponse(Call<ResultDO<IndexShop>> call, Response<ResultDO<IndexShop>> response) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        indexShop = response.body().getResult();
                        updateView();
                    }
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<IndexShop>> call, Throwable t) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void getData() {
        Call<ResultDO<IndexShop>> call = AppClient.getAppAdapter().getIndexShopByLatLon(latitude, longitude);
        call.enqueue(new Callback<ResultDO<IndexShop>>() {
            @Override
            public void onResponse(Call<ResultDO<IndexShop>> call, Response<ResultDO<IndexShop>> response) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() != null) {
                        indexShop = response.body().getResult();
                        updateView();
                    }
                } else {
                    ToastUtils.showToast(mContext, response.body().getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<IndexShop>> call, Throwable t) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private void updateView() {
        titleView.setText(indexShop.getSellerName());
        noticeList.clear();
        imgList.clear();
        sellerShopList.clear();
        noticeList.addAll(indexShop.getNoticeList());
        imgList.addAll(indexShop.getImgList());
        sellerShopList.addAll(indexShop.getSellerShopList());
        indexAdapter.notifyDataSetChanged();
        BaseApplication.getInstance().setSellerId(indexShop.getSellerId());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
