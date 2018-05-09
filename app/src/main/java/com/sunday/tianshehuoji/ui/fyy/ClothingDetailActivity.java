package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.entity.Address;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class ClothingDetailActivity extends BaseActivity implements View.OnClickListener{


    String memberId;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.cloth_name)
    TextView clothName;
    @Bind(R.id.new_price)
    TextView newPrice;
    @Bind(R.id.old_price)
    TextView oldPrice;
    @Bind(R.id.stock)
    TextView stock;
    @Bind(R.id.commit_btn)
    LinearLayout commitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothing_detail);
        ButterKnife.bind(this);
        titleView.setText("服装详情");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        intitView();
    }

    private void intitView() {
        commitBtn.setOnClickListener(this);
        clothName.setText("男士衬衫");
        newPrice.setText("￥100");
        oldPrice.setText("￥190");
        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        stock.setText("44");
    }

    public void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.commit_btn:
                intent = new Intent(this,ComitClothOrderActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
