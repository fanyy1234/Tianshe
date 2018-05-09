package com.sunday.tianshe.branch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.pickerview.TimePickerView;
import com.sunday.member.base.BaseFragment;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Profit;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.ui.InComeDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收益统计
 */
public class ProfitStatisticsFragment extends BaseFragment {


    @Bind(R.id.total_amount)
    TextView totalAmount;
    @Bind(R.id.income_txt)
    TextView incomeTxt;
    @Bind(R.id.cost_txt)
    TextView costTxt;
    @Bind(R.id.profit_txt)
    TextView profitTxt;
    @Bind(R.id.start_time)
    TextView startTime;
    @Bind(R.id.end_time)
    TextView endTime;

    private boolean isStartSelect;

    private TimePickerView timePickerView;

    private SimpleDateFormat sdf ,sdf1;
    private static String ARG_PARAM1 = "shopId";
    private static String PAGE_TYPE = "PAGE_TYPE"; //0分店 类型  1是代表生活馆

    private String shopId;

    private int pageType;

    private String startTimeStr,endTimeStr;

    public static ProfitStatisticsFragment newInstance(String shopId,int pageType) {
        ProfitStatisticsFragment fragment = new ProfitStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1,shopId);
        bundle.putInt(PAGE_TYPE,pageType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shopId = getArguments().getString(ARG_PARAM1);
            pageType = getArguments().getInt(PAGE_TYPE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profit_statistics, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        timePickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH);
        timePickerView.setCyclic(false);
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if(isStartSelect){
                    startTimeStr = sdf.format(date);
                    startTime.setText(sdf1.format(date));
                }else{
                    endTimeStr = sdf.format(date);
                    endTime.setText(sdf1.format(date));
                }
            }
        });
        Date date = Calendar.getInstance().getTime();
        endTimeStr = sdf.format(date);
        endTime.setText(sdf1.format(date));
        date.setMonth(date.getMonth()-1);
        startTimeStr = sdf.format(date);
        startTime.setText(sdf1.format(date));
        getProfitStatstic();

    }

    @OnClick(R.id.start_time)
    void startTimeClick(){
        isStartSelect = true;
        timePickerView.show();

    }

    @OnClick(R.id.end_time)
    void endTimeClick(){
        isStartSelect = false;
        timePickerView.show();
    }

    @OnClick(R.id.income_layout)
    void inComeLayoutClick(){
        intent = new Intent(mContext, InComeDetailActivity.class);
        intent.putExtra("type",1);
        intent.putExtra("pageType",pageType);
        intent.putExtra("startTime",startTimeStr);
        intent.putExtra("endTime",endTimeStr);
       intent.putExtra("shopId",shopId);
        startActivity(intent);
    }

    @OnClick(R.id.cost_layout)
    void costLayoutClick(){
        intent = new Intent(mContext, InComeDetailActivity.class);
        intent.putExtra("type",2);
        intent.putExtra("pageType",pageType);
        intent.putExtra("startTime",startTimeStr);
        intent.putExtra("endTime",endTimeStr);
        intent.putExtra("shopId",shopId);
        startActivity(intent);
    }


    @OnClick(R.id.profit_layout)
    void profitLayoutClick(){

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void getProfitStatstic(){
        if(pageType==0){
            Call<ResultDO<Profit>> call = ApiClient.getApiAdapter().getShopProfit(Long.parseLong(shopId),startTimeStr,endTimeStr);
            call.enqueue(new Callback<ResultDO<Profit>>() {
                @Override
                public void onResponse(Call<ResultDO<Profit>> call, Response<ResultDO<Profit>> response) {
                    ResultDO<Profit> resultDO = response.body();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            incomeTxt.setText("￥:"+resultDO.getResult().otherMonthTotalProfitMoney);
                            costTxt.setText("￥:"+resultDO.getResult().otherMonthTotalMoney);
                            profitTxt.setText("￥:"+resultDO.getResult().otherMonthTotalCostMoney);
                            totalAmount.setText("￥"+resultDO.getResult().thisMonthTotalMoney );
                        }else{
                            ToastUtils.showToast(mContext,resultDO.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<Profit>> call, Throwable t) {

                }
            });
        }else{
            Call<ResultDO<Profit>> call = ApiClient.getApiAdapter().getShopProfit2(Long.parseLong(shopId),startTimeStr,endTimeStr);
            call.enqueue(new Callback<ResultDO<Profit>>() {
                @Override
                public void onResponse(Call<ResultDO<Profit>> call, Response<ResultDO<Profit>> response) {
                    ResultDO<Profit> resultDO = response.body();
                    if(resultDO!=null){
                        if(resultDO.getCode()==0){
                            incomeTxt.setText("￥:"+resultDO.getResult().otherMonthTotalProfitMoney);
                            costTxt.setText("￥:"+resultDO.getResult().otherMonthTotalMoney);
                            profitTxt.setText("￥:"+resultDO.getResult().otherMonthTotalCostMoney);
                            totalAmount.setText("￥"+resultDO.getResult().thisMonthTotalMoney );
                        }else{
                            ToastUtils.showToast(mContext,resultDO.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultDO<Profit>> call, Throwable t) {

                }
            });
        }

    }
}
