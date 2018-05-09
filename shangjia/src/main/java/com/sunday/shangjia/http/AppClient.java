package com.sunday.shangjia.http;

import android.util.Log;

import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.EncryptUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.member.converter.GsonConverterFactory;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import com.sunday.shangjia.ShangjiaApplication;

/**
 * Created by 刘涛 on 2016/12/13.
 */

public class AppClient {

    private static Retrofit imAdapter;
    private static AppService appService;
    private static String API_URL = "http://day-mobile2.tiansheguoji.com/";

    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient client;

    public static AppService getAppAdapter() {
        if (imAdapter == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder()
                    .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new SignInterceptor())
                    .build();
            imAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        appService = imAdapter.create(AppService.class);
        return appService;
    }

    private static String sign(String nonce, String timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append("wBH4YJUYIa6a").append(nonce).append(timestamp);
        return EncryptUtils.sha1(sb.toString());
    }

    private static class SignInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }
            String nonce = StringUtils.getRandomString(8);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            Request compressedRequest = null;
            Log.i("request", originalRequest.url().encodedPath());
            HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                    .addQueryParameter("platform", "android")
                    .addQueryParameter("version", DeviceUtils.getVersion(ShangjiaApplication.getInstance().getApplicationContext()))
                    .addQueryParameter("model", android.os.Build.MODEL)
                    .addQueryParameter("os_version", android.os.Build.VERSION.RELEASE)
                    .build();
            compressedRequest = originalRequest.newBuilder().url(modifiedUrl)
                    .header("App-Key", "uwd1c0sxdoyn1")
                    .header("Nonce", nonce)
                    .header("Timestamp", timestamp)
                    .addHeader("Signature", sign(nonce, timestamp))
                    // .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            Response response = chain.proceed(compressedRequest);
            return response;
        }
    }


}
