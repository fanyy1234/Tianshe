package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.shop.MemberSize;
import com.sunday.tianshehuoji.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fyy on 2018/4/12.
 */

public class AddMemberSizeActivity extends BaseActivity {


    Integer memberId;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.rightTxt)
    TextView rightTxt;
    @Bind(R.id.right_btn)
    ImageView rightBtn;
    @Bind(R.id.name)
    ClearEditText name;
    @Bind(R.id.xiongwei)
    ClearEditText xiongwei;
    @Bind(R.id.yaowei)
    ClearEditText yaowei;
    @Bind(R.id.tunwei)
    ClearEditText tunwei;
    @Bind(R.id.yichang)
    ClearEditText yichang;
    @Bind(R.id.jiankuan)
    ClearEditText jiankuan;
    @Bind(R.id.xiuchang)
    ClearEditText xiuchang;
    private int sizeId;
    private String nameStr,xiongweiStr,yaoweiStr,tunweiStr,yichangStr,jiankuanStr,xiuchangStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_membersize);
        ButterKnife.bind(this);
        memberId = Integer.parseInt(SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, ""));
        initView();
    }

    private void initView() {
        MemberSize memberSize = (MemberSize) getIntent().getSerializableExtra("address");
        if (memberSize ==null) {
            titleView.setText("添加尺寸");
        } else {
            titleView.setText("编辑尺寸");
            sizeId = memberSize.getId();
            name.setText(memberSize.getName());
            xiongwei.setText(memberSize.getXiongwei());
            yaowei.setText(memberSize.getYaowei());
            tunwei.setText(memberSize.getTunwei());
            yichang.setText(memberSize.getYichang());
            jiankuan.setText(memberSize.getJiankuan());
            xiuchang.setText(memberSize.getXiuchang());
        }
        rightTxt.setText("确认");
    }

    @OnClick({R.id.rightTxt})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.rightTxt:
                saveSize();
                break;
        }
    }

    private void saveSize(){
        nameStr = name.getText().toString().trim();
        xiongweiStr = xiongwei.getText().toString().trim();
        yaoweiStr = yaowei.getText().toString().trim();
        tunweiStr = tunwei.getText().toString().trim();
        yichangStr = yichang.getText().toString().trim();
        jiankuanStr = jiankuan.getText().toString().trim();
        xiuchangStr = xiuchang.getText().toString().trim();
        if (TextUtils.isEmpty(nameStr)){
            ToastUtils.showToast(mContext,"请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(xiongweiStr)){
            ToastUtils.showToast(mContext,"请输入胸围");
            return;
        }
        if (TextUtils.isEmpty(yaoweiStr)){
            ToastUtils.showToast(mContext,"请输入腰围");
            return;
        }
        if (TextUtils.isEmpty(tunweiStr)){
            ToastUtils.showToast(mContext,"请输入臀围");
            return;
        }
        if (TextUtils.isEmpty(yichangStr)){
            ToastUtils.showToast(mContext,"请输入衣长");
            return;
        }
        if (TextUtils.isEmpty(jiankuanStr)){
            ToastUtils.showToast(mContext,"请输入肩宽");
            return;
        }
        if (TextUtils.isEmpty(xiuchangStr)){
            ToastUtils.showToast(mContext,"请输入袖长");
            return;
        }
        showLoadingDialog(0);
        if (sizeId==0){
            addSize();
        }
        else {
            editSize();
        }
    }

    private void editSize(){
        Call<ResultDO> call= AppClient.getAppAdapter().updateSize(sizeId,nameStr,yichangStr,jiankuanStr,
                xiongweiStr,yaoweiStr,tunweiStr,xiuchangStr);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"保存成功");
                    finish();
                }else{
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }

    private void addSize(){
        Call<ResultDO> call= AppClient.getAppAdapter().addSize(memberId,nameStr,yichangStr,jiankuanStr,
                xiongweiStr,yaoweiStr,tunweiStr,xiuchangStr);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if (isFinish){return;}
                dissMissDialog();
                if (response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"保存成功");
                    finish();
                }else{
                    ToastUtils.showToast(mContext,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                dissMissDialog();
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }
}
