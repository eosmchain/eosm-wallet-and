package com.token.mangowallet.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APPUtils {
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getInsideString(String str, String strStart, String strEnd) {
        if (str.indexOf(strStart) < 0) {
            return "";
        }
        if (str.indexOf(strEnd) < 0) {
            return "";
        }
        return str.substring(str.indexOf(strStart) + strStart.length(), str.indexOf(strEnd));
    }

    /**
     * 将每三个数字加上逗号处理,最多保留两位小数（通常使用金额方面的编辑）示例：9，702.44
     *
     * @param text
     * @return
     */
    public static String dataFormat(String text) {
        DecimalFormat df = null;
        int pointLength = text.length() - text.indexOf(".") - 1;
        if (text.indexOf(".") > 0) {//含有小数
            if (pointLength == 0) {//含有一位小数
                df = new DecimalFormat("###,##0.");
            } else if (pointLength == 1) {//含有两位小数
                df = new DecimalFormat("###,##0.0");
            } else if (pointLength == 4) {//含有两位小数
                df = new DecimalFormat("###,##0.0000");
            } else {//含有两位以上的小数
                df = new DecimalFormat("###,##0.00");
            }
        } else {//只有整数部分
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }
}
