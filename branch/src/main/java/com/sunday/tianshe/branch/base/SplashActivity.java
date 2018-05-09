package com.sunday.tianshe.branch.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshe.branch.MainActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.ui.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_view)
    RelativeLayout splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        ButterKnife.bind(this);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        splashView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isLogin = SharePerferenceUtils.getIns(mContext).getBoolean("is_login",false);
                if(isLogin){
                    String str = SharePerferenceUtils.getIns(mContext).getString("member",null);
                    if(TextUtils.isEmpty(str)){
                        intent = new Intent(mContext,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return ;
                    }
                    Member member = new Gson().fromJson(str,Member.class);
                    BaseApp.getInstance().setMember(member);
                    intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
