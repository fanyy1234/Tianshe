package com.sunday.tianshe.branch.base;

import android.app.Application;

import com.sunday.tianshe.branch.entity.Member;
import com.sunday.tianshe.branch.http.ApiClient;



/**
 * Created by admin on 2016/12/21.
 */

public class BaseApp extends Application implements Thread.UncaughtExceptionHandler{
    private static BaseApp instance;
    public synchronized static BaseApp getInstance(){
        return instance;
    }


    private  Member member;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ApiClient.getApiAdapter();

   /*     //设置Picasso
        final OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        final Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        Picasso.setSingletonInstance(picasso);*/
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
    }
}
