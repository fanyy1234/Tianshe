package com.sunday.tianshehuoji.ui.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.SellerListAdapter;
import com.sunday.tianshehuoji.entity.shop.Seller;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/27.
 * 生活馆列表
 */

public class SellerListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;
    private SellerListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Seller> dataSet=new ArrayList<>();
    int pageNo=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("选择生活馆");
        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new SellerListAdapter(mContext,dataSet);
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        handlerRecylerView();
        emptylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getData();
            }
        });
        getData();
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Seller item= (Seller) v.getTag();
                Intent data=new Intent();
                data.putExtra("sellerId",item.getId());
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }


    private void handlerRecylerView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }


    private void getData(){
        showLoadingDialog(0);
        Call<ResultDO<List<Seller>>> call = AppClient.getAppAdapter().getSellerList();
        call.enqueue(new Callback<ResultDO<List<Seller>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Seller>>> call, Response<ResultDO<List<Seller>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                ResultDO<List<Seller>> resultDO = response.body();
                if (resultDO != null && resultDO.getCode() == 0) {
                        dataSet.clear();
                    dataSet.addAll(resultDO.getResult());
                    if (dataSet.size() > 0) {
                        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptylayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<List<Seller>>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }

}
