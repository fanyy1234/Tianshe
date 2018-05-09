package com.sunday.tianshehuoji.ui.manage;

import android.content.Intent;
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
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.VotingListAdapter;
import com.sunday.tianshehuoji.entity.Profit;
import com.sunday.tianshehuoji.entity.Vote;
import com.sunday.tianshehuoji.entity.VoteItem;
import com.sunday.tianshehuoji.entity.shop.Seller;
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
 * Created by 刘涛 on 2016/12/20.
 */

public class VotingListActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private VotingListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    int pageNo=1;
    private List<VoteItem> dataSet=new ArrayList<>();
    Vote vote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        titleView.setText("投票参与");
        linearLayoutManager = new LinearLayoutManager(mContext);
        adapter = new VotingListAdapter(mContext,dataSet);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        recyclerView.setLayoutManager(linearLayoutManager);
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


    private void getData(){
        showLoadingDialog(0);
        Call<ResultDO<Vote>> call = AppClient.getAppAdapter().getVoteList(pageNo,Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<Vote>>() {
            @Override
            public void onResponse(Call<ResultDO<Vote>> call, Response<ResultDO<Vote>> response) {
                dissMissDialog();
                ResultDO<Vote> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    if (resultDO.getResult() == null) {
                        return;
                    }
                    emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    vote = resultDO.getResult();
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(vote.getData());
                    if (vote.getData() != null && vote.getData().size() == 20) {
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
            public void onFailure(Call<ResultDO<Vote>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }
}
