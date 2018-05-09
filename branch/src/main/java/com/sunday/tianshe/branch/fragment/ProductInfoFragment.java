package com.sunday.tianshe.branch.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunday.common.model.ResultDO;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.adapter.ProductInforViewAdapter;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Product;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrDefaultHandler;
import com.sunday.tianshe.branch.widgets.ptr.PtrFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 *
 *
 */
public class ProductInfoFragment extends BaseFragment {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    private int pageNo = 1;
    private int pageSize = 20;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrameLayout;

    private static String ARG_PARAM1 = "shopId";
    private String shopId;
    private ProductInforViewAdapter productInforViewAdapter;

    private List<Product> dataSet = new ArrayList<>();


    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private boolean isCanloadMore;

    public static ProductInfoFragment newInstance(String shopId) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1,shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shopId = getArguments().getString(ARG_PARAM1);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        productInforViewAdapter = new ProductInforViewAdapter(mContext,dataSet);
        getData();
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(productInforViewAdapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.divider)
                .build());
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
                        && lastVisibleItem + 1== productInforViewAdapter.getItemCount() && isCanloadMore) {
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

    private void getData(){
        Call<ResultDO<List<Product>>> call = ApiClient.getApiAdapter().productList(BaseApp.getInstance().getMember().sellerId,Long.parseLong(shopId),pageNo,pageSize);
        call.enqueue(new Callback<ResultDO<List<Product>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Product>>> call, Response<ResultDO<List<Product>>> response) {
                ResultDO<List<Product>> resultDO = response.body();
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
                        productInforViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<Product>>> call, Throwable t) {
                ptrFrameLayout.refreshComplete();
            }
        });
    }
}
