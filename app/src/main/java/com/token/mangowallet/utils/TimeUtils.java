package com.token.mangowallet.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

     DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
     DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public String getStringTime(String timef) {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        long millis = com.blankj.utilcode.util.TimeUtils.string2Millis(timef, formatter);
        return com.blankj.utilcode.util.TimeUtils.millis2String(millis, defaultFormat);
    }

}
