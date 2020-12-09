package com.token.mangowallet.utils;

import java.text.DecimalFormat;

public class ConversionUnits {


    /**
     * 内存单位换算
     * 将字节 byte 转为对应的合适的单位
     *
     * @return
     */
    public static String getRAMConversionUnit(long size) {
        //定义GB/MB/KB的计算常量
        double GB = 1024.0 * 1024.0 * 1024.0;
        double MB = 1024.0 * 1024.0;
        double KB = 1024.0;
        StringBuffer bytes = new StringBuffer();
        DecimalFormat df = new DecimalFormat("###.00");
        if (size >= GB) {
            double i = (size / GB);
            bytes.append(df.format(i)).append("GB");
        } else if (size >= MB) {
            double i = (size / MB);
            bytes.append(df.format(i)).append("MB");
        } else if (size >= KB) {
            double i = (size / KB);
            bytes.append(df.format(i)).append("KB");
        } else {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 时间单位换算（CPU）
     * 将μs(微秒) 转为对应的合适的单位
     *
     * @return
     */
    public static String getCPUConversionUnit(long size) {
        //定义s/ms的计算常量
        double s = 1000.0 * 1000.0;
        double ms = 1000.0;
        double m = 1000.0 * 1000.0 * 60.0;
        double hr = 1000.0 * 1000.0 * 60.0 * 60.0;
        //μs
        StringBuffer bytes = new StringBuffer();
        DecimalFormat df = new DecimalFormat("###.00");
        if (size >= hr) {
            double i = (size / hr);
            bytes.append(df.format(i)).append("hr");
        } else if (size >= m) {
            double i = (size / m);
            bytes.append(df.format(i)).append("m");
        } else if (size >= s) {
            double i = (size / s);
            bytes.append(df.format(i)).append("s");
        } else if (size >= ms) {
            double i = (size / ms);
            bytes.append(df.format(i)).append("ms");
        } else {
            if (size <= 0) {
                bytes.append("0μs");
            } else {
                bytes.append((int) size).append("μs");
            }
        }
        return bytes.toString();
    }
}
