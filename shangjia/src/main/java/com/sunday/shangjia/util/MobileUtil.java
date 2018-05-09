package com.sunday.shangjia.util;


import com.sunday.common.utils.StringUtils;


/**
 * Created by 刘涛 on 2017/1/6.
 */

public class MobileUtil {
    public static String ReplaceStr(String sourceStr) {
        if (StringUtils.isMobileNO(sourceStr)) {
            sourceStr = String.format("%s****%s", sourceStr.substring(0, 3), sourceStr.substring(7, 11));
        }
        return sourceStr;
    }
}
