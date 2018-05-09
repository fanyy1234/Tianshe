package com.sunday.shangjia.ui.manage;

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
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.ChildAccountListAdapter;
import com.sunday.shangjia.adapter.EmployeeListAdapter;
import com.sunday.shangjia.entity.ChildAccount;
import com.sunday.shangjia.entity.Staff;
import com.sunday.shangjia.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class ChildAccountManageActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;
    @Bind(R.id.rightTxt)
    TextView rightTxt;


    private ChildAccountListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int pageNo=1;
    private List<ChildAccount> dataSet=new ArrayList<>();
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("子帐号管理");
        rightTxt.setText("添加");
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        rightTxt.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter = new ChildAccountListAdapter(mContext,dataSet);
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
        Call<ResultDO<List<ChildAccount>>> call = AppClient.getAppAdapter().getChildAccount(memberId,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<ChildAccount>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ChildAccount>>> call, Response<ResultDO<List<ChildAccount>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                ResultDO<List<ChildAccount>> resultDO = response.body();
                if (resultDO != null && resultDO.getCode() == 0) {
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult());
                    if (resultDO.getResult().size()== 20) {
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
            public void onFailure(Call<ResultDO<List<ChildAccount>>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }



    @OnClick(R.id.rightTxt)
    void add(){
        intent=new Intent(mContext,AddChildAccountActivity.class);
        startActivity(intent);
    }

}
