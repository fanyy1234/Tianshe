package com.sunday.member.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.sunday.member.R;
import com.sunday.member.base.BaseActivity;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RegistStepOneActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_regist_step_one);
        ButterKnife.bind(this);
        EditText mobileTxt = ButterKnife.findById(this,R.id.mobile);
        mobileTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //下一步
   public void nextStep(View view){
      //  ApiClient.getMemberAdapter().sendActiveCode()
        intent = new Intent(mContext,RegistStepTwoActivity.class);
        startActivity(intent);
    }
}
