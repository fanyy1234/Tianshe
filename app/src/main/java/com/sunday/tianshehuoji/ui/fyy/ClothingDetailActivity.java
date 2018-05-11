package com.sunday.tianshehuoji.ui.fyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class ClothingDetailActivity extends BaseActivity implements View.OnClickListener {


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
    @Bind(R.id.cloth_img)
    ImageView clothImg;
    private String name, price, img;
    private Integer id;
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
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        img = getIntent().getStringExtra("img");
        id = getIntent().getIntExtra("id",0);
        clothName.setText(name);
        newPrice.setText("￥" + price);
        Picasso.with(this).load(img).error(R.mipmap.default_img).into(clothImg);
//        oldPrice.setText("￥190");
//        oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
//        stock.setText("44");

    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.commit_btn:
                intent = new Intent(this, ComitClothOrderActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("price",price);
                intent.putExtra("img",img);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
