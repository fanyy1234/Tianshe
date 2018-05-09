package com.sunday.tianshehuoji.ui.manage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.ProfitStatisticAdapter;
import com.sunday.tianshehuoji.entity.Profit;
import com.sunday.tianshehuoji.entity.ProfitRecord;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.utils.DateUtils;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/19.
 */

public class ProfitStatisticActivity extends BaseActivity {


    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.left_txt)
    TextView leftTxt;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.txt)
    TextView txt;
    @Bind(R.id.text_total)
    TextView textTotal;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;
    @Bind(R.id.date)
    TextView selectTime1;
    @Bind(R.id.time)
    TextView selectTime2;
    @Bind(R.id.rl_select_time)
    RelativeLayout rlSelectTime;

    private ProfitStatisticAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    TimePickerView timePickerView;
    Calendar calendar;
    String memberId;
    int pageNo = 1;
    String startTime;
    String endTime;
    Date date;
    private List<ProfitRecord> dataSet = new ArrayList<>();
    private Profit profit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_statistic);
        ButterKnife.bind(this);
        titleView.setText("收益统计");
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        adapter = new ProfitStatisticAdapter(mContext, dataSet,1);
        linearLayoutManager = new LinearLayoutManager(mContext);
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        startTime= DateUtils.dateToStr(DateUtils.getTimesmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesnight());
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



    private void getData() {
        Call<ResultDO<Profit>> call = AppClient.getAppAdapter().getProfit(memberId, pageNo, Constants.PAGE_SIZE, startTime, endTime);
        call.enqueue(new Callback<ResultDO<Profit>>() {
            @Override
            public void onResponse(Call<ResultDO<Profit>> call, Response<ResultDO<Profit>> response) {
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
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
                        textTotal.setText(String.format("￥%s",profit.getTotal().setScale(2, RoundingMode.HALF_UP)));
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
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                ToastUtils.showToast(mContext, R.string.network_error);
            }
        });
    }



    @OnClick(R.id.select_day)
    void selectDay() {
        rlSelectTime.setVisibility(View.GONE);

      startTime= DateUtils.dateToStr(DateUtils.getTimesmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesnight());
        getData();
    }


    @OnClick(R.id.select_self)
    void selectSelf() {
        rlSelectTime.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.select_week)
    void selectWeek(){
        rlSelectTime.setVisibility(View.GONE);
        startTime= DateUtils.dateToStr(DateUtils.getTimesWeekmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesWeeknight());
        getData();
    }

    @OnClick(R.id.select_month)
    void selectMonth(){
        rlSelectTime.setVisibility(View.GONE);
        startTime= DateUtils.dateToStr(DateUtils.getTimesMonthmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesMonthnight());
        getData();
    }

    @OnClick(R.id.date)
    void selectBeginTime() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(false);
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setRangeMonth(1, 12);//还须修改
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                selectTime1.setText(sdf.format(date));
                startTime=String.format("%s 00:00:01",selectTime1.getText().toString());
                if (!TextUtils.isEmpty(endTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

    @OnClick(R.id.time)
    void selectEndTime() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        timePickerView.setCyclic(false);
        timePickerView.setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR));
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
                selectTime2.setText(sdf.format(date));
                endTime=String.format("%s 23:59:59",selectTime2.getText().toString());
                if (!TextUtils.isEmpty(startTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

}
