package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.adapter.ChildAccountAdapter;
import com.sunday.tianshe.branch.adapter.MemberAuthAdapter;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.BaseMember;
import com.sunday.tianshe.branch.entity.ChildAccount;
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
 * Created by admin on 2016/12/21.
 */

public class ChildAccountListActivity extends BaseActivity {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ptr_frame)
    PtrClassicFrameLayout ptrFrameLayout;
    @Bind(R.id.rightTxt)
    TextView rightTxt;

    private ChildAccountAdapter childAccountAdapter;
    private List<ChildAccount> dataSet = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_member_auth);
        ButterKnife.bind(this);
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("新增");
        getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        childAccountAdapter = new ChildAccountAdapter(mContext, dataSet);
        recyclerView.setAdapter(childAccountAdapter);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });
    }

    @OnClick(R.id.rightTxt)
    void rightClick(){
        intent = new Intent(mContext,AddChildAccountActivity.class);
        startActivityForResult(intent,0x111);
    }


    private void getData(){
        Call<ResultDO<List<ChildAccount>>> call = ApiClient.getApiAdapter().childAccountList(BaseApp.getInstance().getMember().id,
                BaseApp.getInstance().getMember().sellerId);
        call.enqueue(new Callback<ResultDO<List<ChildAccount>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<ChildAccount>>> call, Response<ResultDO<List<ChildAccount>>> response) {
                ResultDO<List<ChildAccount>> resultDO = response.body();
                ptrFrameLayout.refreshComplete();
                if(resultDO!=null){
                    if(resultDO.getCode()==0){
                        dataSet.clear();
                        dataSet.addAll(resultDO.getResult());
                        childAccountAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<ChildAccount>>> call, Throwable t) {
                ptrFrameLayout.refreshComplete();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            getData();
        }
    }
}
