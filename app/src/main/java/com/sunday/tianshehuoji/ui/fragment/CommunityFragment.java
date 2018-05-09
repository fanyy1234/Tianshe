package com.sunday.tianshehuoji.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshehuoji.BaseApplication;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.CommunityFragApdapter;
import com.sunday.tianshehuoji.entity.Ads;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.Img;
import com.sunday.tianshehuoji.entity.News;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/14.
 */

public class CommunityFragment extends BaseFragment {


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

    private CommunityFragApdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private List<Img> adsList = new ArrayList<>();
    private List<Img> newsList = new ArrayList<>();
    private String sellerId;
    boolean isInit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_common_recycler_list, null);
        isInit=true;
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leftBtn.setVisibility(View.GONE);
        titleView.setText("天奢之家");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommunityFragApdapter(mContext, adsList, newsList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        getInfo();
        handlerRefresh();
    }



    private void handlerRefresh() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo();
            }
        });

    }

    public void getInfo() {
        sellerId= BaseApplication.getInstance().getSellerId();
        showLoadingDialog(0);
        Call<ResultDO<List<Img>>> call = AppClient.getAppAdapter().getInformation(1);
        call.enqueue(new Callback<ResultDO<List<Img>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Img>>> call, Response<ResultDO<List<Img>>> response) {
                if (isFinish) {
                    return;
                }
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                dissMissDialog();
                ResultDO<List<Img>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0 && resultDO.getResult() != null) {
                    adsList.clear();
                    adsList.addAll(resultDO.getResult());
                    getNews();

                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Img>>> call, Throwable t) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }

    private void getNews(){
        showLoadingDialog(0);
        Call<ResultDO<List<Img>>> call = AppClient.getAppAdapter().getInformation(2);
        call.enqueue(new Callback<ResultDO<List<Img>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Img>>> call, Response<ResultDO<List<Img>>> response) {
                if (isFinish) {
                    return;
                }
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                dissMissDialog();
                ResultDO<List<Img>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0 && resultDO.getResult() != null) {
                    newsList.clear();
                    newsList.addAll(resultDO.getResult());
                    if (adsList.size()==0&&newsList.size()==0){
                        emptylayout.setErrorType(EmptyLayout.NODATA);
                    }else {
                        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }
                   adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Img>>> call, Throwable t) {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
