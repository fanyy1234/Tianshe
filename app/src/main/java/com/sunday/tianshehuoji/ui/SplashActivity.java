package com.sunday.tianshehuoji.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.sunday.common.utils.Constants;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.R;
import com.sunday.tianshehuoji.ui.login.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 刘涛 on 2016/12/22.
 */

public class SplashActivity extends BaseActivity {


    @Bind(R.id.splash_image)
    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        splashImage.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isLogin= SharePerferenceUtils.getIns(mContext).getBoolean(Constants.IS_LOGIN,false);
                long nowTime=System.currentTimeMillis()/1000;
                long  validTime= SharePerferenceUtils.getIns(mContext).getLong("validTime",0);
                if (!isLogin&&nowTime>validTime) {
                    intent = new Intent(mContext, LoginActivity.class);
                } else {
                    intent = new Intent(mContext, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
