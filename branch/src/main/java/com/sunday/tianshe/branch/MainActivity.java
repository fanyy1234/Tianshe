package com.sunday.tianshe.branch;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.sunday.common.adapter.MainFragmentPagerAdapter;
import com.sunday.common.event.EventBus;
import com.sunday.common.model.ResultDO;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.widgets.ViewPager;
import com.sunday.member.base.BaseActivity;
import com.sunday.member.event.ExitApp;
import com.sunday.tianshe.branch.base.BaseApp;
import com.sunday.tianshe.branch.entity.Version;
import com.sunday.tianshe.branch.fragment.MemberAuthFragment;
import com.sunday.tianshe.branch.fragment.PersonInfoFragment;
import com.sunday.tianshe.branch.fragment.ProfitStatisticsFragment;
import com.sunday.tianshe.branch.fragment.ShopManageFragment;
import com.sunday.tianshe.branch.http.ApiClient;
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

public class MainActivity extends BaseActivity {
    @Bind({R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4})
    List<TextView> tabViews;
    @Bind(R.id.content)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments=new ArrayList<>(4);
        fragments.add(MemberAuthFragment.newInstance());
        fragments.add(ShopManageFragment.newInstance());
        fragments.add(ProfitStatisticsFragment.newInstance(String.valueOf(BaseApp.getInstance().getMember().sellerId),0));
        fragments.add(PersonInfoFragment.newInstance());
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

        checkUpdate();
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

    private void checkUpdate(){
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
                }
            }

            @Override
            public void onFailure(Call<ResultDO<Version>> call, Throwable t) {

            }
        });
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                EventBus.getDefault().post(new ExitApp());
                System.exit(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
