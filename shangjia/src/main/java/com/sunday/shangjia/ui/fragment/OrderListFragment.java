package com.sunday.shangjia.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.OrderListAdapter;
import com.sunday.shangjia.entity.Order;
import com.sunday.shangjia.entity.OrderTotal;
import com.sunday.shangjia.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2016/12/20.
 */

public class OrderListFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.empty_layout)
    EmptyLayout emptylayout;

    private OrderListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Integer status;
    private String memberId;
    private int pageNo = 1;
    private String searchContent;

    private List<Order> dataSet = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_common, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt("status", -1);
        }
        memberId = SharePerferenceUtils.getIns(mContext).getString("shopId", "-1");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width)
                .build());
        emptylayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                pageNo = 1;
                getData();
            }
        });
        getData();
        handlerRecyclerView();
    }

    private void getData() {
        Call<ResultDO<OrderTotal>> call = AppClient.getAppAdapter().getOrderList(memberId, searchContent, status, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<OrderTotal>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderTotal>> call, Response<ResultDO<OrderTotal>> response) {
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                ResultDO<OrderTotal> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult().getData());
                    if (resultDO.getResult().getData() != null && resultDO.getResult().getData().size() == 20) {
                        pageNo++;
                        isCanloadMore = true;
                    }
                    if (dataSet.size() > 0) {
                        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptylayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<OrderTotal>> call, Throwable t) {
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }


    private int lastVisibleItem;
    private boolean isCanloadMore = false;

    private void handlerRecyclerView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                getData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount() && isCanloadMore) {
                    getData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
