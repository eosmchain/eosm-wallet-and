package com.token.mangowallet.utils;

import com.blankj.utilcode.util.MapUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class TimeUtils {

    static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    static DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    static DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    static DateFormat defaultFormat2 = new SimpleDateFormat("HH:mm MM/dd", Locale.getDefault());

    public static String getStringTime(String timef) {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        long millis = com.blankj.utilcode.util.TimeUtils.string2Millis(timef, formatter);
        return com.blankj.utilcode.util.TimeUtils.millis2String(millis, defaultFormat);
    }

    public static long getSurplusMillisTime(String timef) {
        formatter2.setTimeZone(TimeZone.getTimeZone("UTC"));
        long millis = com.blankj.utilcode.util.TimeUtils.string2Millis(timef, formatter2);
        String mTime = com.blankj.utilcode.util.TimeUtils.millis2String(millis, defaultFormat);
        long expirationMillis = com.blankj.utilcode.util.TimeUtils.string2Millis(mTime);
        long curMillis = com.blankj.utilcode.util.TimeUtils.getNowMills();
        return expirationMillis - curMillis;
    }

    public static String getStringTime2(String timef) {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        long millis = com.blankj.utilcode.util.TimeUtils.string2Millis(timef, formatter2);
        return com.blankj.utilcode.util.TimeUtils.millis2String(millis, defaultFormat2);
    }

    /**
     * 要转换的毫秒数
     *
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static Map<String, Long> formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        Map<String, Long> timeMap = MapUtils.newHashMap();
        timeMap.put("days", days);
        timeMap.put("hours", hours);
        timeMap.put("minutes", minutes);
        timeMap.put("seconds", seconds);
        return timeMap;
    }
}
