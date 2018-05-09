package com.sunday.tianshe.branch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.CacheUtils;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.tianshe.branch.R;
import com.sunday.tianshe.branch.entity.Version;
import com.sunday.tianshe.branch.http.ApiClient;
import com.umeng.update.UpdateDialogActivity;
import com.umeng.update.UpdateResponse;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.tv_cacheSize)
    TextView tvCacheSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        titleView.setText("设置");
    }

    @OnClick(R.id.check_update)
    void checkForUpdate(){
        Call<ResultDO<Version>> call = ApiClient.getApiAdapter().checkVersion(DeviceUtils.getVersionCode(mContext),2);
        call.enqueue(new Callback<ResultDO<Version>>() {
            @Override
            public void onResponse(Call<ResultDO<Version>> call, Response<ResultDO<Version>> response) {
                ResultDO<Version> resultDO = response.body();
                if(resultDO!=null&&resultDO.getCode()==0){
                    Version version = resultDO.getResult();
                    Intent intent = new Intent(mContext, UpdateDialogActivity.class);
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("update","Yes");
                    params.put("version",version.getVersionName());
                    params.put("update_log",version.getFileLogs());
                    params.put("version",version.getVersionCode());
                    params.put("path",version.getDownLoadUrl());
                    params.put("target_size",Long.parseLong(version.getFizeSize())/1024/1024+"M");
                    params.put("new_md5",version.getFileMd5());
                    UpdateResponse updateResponse = new UpdateResponse(new JSONObject(params));
                    intent.putExtra("response",updateResponse);
                    intent.putExtra("force",true);
                    startActivity(intent);
                }else{
                    ToastUtils.showToast(mContext,"已经是最新版本");
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Version>> call, Throwable t) {

            }
        });

    }


    @OnClick(R.id.l_clearCache)
    void clear() {
        CacheUtils.clearAllCache(mContext);
        tvCacheSize.setText(CacheUtils.getTotalCacheSize(mContext));
    }

}
