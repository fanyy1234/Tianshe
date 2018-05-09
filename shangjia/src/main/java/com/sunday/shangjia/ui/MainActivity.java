package com.sunday.shangjia.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.Constants;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.ViewPager;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.event.ExitApp;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.shangjia.R;
import com.sunday.shangjia.entity.Version;
import com.sunday.shangjia.http.AppClient;
import com.sunday.shangjia.ui.fragment.ManageFragment;
import com.sunday.shangjia.ui.fragment.OrderBaseFragment;
import com.sunday.shangjia.ui.fragment.ProductManageFragment;
import com.sunday.shangjia.ui.fragment.ProfitManageFragment;
import com.sunday.shangjia.ui.login.LoginActivity;
import com.umeng.update.UpdateDialogActivity;
import com.umeng.update.UpdateResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends BaseActivity {
    @Bind({R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4})
    List<TextView> tabViews;
    @Bind(R.id.content)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;


    private boolean isGotoProduct=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        PushManager.getInstance().initialize(this.getApplicationContext());
        PackageManager pkgManager = getPackageManager();
        //读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(WRITE_EXTERNAL_STORAGE, getPackageName()) == PERMISSION_GRANTED;

        //read phone state用于获取 imei 设备信息
        boolean phoneStatePermission =
                pkgManager.checkPermission(READ_PHONE_STATE, getPackageName()) == PERMISSION_GRANTED;

        //获取位置权限
        boolean locationWifiPermission=pkgManager.checkPermission(ACCESS_COARSE_LOCATION ,getPackageName())==PERMISSION_GRANTED;
        boolean locationGPSPermission=pkgManager.checkPermission(ACCESS_FINE_LOCATION ,getPackageName())==PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneStatePermission||!locationWifiPermission||!locationGPSPermission) {
            ActivityCompat.requestPermissions(this, new String[]{
                    WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE,ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION
            }, 0);
        } else {
            // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
//            PushManager.getInstance().initialize(this.getApplicationContext());
        }
        fragments=new ArrayList<>(4);
        fragments.add(new OrderBaseFragment());
        fragments.add(new ProductManageFragment());
        fragments.add(new ProfitManageFragment());
        fragments.add(new ManageFragment());
        tabViews.get(0).setSelected(true);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(4);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<tabViews.size();i++){
                    tabViews.get(i).setSelected(false);
                }
                tabViews.get(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        isGotoProduct=getIntent().getBooleanExtra("gotoProduct",false);
        if (isGotoProduct){
            viewPager.setCurrentItem(1);
        }

        checkUpdate();


    }

    private void checkUpdate(){
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

    @OnClick(R.id.tab1)
    void order(){
        viewPager.setCurrentItem(0,false);
    }

    @OnClick(R.id.tab2)
    void product(){
        viewPager.setCurrentItem(1,false);
    }

    @OnClick(R.id.tab3)
    void shoppping(){
        viewPager.setCurrentItem(2,false);
    }

    @OnClick(R.id.tab4)
    void profit(){
        viewPager.setCurrentItem(3,false);
    }


    public void setPageTwo(){
        viewPager.setCurrentItem(1,false);
    }


    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else{
                EventBus.getDefault().post(new ExitApp());
                System.exit(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
//                    PushManager.getInstance().initialize(this.getApplicationContext());

                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
