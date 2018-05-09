package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.AccountListAdapter;
import com.sunday.tianshehuoji.adapter.CashRecordAdapter;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.entity.CashDetail;
import com.sunday.tianshehuoji.entity.CashRecord;
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
 * Created by 刘涛 on 2016/12/28.
 * 提现记录
 */

public class CashRecordActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private CashRecordAdapter adapter;
    private List<CashRecord> dataSet = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    int pageNo=1;
    String memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("提现记录");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter = new CashRecordAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        getData();
        handlerRecylerView();
        emptylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getData();
            }
        });


    }

    private boolean isCanLoadMore = false;
    private int lastVisibleItem;


    private void handlerRecylerView() {
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
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 3 == adapter.getItemCount() && isCanLoadMore == true) {
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
        showLoadingDialog(0);
        Call<ResultDO<CashDetail>> call = AppClient.getAppAdapter().getCashRecords(memberId,pageNo,Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<CashDetail>>() {
            @Override
            public void onResponse(Call<ResultDO<CashDetail>> call, Response<ResultDO<CashDetail>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                ResultDO<CashDetail> resultDO = response.body();
                if (resultDO != null && resultDO.getCode() == 0) {
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult().getData());
                    if (resultDO.getResult().getData().size()== 20) {
                        pageNo++;
                        isCanLoadMore = true;
                    }
                    if (dataSet.size() > 0) {
                        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptylayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ResultDO<CashDetail>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }
}
