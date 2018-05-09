package com.sunday.shangjia.ui.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.ToastUtils;
import com.sunday.common.widgets.ClearEditText;
import com.sunday.member.base.BaseActivity;
import com.sunday.shangjia.R;
import com.sunday.shangjia.http.AppClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Created by 刘涛 on 2016/12/29.
 */

public class AddQrcdeActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.edit_qrcode)
    ClearEditText editQrcode;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_qrcode);
        ButterKnife.bind(this);
        titleView.setText("核销码核销");

    }

    @OnClick(R.id.btn_submit)
   void checkCode(){
        String cancelNo=editQrcode.getText().toString().trim();
        if (TextUtils.isEmpty(cancelNo)){
            return;
        }
        Call<ResultDO>call= AppClient.getAppAdapter().orderVerificationCancelNo(cancelNo);
        call.enqueue(new Callback<ResultDO>() {
            @Override
            public void onResponse(Call<ResultDO> call, Response<ResultDO> response) {
                if(response.body()==null){return;}
                if (response.body().getCode()==0){
                    ToastUtils.showToast(mContext,"核销成功");
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
