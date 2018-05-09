package com.sunday.tianshehuoji.ui.manage;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.StringUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 刘涛 on 2016/12/28.
 */

public class FeedBackActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_advice)
    ClearEditText editAdvice;
    @Bind(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.submit)
    void submit(){
        String advice=editAdvice.getText().toString().trim();
        if(TextUtils.isEmpty(advice)){
            ToastUtils.showToast(mContext, "请填写具体内容", ToastUtils.LENGTH_SHORT);
            return;
        }
        showLoadingDialog(0);
        Call<ResultDO> call= AppClient.getAppAdapter().saveFeedBack(advice);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                ResultDO resultDO=response.body();
                if(resultDO!=null&&resultDO.getCode()==0){
                    ToastUtils.showToast(mContext,"提交成功，感谢您的反馈",ToastUtils.LENGTH_SHORT);
                    finish();
                }

            }
            @Override
            public void onFailure(Call<ResultDO> call, Throwable t) {
                ToastUtils.showToast(mContext,R.string.network_error);
            }
        });
    }
}
