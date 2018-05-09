package com.sunday.shangjia.ui.manage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.widgets.EmptyLayout;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.adapter.CommentListAdapter;
import com.sunday.shangjia.adapter.CostListAdapter;
import com.sunday.shangjia.entity.CommentScore;
import com.sunday.shangjia.entity.OrderComment;
import com.sunday.shangjia.http.AppClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/21.
 */

public class CustomerCommentActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.environ_star)
    RatingBar environStar;
    @Bind(R.id.shop_star)
    RatingBar shopStar;
    @Bind(R.id.service_star)
    RatingBar serviceStar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.emptylayout)
    EmptyLayout emptylayout;

    private CommentListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    int pageNo=1;
    String memberId;
    CommentScore commentScore;
    private List<OrderComment> dataSet=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_layout);
        ButterKnife.bind(this);
        emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
        titleView.setText("用户评价");
        linearLayoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext).drawable(R.drawable.shape_divider).build());
        refreshLayout.setColorSchemeResources(R.color.material_red, R.color.material_green,
                R.color.material_blue, R.color.material_yellow);
        adapter=new CommentListAdapter(mContext,dataSet);
        recyclerView.setAdapter(adapter);
        handlerRecylerView();
        emptylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptylayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                getData();
            }
        });
        getScore();
        getData();

    }

    private void getScore(){
        Call<ResultDO<CommentScore>> call= AppClient.getAppAdapter().getScore(memberId);
        call.enqueue(new Callback<ResultDO<CommentScore>>() {
            @Override
            public void onResponse(Call<ResultDO<CommentScore>> call, Response<ResultDO<CommentScore>> response) {
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    if (response.body().getResult()==null){return;}
                    commentScore=response.body().getResult();
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<ResultDO<CommentScore>> call, Throwable t) {

            }
        });
    }

    private void updateView(){
        if (commentScore==null){return;}
        environStar.setRating(commentScore.getHealth());
        shopStar.setRating(commentScore.getQuality());
        serviceStar.setRating(commentScore.getService());
    }



    private void handlerRecylerView() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });



    }

    private void getData(){
        showLoadingDialog(0);
        Call<ResultDO<List<OrderComment>>> call = AppClient.getAppAdapter().getCommentList(memberId);
        call.enqueue(new Callback<ResultDO<List<OrderComment>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<OrderComment>>> call, Response<ResultDO<List<OrderComment>>> response) {
                if (isFinish) {
                    return;
                }
                dissMissDialog();
                refreshLayout.setRefreshing(false);
                ResultDO<List<OrderComment>> resultDO = response.body();
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
            public void onFailure(Call<ResultDO<List<OrderComment>>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                emptylayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
        });
    }


}
