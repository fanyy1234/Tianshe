package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.sunday.tianshehuoji.entity.Income;
import com.sunday.tianshehuoji.entity.Profit;
import com.sunday.tianshehuoji.entity.ProfitRecord;
import com.sunday.tianshehuoji.entity.ShareProfit;
import com.sunday.tianshehuoji.entity.shop.ShopDetail;
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
 * Created by 刘涛 on 2017/1/9.
 */

public class ShareProfitActivity extends BaseActivity {

    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.can_apply_money)
    TextView canApplyMoney;
    @Bind(R.id.date)
    TextView txt_date;
    @Bind(R.id.time)
    TextView txt_time;
    @Bind(R.id.text_total)
    TextView textTotal;
    @Bind(R.id.rl_select_time)
    RelativeLayout rlSelectTime;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private String memberId;
    String startTime;
    String endTime;
    TimePickerView timePickerView;
    Calendar calendar;
    Date date;
    int pageNo=1;
    private Profit profit;
    private LinearLayoutManager linearLayoutManager;
    private List<ProfitRecord> dataSet = new ArrayList<>();
    private ProfitStatisticAdapter adapter;
    SimpleDateFormat sdf;
    SimpleDateFormat sdf1;
    SimpleDateFormat sdf2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profit_statistic);
        ButterKnife.bind(this);
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        startTime= DateUtils.dateToStr(DateUtils.getTimesLastMonthmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesLastMonthnight());
        linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProfitStatisticAdapter(mContext, dataSet,1);
        recyclerView.setAdapter(adapter);
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINESE);
        sdf1 = new SimpleDateFormat("yyyy-MM", Locale.CHINESE);
        sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
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
        Call<ResultDO<ShareProfit>> call= AppClient.getAppAdapter().getRecStatistics(memberId,pageNo,Constants.PAGE_SIZE,startTime,endTime);
        call.enqueue(new Callback<ResultDO<ShareProfit>>() {
            @Override
            public void onResponse(Call<ResultDO<ShareProfit>> call, Response<ResultDO<ShareProfit>> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    ResultDO<ShareProfit> resultDO=response.body();
                    if (pageNo == 1) {
                        dataSet.clear();
                        totalMoney.setText(String.format("￥%s",resultDO.getResult().getTotalMoney().setScale(2, RoundingMode.HALF_UP)));
                        canApplyMoney.setText(String.format("￥%s",resultDO.getResult().getTotalWithdraw().setScale(2, RoundingMode.HALF_UP)));
                    }
                    dataSet.addAll(resultDO.getResult().getList().getData());
                    if (dataSet.size()>0){
                        emptylayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                    }else {
                        emptylayout.setErrorType(EmptyLayout.NODATA);
                    }
                    profit=resultDO.getResult().getList();
                    if (profit.getData() != null && profit.getData().size() == 20) {
                        isCanLoadMore = true;
                        pageNo++;
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    ToastUtils.showToast(mContext,R.string.network_error);
                    emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ShareProfit>> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
                    emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }




    @OnClick(R.id.select_month)
    void selectMonth(){
        rlSelectTime.setVisibility(View.GONE);
        startTime= DateUtils.dateToStr(DateUtils.getTimesLastMonthmorning());
        endTime=DateUtils.dateToStr(DateUtils.getTimesLastMonthnight());
        getData();
    }



    @OnClick(R.id.select_self)
    void selectSelf() {
        textTotal.setText("");
        rlSelectTime.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.date)
    void selectBeginTime() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
        timePickerView.setCyclic(false);
        timePickerView.setRange(2016,2050);
        timePickerView.setRangeMonth(1, 12);//还须修改
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                txt_date.setText(sdf.format(date));
                date.setDate(1);
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(1);
                startTime=sdf2.format(date);
                if (!TextUtils.isEmpty(endTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

    @OnClick(R.id.time)
    void selectEndTime() {
        timePickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH);
        timePickerView.setCyclic(false);
        timePickerView.setRange(2016,2050);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                txt_time.setText(sdf.format(date));
                Calendar calendar = Calendar.getInstance();
                // 设置时间,当前时间不用设置
                 calendar.setTime(date);
                // 设置日期为本月最大日期
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,59);
                endTime=sdf2.format(calendar.getTime());
                if (!TextUtils.isEmpty(startTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

    @OnClick(R.id.left_btn)
    void back(){
        finish();
    }
}
