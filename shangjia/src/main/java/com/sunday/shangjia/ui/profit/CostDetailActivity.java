package com.sunday.shangjia.ui.profit;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.CostListAdapter;
import com.sunday.shangjia.adapter.ProfitListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class CostDetailActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.total_money)
    TextView totalMoney;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private CostListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recylcer_list_with_head);
        ButterKnife.bind(this);
        titleView.setText("成本明细");
        linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter=new CostListAdapter(mContext);
        recyclerView.setAdapter(adapter);


    }

}
