package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.ProfitStatisticAdapter;
import com.sunday.tianshehuoji.entity.Profit;
import com.sunday.tianshehuoji.entity.ProfitRecord;
import com.sunday.tianshehuoji.http.AppClient;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 刘涛 on 2017/1/9.
 */

public class BonusDetailListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private ProfitStatisticAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ProfitRecord> dataSet=new ArrayList<>();
    private int pageNo=1;
    String memberId;
    String startTime;
    String endTime;
    Profit profit;
    String allMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_common_recycler_with_head);
        ButterKnife.bind(this);
        startTime=getIntent().getStringExtra("startTime");
        endTime=getIntent().getStringExtra("endTime");
        allMoney=getIntent().getStringExtra("totalMoney");
        totalMoney.setText(allMoney);
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter=new ProfitStatisticAdapter(mContext,dataSet,3);
        recyclerView.setAdapter(adapter);

        handlerRecylerView();
        emptylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getData();
            }
        });

        getData();
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

    private void getData() {
        Call<ResultDO<Profit>> call = AppClient.getAppAdapter().getMemberRecRecords(memberId, pageNo, Constants.PAGE_SIZE, startTime, endTime,1);
        call.enqueue(new Callback<ResultDO<Profit>>() {
            @Override
            public void onResponse(Call<ResultDO<Profit>> call, Response<ResultDO<Profit>> response) {
                ResultDO<Profit> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    profit = resultDO.getResult();
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(profit.getData());
                    if (profit.getData() != null && profit.getData().size() == 20) {
                        isCanLoadMore = true;
                        pageNo++;
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                    emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Profit>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
}
