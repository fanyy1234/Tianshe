package com.sunday.shangjia.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunday.common.qrcode.CaptureActivity;
import com.sunday.member.base.BaseActivity;
import com.sunday.shangjia.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 刘涛 on 2016/12/29.
 * 二维码核销
 */

public class QrcodeVerificationActivity extends BaseActivity {


    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.imt_check)
    ImageView imtCheck;
    @Bind(R.id.rightTxt)
    TextView rightTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_verification);
        ButterKnife.bind(this);
        titleView.setText("核销");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("核销码核销");

    }

    @OnClick(R.id.imt_check)
    void check() {
        intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, 0x111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //扫描核销 刷新订单
             intent=new Intent(mContext,OrderDetailActivity.class);
            intent.putExtra("result",data.getExtras().getString("result"));
            startActivity(intent);
        }
    }

    @OnClick(R.id.rightTxt)
    void add() {
intent=new Intent(mContext,AddQrcdeActivity.class);
        startActivity(intent);
    }
}
