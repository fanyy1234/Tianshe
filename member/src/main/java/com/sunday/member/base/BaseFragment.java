package com.sunday.member.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.sunday.common.event.EventBus;
import com.sunday.common.widgets.dialog.ACProgressFlower;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/2/23.
 */
public class BaseFragment extends Fragment {
    protected View rootView;
    protected Context mContext;
    protected Intent intent;
    protected ACProgressFlower progressDialog;
    //标识是否显示对话框
    protected boolean progressShow;

    protected boolean isFinish = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);//反注册EventBus
        }
        ButterKnife.unbind(this);
        isFinish = true;
    }

    public void showLoadingDialog(int res) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .text(res == 0 ? "正在加载中..." : getString(res))
                .build();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dissMissDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
