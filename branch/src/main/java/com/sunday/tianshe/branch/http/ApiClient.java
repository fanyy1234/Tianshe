package com.sunday.tianshe.branch.http;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.sunday.common.logger.Logger;
import com.sunday.common.utils.DeviceUtils;
import com.sunday.common.utils.EncryptUtils;
import com.sunday.common.utils.StringUtils;
import com.sunday.member.converter.GsonConverterFactory;
import com.sunday.member.http.MemberService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/1/26.
 */
public class ApiClient {
    private static Retrofit imAdapter;
    private static ApiService apiService;
    private static MemberService memberService;
   // private static String API_URL = "http://192.168.1.130:8070";
    private static String API_URL = "https://day-mobile.tiansheguoji.com:443/";
   // private static String API_URL = "http://day-mobile2.tiansheguoji.com/";

    private static OkHttpClient client;
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    public static ApiService getApiAdapter() {
        if (imAdapter == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.addInterceptor(loggingInterceptor);
            builder.sslSocketFactory(createSSLSocketFactory());
            builder.build();
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            client = builder.build();
            imAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        apiService = imAdapter.create(ApiService.class);
        return apiService;
    }

    public static MemberService getMemberAdapter(){
        if (imAdapter == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(30, TimeUnit.SECONDS);
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.addInterceptor(loggingInterceptor);
            builder.sslSocketFactory(createSSLSocketFactory());
            builder.build();
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            client = builder.build();
            imAdapter = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        memberService = imAdapter.create(MemberService.class);
        return memberService;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }


    private static String sign(String nonce, String timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append("wBH4YJUYIa6a").append(nonce).append(timestamp);
        return EncryptUtils.sha1(sb.toString());

    }


    static class AddPostParamRequestBody extends RequestBody {

        final RequestBody body;
        StringBuilder encodedParams = new StringBuilder();

        AddPostParamRequestBody(RequestBody body, HashMap<String, String> params) {
            this.body = body;
            try {
                encodedParams.append('&');
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(TextUtils.isEmpty(entry.getValue()) ? "" : entry.getValue(), "UTF-8"));
                    encodedParams.append('&');
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("param", "参数编码异常");
            }
        }

        @Override
        public long contentLength() throws IOException {
            return body.contentLength() + encodedParams.length();
        }

        @Override
        public MediaType contentType() {
            return body.contentType();
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            body.writeTo(bufferedSink);
            bufferedSink.writeString(encodedParams.toString(), Charset.forName("UTF-8"));
        }

    }


}
