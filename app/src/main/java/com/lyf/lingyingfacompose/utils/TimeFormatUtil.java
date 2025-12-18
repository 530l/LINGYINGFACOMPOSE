package com.lyf.lingyingfacompose.utils;

import android.content.Context;
import android.text.TextUtils;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式化工具类
 */
public class TimeFormatUtil {


    public static String formatTimestampToYearMonthDay2(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        Date date = new Date(timestamp);
        return formatter.format(date);
    }


    /**
     * 将时间戳（毫秒）格式化为 "yyyy.MM.dd" 格式
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的日期字符串，例如 "2025.02.06"
     */
    public static String formatTimestampToYearMonthDay(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        Date date = new Date(timestamp);
        return formatter.format(date);
    }

    /**
     * 将时间戳（毫秒）格式化为 "yyyy.MM.dd" 格式
     *
     * @param timestamp 时间戳（毫秒）
     * @return 格式化后的日期字符串，例如 "2025.02.06"
     */
    public static String formatTimestampToMonthDayHourMinute(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        Date date = new Date(timestamp);
        return formatter.format(date);
    }

    public static String formatTimestampToMonthDay(long timestamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date date = new Date(timestamp);
        return formatter.format(date);
    }



    private static String formatSeconds(int seconds, String secondsUnit, String minutesUnit) {
        // 处理负数和零值
        if (seconds <= 0) {
            return "0" + secondsUnit;
        }

        // 60秒以下直接返回秒数
        if (seconds < 60) {
            return seconds + secondsUnit;
        }

        // 四舍五入计算分钟 (加30秒实现四舍五入)
        long minutes = (seconds + 30) / 60;

        return minutes + minutesUnit;

    }

