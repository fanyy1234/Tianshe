package com.sunday.tianshehuoji.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.AccountListAdapter;
import com.sunday.tianshehuoji.entity.CashAccount;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class AccountListActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptyLayout;

    private AccountListAdapter adapter;
    private List<CashAccount> dataSet = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    String memberId;
    boolean isSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("我的账户");
        isSelect=getIntent().getBooleanExtra("isSelect",false);
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.mipmap.earnings_top_add);
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "0");
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter = new AccountListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        handlerRecyclerView();
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getData();
            }
        });
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.del:
                        final int p = (int) v.getTag();
                        Call<ResultDO> call = AppClient.getAppAdapter().delAccount(dataSet.get(p).getId());
                        call.enqueue(new Callback<ResultDO>() {
                            @Override
                            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                                ResultDO resultDO = response.body();
                                if (resultDO == null) {
                                    return;
                                }
                                if (resultDO.getCode() == 0) {
                                    dataSet.remove(p);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultDO> call, Throwable t) {

                            }
                        });
                        break;
                    case R.id.viewItem:
                        CashAccount item= (CashAccount) v.getTag();
                        if (isSelect){
                            Intent data=new Intent();
                            data.putExtra("account",item);
                            setResult(RESULT_OK,data);
                            finish();
                        }else{
                            intent = new Intent(mContext, AddAccountActivity.class);
                            intent.putExtra("isEdit",true);
                            intent.putExtra("cashAccount",item);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
        getData();
    }

    @OnClick(R.id.right_btn)
    void add() {
        intent = new Intent(mContext, AddAccountActivity.class);
        startActivity(intent);
    }


    private void handlerRecyclerView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    private void getData() {
             Call<ResultDO<List<CashAccount>>> call = AppClient.getAppAdapter().getAccountList(memberId);
        call.enqueue(new Callback<ResultDO<List<CashAccount>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<CashAccount>>> call, Response<ResultDO<List<CashAccount>>> response) {
                if (isFinish) {
                    return;
                }
                refreshLayout.setRefreshing(false);
                ResultDO<List<CashAccount>> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0 && resultDO.getResult() != null) {
                    dataSet.clear();
                    dataSet.addAll(resultDO.getResult());

                    if (dataSet.size() > 0) {
                        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    } else {
                        emptyLayout.setErrorType(EmptyLayout.NODATA);
                    }
                    adapter.notifyDataSetChanged();

                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                    emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<CashAccount>>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                ToastUtils.showToast(mContext, R.string.network_error);
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);

            }
        });

    }



}
