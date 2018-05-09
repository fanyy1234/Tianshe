package com.sunday.tianshehuoji.ui.product;

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
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.SelectRoomAdapter;
import com.sunday.tianshehuoji.entity.Room;
import com.sunday.tianshehuoji.entity.shop.Seller;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.CamcorderProfile.get;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class SelectRoomActivity extends BaseActivity {


    @Bind(R.id.left_btn)
    ImageView leftBtn;
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

    private SelectRoomAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private List<Room> dataSet = new ArrayList<>();

    private String shopId;
    private String shopType;
    private String shopName;
    Room room;
    private String jsonresult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("选择房型");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("确定");
        shopId = getIntent().getStringExtra("shopId");
        shopType = getIntent().getStringExtra("shopType");
        shopName = getIntent().getStringExtra("shopName");
        jsonresult = getIntent().getStringExtra("cart");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        linearLayoutManager = new LinearLayoutManager(mContext);
        adapter = new SelectRoomAdapter(mContext, dataSet);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
                int p = (int) v.getTag();
                room = dataSet.get(p);
                switch (v.getId()) {
                    case R.id.img_select:
                        adapter.setSelectIndex(p);
                        adapter.notifyDataSetChanged();
                        break;
                }
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


    private void getData() {
        showLoadingDialog(0);
        Call<ResultDO<List<Room>>> call = AppClient.getAppAdapter().getRoomList(shopId);
        call.enqueue(new Callback<ResultDO<List<Room>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Room>>> call, Response<ResultDO<List<Room>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                ResultDO<List<Room>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<Room>>> call, Throwable t) {
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


    @OnClick(R.id.rightTxt)
    void submit() {
        intent = new Intent(mContext, SelectServiceManActivity.class);
        intent.putExtra("room", room);
        intent.putExtra("shopId", shopId);
        intent.putExtra("shopType", shopType);
        intent.putExtra("shopName", shopName);
        intent.putExtra("cart", jsonresult);
        startActivity(intent);
    }


}