    public static String formatSecondsToMinSec(int totalSeconds) {
        if (totalSeconds < 0) {
            throw new IllegalArgumentException("总秒数不能为负数");
        }

        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes).append("min");
        }
        if (seconds > 0 || totalSeconds == 0) { // 处理 0 秒的情况
            sb.append(seconds).append("s");
        }

        return sb.toString();
    }


    // 定义输入的日期时间格式
    private static final SimpleDateFormat INPUT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    // 定义只输出时间的格式 (HH:mm)
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm", Locale.getDefault());

    // 定义只输出日期的格式 (MM-dd)
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM-dd", Locale.getDefault());

    // 定义完整年月日格式 (用于往年)
    private static final SimpleDateFormat FULL_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // 定义输出的 "时:分" 格式
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());


    public static String formatTimestampToYMDHMS(long timestamp) {
        Date date = new Date(timestamp);
        return INPUT_FORMATTER.format(date);
    }

    /**
     * 根据产品需求格式化时间
     * <p>
     * 规则：
     * 1. 今天 < 5分钟          -> 刚刚
     * 2. 今天 [5, 60) 分钟     -> N分钟前
     * 3. 今天 >= 1小时         -> N小时前
     * 4. 昨天                 -> 昨天HH:mm
     * 5. 前天                 -> 前天HH:mm
     * 6. 3天~同年              -> MM-dd
     * 7. 跨年                 -> yyyy-MM-dd
     *
     * @param timestampString 输入的时间戳字符串 (yyyy-MM-dd HH:mm:ss)
     * @return 格式化后的字符串
     */
    public static String formatTimestamp(String timestampString) {
        // 1. 处理空值
        if (TextUtils.isEmpty(timestampString)) {
            return "";
        }

        try {
            Date inputDate = INPUT_FORMATTER.parse(timestampString);
            if (inputDate == null) {
                return timestampString;
            }
            Calendar currentCalendar = Calendar.getInstance();
            Calendar targetCalendar = Calendar.getInstance();
            targetCalendar.setTime(inputDate);

            long diffInMillis = currentCalendar.getTimeInMillis() - targetCalendar.getTimeInMillis();
            if (diffInMillis < 0) {
                diffInMillis = 0;
            }

            int dayDiff = getDayDifference(currentCalendar, targetCalendar);
            if (dayDiff == 0) {
                long diffMinutes = diffInMillis / (60 * 1000); // 转为分钟
                long diffHours = diffInMillis / (60 * 60 * 1000); // 转为小时

                if (diffMinutes < 5) {
                    // < 5分钟
                    return "刚刚";
                } else if (diffMinutes < 60) {
                    // [5, 60) 分钟
                    return diffMinutes + "分钟前";
                } else {
                    // >= 1小时 (注：逻辑上同一天内，最多也就显示到23小时前)
                    return diffHours + "小时前";
                }
            }

            if (dayDiff == 1) {
                return "昨天" + TIME_FORMATTER.format(inputDate);
            }
            if (dayDiff == 2) {
                return "前天" + TIME_FORMATTER.format(inputDate);
            }
            // 判断是否同一年
            boolean isSameYear = currentCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR);
            if (isSameYear) {
                // 3天~同年 -> MM-dd
                return DATE_FORMATTER.format(inputDate);
            } else {
                // 跨年 -> yyyy-MM-dd
                return FULL_DATE_FORMATTER.format(inputDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return timestampString;
        }
    }

    /**
     * 计算两个日期相差的自然天数（忽略时分秒）
     * 比如：今天 00:01 和 昨天 23:59，差 1 天
     *
     * @param now    当前时间
     * @param target 目标时间
     * @return 天数差 (0=同一天, 1=昨天, 2=前天, etc.)
     */
    private static int getDayDifference(Calendar now, Calendar target) {
        // 创建副本以免修改原 Calendar 对象
        Calendar c1 = (Calendar) now.clone();
        Calendar c2 = (Calendar) target.clone();

        // 将时分秒毫秒全部置零，只比较日期
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);

        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        // 计算差值毫秒数
        long diffMs = c1.getTimeInMillis() - c2.getTimeInMillis();
        // 转换为天数
        return (int) (diffMs / (24 * 60 * 60 * 1000));
    }


    /**
     * 比较两个 Calendar 对象是否代表同一天（忽略时分秒）
     *
     * @param cal1 第一个 Calendar
     * @param cal2 第二个 Calendar
     * @return 如果是同一天则返回 true
     */
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 将 "yyyy-MM-dd HH:mm:ss" 格式的字符串转换为 "MM-dd"
     *
     * @param dateTimeString 完整的日期时间字符串
     * @return 格式化后的日期字符串，如果输入无效则返回空字符串
     */
    public static String formatDate(String dateTimeString) {
        if (TextUtils.isEmpty(dateTimeString)) {
            return ""; // 处理 null 或空字符串
        }
        try {
            Date date = INPUT_FORMATTER.parse(dateTimeString);
            if (date != null) {
                return DATE_FORMATTER.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ""; // 解析失败也返回空字符串
    }

    /**
     * 将 "yyyy-MM-dd HH:mm:ss" 格式的字符串转换为 "HH:mm"
     *
     * @param dateTimeString 完整的日期时间字符串
     * @return 格式化后的时间字符串，如果输入无效则返回空字符串
     */
    public static String formatTime(String dateTimeString) {
        if (TextUtils.isEmpty(dateTimeString)) {
            return "";
        }
        try {
            Date date = INPUT_FORMATTER.parse(dateTimeString);
            if (date != null) {
                return timeFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据秒数格式化时长。
     *
     * @param durationInSeconds Double 类型的总秒数，例如 169.16
     * @return 格式化后的字符串，例如 "02:49", "59", "01:00"
     */
    public static String formatDuration(double durationInSeconds) {
        // 首先，将 double 类型的秒数转换为整数，忽略小数部分
        int totalSeconds = (int) durationInSeconds;

        // 判断总秒数是否小于一分钟
        if (totalSeconds < 60) {
            // 如果不足1分钟，就直接显示秒数
            return String.valueOf(totalSeconds);
        } else {
            // 如果等于或超过1分钟
            int minutes = totalSeconds / 60; // 计算分钟
            int seconds = totalSeconds % 60; // 计算余下的秒数

            // 使用 String.format 来格式化字符串
            // %02d 的意思是：一个整数(d)，显示为两位(2)，如果不足两位，前面补零(0)
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    public static String formatSecTime(long sec) {
        long minutes = sec / 60;
        long seconds = sec % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public static String formatSecTime(double sec) {
        double minutes = sec / 60;
        double seconds = sec % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    /**
     * 将秒数格式化为 "mm:ss" 标准格式
     * 例如：31秒 -> "00:31", 120秒 -> "02:00"
     * 适用于播放器进度条、倒计时等场景
     *
     * @param seconds 总秒数
     * @return 格式化后的字符串
     */
    public static String formatSecondsToMmSs(long seconds) {
        if (seconds < 0) {
            seconds = 0;
        }
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        // %02d: 整数，不足两位补零
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * 重载方法：支持 double 类型的秒数 (自动向下取整)
     *
     * @param seconds 总秒数 (例如 120.5)
     * @return "mm:ss"
     */
    public static String formatSecondsToMmSs(double seconds) {
        return formatSecondsToMmSs((long) seconds);
    }

    /**
     * 将秒数格式化为 "x分x秒" 格式
     * 例如：90 -> "1分30秒", 30 -> "0分30秒", 3605 -> "60分5秒"
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的字符串
     */
    public static String formatSecondsToChinese(long totalSeconds) {
        if (totalSeconds < 0) {
            totalSeconds = 0;
        }
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (seconds > 0) {
            return minutes + "分" + seconds + "秒";
        } else {
            // 秒数为0，只显示分
            return minutes + "  分钟";
        }
    }

    /**
     * 重载方法：支持 double 类型 (自动向下取整)
     *
     * @param totalSeconds 总秒数
     * @return "x分x秒"
     */
    public static String formatSecondsToChinese(double totalSeconds) {
        return formatSecondsToChinese((long) totalSeconds);
    }
}