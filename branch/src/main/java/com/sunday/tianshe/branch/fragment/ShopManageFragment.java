package com.sunday.tianshe.branch.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.adapter.MemberAuthAdapter;
import com.sunday.tianshe.branch.adapter.ShopManageAdapter;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Shop;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrDefaultHandler;
import com.sunday.tianshe.branch.widgets.ptr.PtrFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 商家管理
 */
public class ShopManageFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.left_btn)
    ImageView leftBtn;

    private int pageNo = 1;
    private int pageSize = 20;

    private ShopManageAdapter shopManageAdapter;

    private List<Shop> dataSet = new ArrayList<>();
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private boolean isCanloadMore;

    public ShopManageFragment() {
    }

    public static ShopManageFragment newInstance() {
        ShopManageFragment fragment = new ShopManageFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        leftBtn.setVisibility(View.GONE);
        getData();
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        shopManageAdapter = new ShopManageAdapter(mContext,dataSet);
        recyclerView.setAdapter(shopManageAdapter);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1== shopManageAdapter.getItemCount() && isCanloadMore) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_member_auth, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void getData(){
        Call<ResultDO<List<Shop>>> call = ApiClient.getApiAdapter().shopList(BaseApp.getInstance().getMember().sellerId,pageNo,pageSize);
        call.enqueue(new Callback<ResultDO<List<Shop>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Shop>>> call, Response<ResultDO<List<Shop>>> response) {
                ResultDO<List<Shop>> resultDO = response.body();
                ptrFrameLayout.refreshComplete();
                if(resultDO!=null){
                    if(resultDO.getCode()==0){
                        if(pageNo==1){
                            dataSet.clear();
                        }
                        if(resultDO.getResult().size()==20){
                            pageNo++;
                            isCanloadMore = true;
                        }else{
                            isCanloadMore = false;
                        }
                        dataSet.addAll(resultDO.getResult());
                        shopManageAdapter.notifyDataSetChanged();
                    }else{
                        ToastUtils.showToast(mContext,"系统异常");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Shop>>> call, Throwable t) {
                ptrFrameLayout.refreshComplete();
            }
        });
    }

}
