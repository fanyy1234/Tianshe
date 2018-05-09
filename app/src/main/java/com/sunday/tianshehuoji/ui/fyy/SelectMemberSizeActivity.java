package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.adapter.MultiTypeAdapter;
import com.sunday.tianshehuoji.model.Visitable;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class SelectMemberSizeActivity extends BaseActivity {
    List<Visitable> models = new ArrayList<Visitable>();
    LinearLayoutManager layoutManager;
    MultiTypeAdapter adapter;

    String memberId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_size);
        ButterKnife.bind(this);
        titleView.setText("选择人员");
        rightTxt.setVisibility(View.VISIBLE);
        rightTxt.setText("添加人员");
        memberId = SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID, "");
        initView();
    }

    private void initView(){

    }

}
