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
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.SelectRoomAdapter;
import com.sunday.tianshehuoji.adapter.SelectServiceManAdapter;
import com.sunday.tianshehuoji.entity.Const;
import com.sunday.tianshehuoji.entity.Room;
import com.sunday.tianshehuoji.entity.Staff;
import com.sunday.tianshehuoji.entity.order.OrderConfirm;
import com.sunday.tianshehuoji.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static u.aly.av.o;

/**
 * Created by 刘涛 on 2017/1/4.
 */

public class SelectServiceManActivity extends BaseActivity {

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

    private SelectServiceManAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Staff> dataSet = new ArrayList<>();
    int pageNo = 1;

    private String shopId;
    Staff staff;
    Room room;
    private String jsonresult = "";
    private String shopType;

    private String memberId;
    private String selectRoom;
    private String selectServiceMan;
    private String shopName;
    OrderConfirm orderConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler_list);
        ButterKnife.bind(this);
        titleView.setText("选择服务员");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("确定");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        shopId = getIntent().getStringExtra("shopId");
        room= (Room) getIntent().getSerializableExtra("room");
        jsonresult=getIntent().getStringExtra("cart");
        shopName=getIntent().getStringExtra("shopName");
        shopType = getIntent().getStringExtra("shopType");
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        linearLayoutManager = new LinearLayoutManager(mContext);
        adapter = new SelectServiceManAdapter(mContext, dataSet);
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

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p = (int) v.getTag();
                staff = dataSet.get(p);
                switch (v.getId()) {
                    case R.id.img_select:
                        adapter.setSelectIndex(p);
                        adapter.notifyDataSetChanged();
                        break;
                }
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
        showLoadingDialog(0);
        Call<ResultDO<List<Staff>>> call = AppClient.getAppAdapter().getStaffList(shopId,pageNo, Constants.PAGE_SIZE);
        call.enqueue(new Callback<ResultDO<List<Staff>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<Staff>>> call, Response<ResultDO<List<Staff>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                ResultDO<List<Staff>> resultDO = response.body();
                if (resultDO != null && resultDO.getCode() == 0) {
                    if (pageNo == 1) {
                        dataSet.clear();
                    }
                    dataSet.addAll(resultDO.getResult());
                    if (resultDO.getResult().size() == 20) {
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
            public void onFailure(Call<ResultDO<List<Staff>>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }



    @OnClick(R.id.rightTxt)
    void submit() {
       orderConfirm();
    }

    private void orderConfirm(){
       if (room!=null){
           selectRoom=room.getRoomName();
       }
        selectServiceMan=staff.getName();
        Call<ResultDO<OrderConfirm>> call = AppClient.getAppAdapter().OrderConfirm(memberId, shopId, selectRoom, selectServiceMan, null, null,
                null, shopType, jsonresult);
        call.enqueue(new Callback<ResultDO<OrderConfirm>>() {
            @Override
            public void onResponse(Call<ResultDO<OrderConfirm>> call, Response<ResultDO<OrderConfirm>> response) {
                ResultDO<OrderConfirm> resultDO = response.body();
                if (resultDO == null) {
                    return;
                }
                if (resultDO.getCode() == 0) {
                    orderConfirm = resultDO.getResult();
                    intent = new Intent(mContext, ConfirmBuyActivity.class);
                    intent.putExtra("shopType", shopType);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("orderConfirm", orderConfirm);
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<OrderConfirm>> call, Throwable t) {

            }
        });
    }

}
