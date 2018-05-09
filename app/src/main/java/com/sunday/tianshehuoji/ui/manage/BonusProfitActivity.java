package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.BonusProfitAdapter;
import com.sunday.tianshehuoji.entity.Bonus;
import com.sunday.tianshehuoji.entity.BonusRecord;
import com.sunday.tianshehuoji.entity.Income;
import com.sunday.tianshehuoji.http.AppClient;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2017/1/9.
 * 分红统计
 */


public class BonusProfitActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.can_apply_money)
    TextView canApplyMoney;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private BonusProfitAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private String memberId;
    private List<BonusRecord> dataSet=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_profit_statistic);
        ButterKnife.bind(this);
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new BonusProfitAdapter(mContext,dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        recyclerView.setAdapter(adapter);
        getBonus();
    }

    private void getData(){
        Call<ResultDO<Income>> call= AppClient.getAppAdapter().getDividends(memberId,1);
        call.enqueue(new Callback<ResultDO<Income>>() {
            @Override
            public void onResponse(Call<ResultDO<Income>> call, Response<ResultDO<Income>> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    updateView(response.body().getResult());
                    getBonus();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Income>> call, Throwable t) {

            }
        });
    }

    private void updateView(Income income){
        totalMoney.setText(String.format("￥%s",income.getTotalMoney().setScale(2, RoundingMode.HALF_UP)));
        canApplyMoney.setText(String.format("￥%s",income.getTotalWithdraw().setScale(2, RoundingMode.HALF_UP)));
    }

    private void getBonus(){
        Call<ResultDO<Bonus>> call=AppClient.getAppAdapter().getMemberBonusById(memberId);
        call.enqueue(new Callback<ResultDO<Bonus>>() {
            @Override
            public void onResponse(Call<ResultDO<Bonus>> call, Response<ResultDO<Bonus>> response) {
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    totalMoney.setText(String.format("￥%s",response.body().getResult().getBonusMoney()));
                    canApplyMoney.setText(String.format("￥%s",response.body().getResult().getWithdrawMoney()));
                    dataSet.addAll(response.body().getResult().getList());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Bonus>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.left_btn)
    void back(){
        finish();
    }

}
