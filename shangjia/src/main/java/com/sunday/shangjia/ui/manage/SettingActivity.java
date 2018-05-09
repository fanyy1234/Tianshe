package com.sunday.shangjia.ui.manage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.CacheUtils;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.ToastUtils;
import com.sunday.member.base.BaseActivity;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Version;
import com.sunday.shangjia.http.AppClient;
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

/**
 * Created by 刘涛 on 2016/12/30.
 */

public class SettingActivity extends BaseActivity {
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.tv_updateVersion)
    TextView tvUpdateVersion;
    @Bind(R.id.tv_cacheSize)
    TextView tvCacheSize;
    @Bind(R.id.l_clearCache)
    LinearLayout lClearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        titleView.setText("设置");
        tvCacheSize.setText(CacheUtils.getTotalCacheSize(mContext));


    }

    @OnClick(R.id.l_clearCache)
    void clear() {
        CacheUtils.clearAllCache(mContext);
        tvCacheSize.setText(CacheUtils.getTotalCacheSize(mContext));
    }
    @OnClick(R.id.tv_updateVersion)
    void update(){
        Call<ResultDO<Version>> call= AppClient.getAppAdapter().checkVersion(5, DeviceUtils.getVersionCode(mContext));
        call.enqueue(new Callback<ResultDO<Version>>() {
            @Override
            public void onResponse(Call<ResultDO<Version>> call, Response<ResultDO<Version>> response) {
                ResultDO<Version> resultDO=response.body();
                if (resultDO==null){
                    return;
                }
                if (resultDO.getCode()==0&&resultDO.getResult()!=null){
                    Version version=resultDO.getResult();
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
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Version>> call, Throwable t) {

            }
        });
    }

}