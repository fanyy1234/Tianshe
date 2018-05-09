package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.UIAlertView;
import com.sunday.common.widgets.recyclerView.HorizontalDividerItemDecoration;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.AddressListAdapter;
import com.sunday.tianshehuoji.adapter.MemberSizeListAdapter;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.entity.Address;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.http.AppClient;
import com.sunday.tianshehuoji.model.Visitable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fyy on 2018/4/12.
 */

public class SelectMemberSizeActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.common_header)
    RelativeLayout commonHeader;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private Integer memberId;
    private List<MemberSize> dataSet=new ArrayList<>();
    private MemberSizeListAdapter adapter;
    private boolean isSelectMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView(){
        memberId= Integer.parseInt(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"0"));
        isSelectMode=getIntent().getBooleanExtra("isSelectMode",false);
        titleView.setText("我的尺寸");
        rightTxt.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter=new MemberSizeListAdapter(mContext,dataSet);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .drawable(R.drawable.horizontal_space_divider)
                .build());
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.edite:
                        int p= (int) v.getTag();
                        MemberSize memberSize=dataSet.get(p);
//                        intent=new Intent(mContext,AddAddressActivity.class);
//                        intent.putExtra("address",memberSize);
//                        startActivity(intent);
                        break;
                    case R.id.delete:
                        final int p2= (int) v.getTag();
                        final MemberSize memberSize2=dataSet.get(p2);
                        final UIAlertView dialog=new UIAlertView(mContext,"温馨提示","确实删除此尺寸吗","取消","确定");
                        dialog.show();
                        dialog.setClicklistener(new UIAlertView.ClickListenerInterface() {
                            @Override
                            public void doLeft() {
                                dialog.dismiss();
                            }

                            @Override
                            public void doRight() {
                                delAddr(memberSize2,p2);
                                dialog.dismiss();
                            }
                        });
                        break;
                    case R.id.choice:
                        int p3= (int) v.getTag();
                        MemberSize memberSize3=dataSet.get(p3);
                        if (memberSize3.isDeleted()){
                            return;
                        }else {
                            setDefaultAddr(memberSize3,p3);
                        }
                        break;
                    case R.id.address_layout:
                        if (isSelectMode){
                            MemberSize memberSize1= (MemberSize) v.getTag();
                            Intent data=new Intent();
                            data.putExtra("address",memberSize1);
                            setResult(RESULT_OK,data);
                            finish();
                        }
                        break;
                }
            }
        });
    }

    private void delAddr(MemberSize memberSize,final int p){
        showLoadingDialog(0);
        Call<ResultDO> call= AppClient.getAppAdapter().delSize(memberSize.getId());
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                dissMissDialog();
                if (response.body()==null){
                    return;
                }
                if (response.body().getCode()==0){
                    dataSet.remove(p);
                    adapter.notifyDataSetChanged();
                    ToastUtils.showToast(mContext,"删除成功");
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);

            }
        });
    }

    private void setDefaultAddr(MemberSize memberSize,final int p){

    }



    @OnClick(R.id.add_addr)
    void addAddr(){
        intent=new Intent(mContext,AddMemberSizeActivity.class);
        startActivity(intent);
    }

    private void getData(){
        showLoadingDialog(0);
        Call<ResultDO<List<MemberSize>>> call= AppClient.getAppAdapter().sizeList(memberId);
        call.enqueue(new Callback<ResultDO<List<MemberSize>>>() {
            @Override
            public void onResponse(Call<ResultDO<List<MemberSize>>> call, Response<ResultDO<List<MemberSize>>> response) {
                if (isFinish){return;}
                dissMissDialog();
                ResultDO<List<MemberSize>> resultDO=response.body();
                if (resultDO==null){return;}
                if (resultDO.getCode()==0){
                    if (resultDO.getResult()==null){return;}
                    dataSet.clear();
                    dataSet.addAll(resultDO.getResult());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ResultDO<List<MemberSize>>> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

}
