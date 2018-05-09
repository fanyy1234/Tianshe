package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.adapter.MemberAuthAdapter;
import com.sunday.tianshe.branch.adapter.RetMemberAdapter;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.http.ApiClient;
import com.sunday.tianshe.branch.widgets.ptr.PtrClassicFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrDefaultHandler;
import com.sunday.tianshe.branch.widgets.ptr.PtrFrameLayout;
import com.sunday.tianshe.branch.widgets.ptr.PtrHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 推荐人列表
 */
public class RecommondMemberActivity extends BaseActivity {

    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrameLayout;

    private RetMemberAdapter retMemberAdapter;
    private List<BaseMember> dataSet = new ArrayList();
    public int pageNo = 1;
    public int pageSize = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommond_member);
        ButterKnife.bind(this);
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("确定");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        retMemberAdapter = new RetMemberAdapter(mContext,dataSet);
        recyclerView.setAdapter(retMemberAdapter);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageNo = 1;
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
        getData();
    }

    @OnClick(R.id.rightTxt)
    void rightTxtClick(){
        BaseMember member = retMemberAdapter.getSelectMember();
        if(member==null){
            ToastUtils.showToast(mContext,"请选择推荐人");
            return ;
        }
        intent = new Intent();
        intent.putExtra("data",member);
        setResult(RESULT_OK,intent);
        finish();

    }



    private void getData(){
        Call<ResultDO<List<BaseMember>>> call = ApiClient.getApiAdapter().recList(pageNo,pageSize);
        call.enqueue(new Callback<ResultDO<List<BaseMember>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<BaseMember>>> call, Response<ResultDO<List<BaseMember>>> response) {
                ResultDO<List<BaseMember>> resultDO = response.body();
                ptrFrameLayout.refreshComplete();
                if(resultDO!=null){
                    if(resultDO.getCode()==0){
                        if(pageNo==1){
                            dataSet.clear();
                        }
                        if(resultDO.getResult().size()==20){
                            pageNo++;
                        }
                        dataSet.addAll(resultDO.getResult());
                        retMemberAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<BaseMember>>> call, Throwable t) {
                ptrFrameLayout.refreshComplete();
            }
        });
    }
}
