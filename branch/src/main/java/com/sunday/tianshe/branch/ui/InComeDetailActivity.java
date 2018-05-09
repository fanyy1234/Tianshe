package com.sunday.tianshe.branch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.adapter.ChildAccountAdapter;
import com.sunday.tianshe.branch.adapter.ProfitRecordAdapter;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.ChildAccount;
import com.sunday.tianshe.branch.entity.Record;
import com.sunday.tianshe.branch.entity.RecordDO;
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

public class InComeDetailActivity extends BaseActivity {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrameLayout;

    private ProfitRecordAdapter profitRecordAdapter;
    private List<Record> dataSet = new ArrayList<>();

    private String startTime;
    private String endTime;
    /**
     * type=1代表收入明细 type=2代表支出明细
     */
    private int type,pageNo = 1,pageSize = 20;
    private boolean isCanLoadMore = false;

    private int pageType = 0;
    private String shopId;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private boolean isCanloadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_member_auth);
        ButterKnife.bind(this);
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        type = getIntent().getIntExtra("type",type);
        pageType = getIntent().getIntExtra("pageType",0);
        shopId = getIntent().getStringExtra("shopId");
        getData();
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        profitRecordAdapter = new ProfitRecordAdapter(mContext, dataSet,type);
        recyclerView.setAdapter(profitRecordAdapter);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
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
                        && lastVisibleItem + 1== profitRecordAdapter.getItemCount() && isCanloadMore) {
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
        if(pageType==0){
            Call<ResultDO<RecordDO>> call = ApiClient.getApiAdapter().getShopProfitRecord(BaseApp.getInstance().getMember().id,
                    type,startTime,endTime,pageNo,pageSize);
            call.enqueue(new Callback<ResultDO<RecordDO>>() {
                @Override
                public void onResponse(Call<ResultDO<RecordDO>> call, Response<ResultDO<RecordDO>> response) {
                    ResultDO<RecordDO> resultDO = response.body();
                    ptrFrameLayout.refreshComplete();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            if(pageNo==1){
                                dataSet.clear();
                            }
                            if(resultDO.getResult().data.size()==20){
                                isCanLoadMore = true;
                                pageNo++;
                            }else{
                                isCanLoadMore = false;
                            }
                            profitRecordAdapter.setAmount(resultDO.getResult().total);
                            dataSet.addAll(resultDO.getResult().data);
                            profitRecordAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(mContext,"系统异常");
                        }
                    }else{
                        ToastUtils.showToast(mContext,"网络不给力");
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<RecordDO>> call, Throwable t) {
                    ptrFrameLayout.refreshComplete();
                }
            });
        }else{
            Call<ResultDO<RecordDO>> call = ApiClient.getApiAdapter().getShopProfitRecord2(Long.parseLong(shopId),
                    type,startTime,endTime,pageNo,pageSize);
            call.enqueue(new Callback<ResultDO<RecordDO>>() {
                @Override
                public void onResponse(Call<ResultDO<RecordDO>> call, Response<ResultDO<RecordDO>> response) {
                    ResultDO<RecordDO> resultDO = response.body();
                    ptrFrameLayout.refreshComplete();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            if(pageNo==1){
                                dataSet.clear();
                            }
                            if(resultDO.getResult().data.size()==20){
                                isCanLoadMore = true;
                                pageNo++;
                            }else{
                                isCanLoadMore = false;
                            }
                            dataSet.addAll(resultDO.getResult().data);
                            profitRecordAdapter.notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(mContext,"系统异常");
                        }
                    }else{
                        ToastUtils.showToast(mContext,"网络不给力");
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<RecordDO>> call, Throwable t) {
                    ptrFrameLayout.refreshComplete();
                }
            });
        }

    }
}
