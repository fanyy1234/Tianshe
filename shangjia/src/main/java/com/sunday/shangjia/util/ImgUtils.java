package com.sunday.shangjia.util;

import android.util.Log;

/**
 * Created by 刘涛 on 2016/12/23.
 */

public class ImgUtils {
    public static String ReplaceStr(String sourceStr){
        String str1="https://day-mobile.tiansheguoji.com";
        String str2="http://day-mobile2.tiansheguoji.com";

        if (sourceStr.contains(str1)){
            sourceStr = sourceStr.replace(str1,str2);
        }
        Log.d("img",sourceStr);
        return sourceStr;
    }
}
