package com.sunday.shangjia.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.member.base.BaseFragment;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.ProfitTotal;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.profit.CostDetailActivity;
import com.sunday.shangjia.ui.profit.ProfitDetailActivity;
import com.sunday.shangjia.util.DateUtils;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static u.aly.av.C;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class ProfitManageFragment extends BaseFragment {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.sell_total_money)
    TextView sellTotalMoney;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.date)
    TextView datetime;
    @Bind(R.id.txt_income)
    TextView txtIncome;
    @Bind(R.id.check_income)
    TextView checkIncome;
    @Bind(R.id.txt_cost)
    TextView txtCost;
    @Bind(R.id.check_cost)
    TextView checkCost;
    @Bind(R.id.txt_profit)
    TextView txtProfit;

    String startTime;
    String endTime;
    String memberId;
    ProfitTotal profitTotal;

    TimePickerView timePickerView;
    Calendar calendar;
    Date date;
    SimpleDateFormat sdf;
    SimpleDateFormat sdf1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_profit_manange, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        memberId = SharePerferenceUtils.getIns(mContext).getString("shopId", "");
        startTime = DateUtils.dateToStr(DateUtils.getTimesLastMonthmorning());
        endTime = DateUtils.dateToStr(DateUtils.getTimesLastMonthnight());
        datetime.setText(DateUtils.dateToStr2(DateUtils.getTimesLastMonthmorning()));
        time.setText(DateUtils.dateToStr2(DateUtils.getTimesLastMonthnight()));
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        getData();
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINESE);
        sdf1 = new SimpleDateFormat("yyyy-MM", Locale.CHINESE);
    }

    private void getData() {
        Call<ResultDO<ProfitTotal>> call = AppClient.getAppAdapter().getProfitTotal(memberId, startTime, endTime);
        call.enqueue(new Callback<ResultDO<ProfitTotal>>() {
            @Override
            public void onResponse(Call<ResultDO<ProfitTotal>> call, Response<ResultDO<ProfitTotal>> response) {
                if (response.body() == null) {
                    return;
                }
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                if (response.body().getCode() == 0) {
                    if (response.body().getResult() == null) {
                        return;
                    }
                    profitTotal = response.body().getResult();
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<ProfitTotal>> call, Throwable t) {
                if (refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    private void updateView() {
        if (profitTotal == null) {
            return;
        }
        sellTotalMoney.setText(String.format("%s", profitTotal.getThisMonthTotalMoney().setScale(2, RoundingMode.HALF_UP)));
        txtIncome.setText(String.format("收入:%s",profitTotal.getOtherMonthTotalMoney().setScale(2,RoundingMode.HALF_UP)));
        txtCost.setText(String.format("成本:%s",profitTotal.getOtherMonthTotalCostMoney().setScale(2,RoundingMode.HALF_UP)));
        txtProfit.setText(String.format("利润:%s",profitTotal.getOtherMonthTotalProfitMoney().setScale(2,RoundingMode.HALF_UP)));
    }

    @OnClick(R.id.date)
    void selectStartTime() {
        timePickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
        timePickerView.setCyclic(false);
        timePickerView.setRange(2016,2050);
        timePickerView.setRangeMonth(1, 12);//还须修改
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                date.setDate(1);
                date.setHours(0);
                date.setMinutes(0);
                date.setSeconds(1);
                datetime.setText(sdf.format(date));
                startTime=sdf1.format(date);
                if (!TextUtils.isEmpty(endTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

    @OnClick(R.id.time)
    void selectEndTime() {
        timePickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
        timePickerView.setCyclic(false);
        timePickerView.setRange(2016,2050);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                date.setDate(31);
                date.setHours(23);
                date.setMinutes(59);
                date.setSeconds(59);
                time.setText(sdf.format(date));
                endTime=sdf1.format(date);
                if (!TextUtils.isEmpty(startTime)){
                    getData();
                }
            }
        });
        timePickerView.show();
    }

    @OnClick({R.id.check_income,R.id.txt_income,R.id.profit_img3})
    void checkIncome() {
        intent = new Intent(mContext, ProfitDetailActivity.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @OnClick({R.id.check_cost,R.id.txt_cost,R.id.profit_img2})
    void checkCost() {
        intent = new Intent(mContext, ProfitDetailActivity.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("type", 2);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
