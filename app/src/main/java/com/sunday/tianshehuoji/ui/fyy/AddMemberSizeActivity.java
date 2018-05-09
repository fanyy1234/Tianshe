package com.sunday.tianshehuoji.ui.fyy;

import android.os.Bundle;
import android.widget.TextView;

import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fyy on 2018/4/12.
 */

public class AddMemberSizeActivity extends BaseActivity {


    String memberId;
    @Bind(R.id.title_view)
    TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_membersize);
        ButterKnife.bind(this);
        titleView.setText("添加人员");
        memberId= SharePerferenceUtils.getIns(mContext).getString(Constants.MEMBERID,"");
    }

}
