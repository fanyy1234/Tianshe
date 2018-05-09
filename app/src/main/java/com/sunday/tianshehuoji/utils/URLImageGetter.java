package com.sunday.tianshehuoji.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fanyy on 2018/4/9.
 */

public class URLImageGetter implements Html.ImageGetter {
    Context context;
    TextView textView;

    public URLImageGetter(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }
    @Override
    public Drawable getDrawable(String paramString) {
         final URLDrawable urlDrawable = new URLDrawable(context);
         ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
         getterTask.execute(paramString);
         return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
         URLDrawable urlDrawable;

         public ImageGetterAsyncTask(URLDrawable drawable) {
             this.urlDrawable = drawable;
         }

         @Override
         protected void onPostExecute(Drawable result) {
             if (result != null) {
                  urlDrawable.drawable = result;
                  URLImageGetter.this.textView.requestLayout();
             }
         }

         @Override
         protected Drawable doInBackground(String... params) {
             String source = params[0];
             return fetchDrawable(source);
         }

         public Drawable fetchDrawable(String url) {
             try {
                 InputStream is = fetch(url);
                 Rect bounds = SystemInfoUtils.getDefaultImageBounds(context);
                 Bitmap bitmapOrg = BitmapFactory.decodeStream(is);
                 Bitmap bitmap = Bitmap.createScaledBitmap(bitmapOrg, bounds.right, bounds.bottom, true);

                 BitmapDrawable drawable = new BitmapDrawable(bitmap);
                 drawable.setBounds(bounds);

                 return drawable;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 return null;
             }
    }
    private static InputStream fetch(String path){
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            //GET请求直接在链接后面拼上请求参数
            String mPath = path ;
            URL url = new URL(mPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //Get请求不需要DoOutPut
            conn.setDoOutput(false);
            conn.setDoInput(true);
            //设置连接超时时间和读取超时时间
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //连接服务器
            conn.connect();
            // 取得输入流，并使用Reader读取
            return conn.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //关闭输入流
        finally{
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }
}
