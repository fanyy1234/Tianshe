package com.sunday.tianshehuoji;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sunday.common.event.EventBus;
import com.sunday.common.logger.Logger;
import com.sunday.common.utils.Constants;
import com.sunday.member.utils.SharePerferenceUtils;
import com.sunday.tianshehuoji.entity.Account;

/**
 * Created by 刘涛 on 2016/12/12.
 */

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static BaseApplication instance;
    private Account AdminUser;
    private String sellerId;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    public static int screenWidth;//屏幕宽度
    public static int screenHeight;//屏幕高度
    private boolean isConsumed=false;
    private static int payFlag ;//微信支付用途1钱包充值2订单支付
    private static int clearCartFlag=0;//1表示需要清空购物车

    public synchronized static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.init("Sunday").hideThreadInfo().setMethodCount(3).setMethodOffset(2);
        //Thread.setDefaultUncaughtExceptionHandler(this);
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        WindowManager wm = (WindowManager) getBaseContext().getSystemService(
                Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        initLocation();
        mLocationClient.start();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        if (!BuildConfig.DEBUG) {
            System.exit(0);
        }
    }

    public void reLocate(){
        mLocationClient.stop();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
    }

    public static int getPayFlag() {
        return payFlag;
    }

    public static void setPayFlag(int payFlag) {
        BaseApplication.payFlag = payFlag;
    }

    public static int getClearCartFlag() {
        return clearCartFlag;
    }

    public static void setClearCartFlag(int clearCartFlag) {
        BaseApplication.clearCartFlag = clearCartFlag;
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLatitude() != 0) {
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LATITUDE, String.valueOf(location.getLatitude()));
            }
            if (location.getLongitude() != 0) {
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LONGITUDE, String.valueOf(location.getLongitude()));
            }
            if (!TextUtils.isEmpty(location.getAddrStr())) {
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LOCATION_ADDRESS, location.getAddrStr());
            }
            if (!TextUtils.isEmpty(location.getProvince())) {
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LOCATION_PROVINCE, location.getProvince());

            }
            if (!TextUtils.isEmpty(location.getCity())) {
                Log.i("city",String.valueOf(location.getCity()));
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LOCALTION_CITY, String.valueOf(location.getCity()));
                EventBus.getDefault().post(String.valueOf(location.getCity()));
                mLocationClient.stop();
            }
            if (!TextUtils.isEmpty(location.getDistrict())) {
                SharePerferenceUtils.getIns(getApplicationContext()).putString(Constants.LOCALTION_DISTRCT, String.valueOf(location.getDistrict()));
            }
        }
    }


    public Account getAdminUser() {
        return AdminUser;
    }

    public void setAdminUser(Account adminUser) {
        AdminUser = adminUser;
    }


}
