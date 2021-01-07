package com.token.mangowallet.utils;

import com.blankj.utilcode.util.MapUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class TimeUtils {

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public String getStringTime(String timef) {
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        long millis = com.blankj.utilcode.util.TimeUtils.string2Millis(timef, formatter);
        return com.blankj.utilcode.util.TimeUtils.millis2String(millis, defaultFormat);
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
