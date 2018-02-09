package com.zhusx.core.utils;

import android.annotation.SuppressLint;
import android.support.annotation.IntRange;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2017/2/21 10:35
 */
public class _Dates {
    /**
     * 获取当月的 天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DAY_OF_MONTH, 1);// 设置当月第一天
        a.roll(Calendar.DAY_OF_MONTH, -1);// 天数 -1 但是月份不变
        int maxDate = a.get(Calendar.DAY_OF_MONTH);
        return maxDate;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DAY_OF_MONTH, 1);
        a.roll(Calendar.DAY_OF_MONTH, -1);// 天数 -1 但是月份不变
        int maxDate = a.get(Calendar.DAY_OF_MONTH);
        return maxDate;
    }

    /**
     * 字符串转化成日期  y 年 M 月 d 日 H 小时 m 分钟
     *
     * @param dateValue  如 2011-11-11
     * @param dateFormat 如 yyyy-MM-dd
     * @return 日期
     */
    @SuppressLint("SimpleDateFormat")
    public static Date toDate(String dateFormat, String dateValue) {
        try {
            return new SimpleDateFormat(dateFormat).parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(String format, long timestamp) {
        if (timestamp < 10000000000L) {
            timestamp = timestamp * 1000;
        }
        return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(timestamp));
    }

    public static String formatDate(String format, String timestamp) {
        return formatDate(format, Long.parseLong(timestamp));
    }

    public static String formatTime(@IntRange(from = 0, to = 60) int date) {
        return date < 10 ? "0" + date : String.valueOf(date);
    }
}
