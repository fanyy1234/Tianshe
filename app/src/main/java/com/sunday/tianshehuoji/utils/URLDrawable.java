package com.sunday.tianshehuoji.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.sunday.tianshehuoji.R;


/**
 * Created by fanyy on 2018/4/9.
 */

public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

              public URLDrawable(Context context) {
                 this.setBounds(SystemInfoUtils.getDefaultImageBounds(context));

                 drawable = context.getResources().getDrawable(R.mipmap.default_img);
                 drawable.setBounds(SystemInfoUtils.getDefaultImageBounds(context));
             }

             @Override
     public void draw(Canvas canvas) {
                 Log.d("test", "this=" + this.getBounds());
                 if (drawable != null) {
                         Log.d("test", "draw=" + drawable.getBounds());
                         drawable.draw(canvas);
                     }
             }
}
