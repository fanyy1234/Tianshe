package com.sunday.shangjia.ui.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/1/7.
 */

public class OrderSearchActivity extends BaseActivity {


    @Bind(R.id.text_desc)
    TextView textDesc;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.edit_search)
    ClearEditText editSearch;

    private int pageNo = 1;
    private String memberId, name;
    private OrderListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Order> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);
        ButterKnife.bind(this);
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        textDesc.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrderListAdapter(mContext, dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.shape_divider_width)
                .build());
        editSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    name = editSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(name)) {
                        return true;
                    }
                    getData();
                }
                return true;
            }
        });
        handlerRecyclerView();
    }

    private void getData() {
        Call<ResultDO<OrderTotal>> call = AppClient.getAppAdapter().getOrderList(memberId, name, null, pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<OrderTotal>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderTotal>> call, Response<ResultDO<OrderTotal>> response) {
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
                        textDesc.setVisibility(View.VISIBLE);
                        textDesc.setText("订单搜索结果");
                    } else {
                        textDesc.setVisibility(View.VISIBLE);
                        textDesc.setText("暂无搜索结果");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, resultDO.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultDO<OrderTotal>> call, Throwable t) {
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });

    }

    private int lastVisibleItem;
    private boolean isCanloadMore = false;

    private void handlerRecyclerView() {
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


    @OnClick(R.id.cancel)
    void cancel() {
        finish();
    }


}
