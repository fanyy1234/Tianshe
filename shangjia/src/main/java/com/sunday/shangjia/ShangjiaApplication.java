package com.sunday.shangjia;

import android.app.Application;

import com.sunday.common.logger.Logger;
import com.sunday.shangjia.entity.Account;

/**
 * Created by 刘涛 on 2016/12/20.
 */

public class ShangjiaApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static ShangjiaApplication instance;
    private Account AdminUser;

    public synchronized static ShangjiaApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.init("Sunday").hideThreadInfo().setMethodCount(3).setMethodOffset(2);
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        if (!BuildConfig.DEBUG) {
            System.exit(0);
        }
    }

    public Account getAdminUser() {
        return AdminUser;
    }

    public void setAdminUser(Account adminUser) {
        AdminUser = adminUser;
    }
}
