//package com.click.cn.util;
//
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.text.format.Time;
//import android.util.Log;
//
//
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;
//
///**
// * Created by vito-xa49 on 2017/8/25.
// */
//
//public class TimeUtil {
//
//    private TimeUtil() {
//    }
//
//    public static String TimeStamp2Date(String timestampString, String formats) { // yyyy-MM-dd HH:mm:ss
//        Long timestamp = Long.parseLong(timestampString) * 1000;
//        return TimeStamp2Date(timestamp, formats);
//    }
//
//    /**
//     * unix时间戳转换为指定格式
//     *
//     * @param timestamp
//     * @param formats
//     * @return
//     */
//    public static String TimeStamp2Date(long timestamp, String formats) {
//        String date = new SimpleDateFormat(formats).format(new Date(timestamp * 1000));
//        return date;
//    }
//
//    /**
//     * 将unix时间戳转换为
//     * <p>
//     * 格式： yyyy-MM-dd HH:mm:ss
//     *
//     * @param time
//     * @return
//     */
//    public static DateTime unixLong2DateTime(long time) {
//        String year = TimeStamp2Date(time, "yyyy");
//        String month = TimeStamp2Date(time, "MM");
//        String day = TimeStamp2Date(time, "dd");
//        String hour = TimeStamp2Date(time, "HH");
//        String minutes = TimeStamp2Date(time, "mm");
//        String seconds = TimeStamp2Date(time, "ss");
//
//        return new DateTime(Integer.parseInt(year), Integer.parseInt(month),
//                Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minutes),
//                Integer.parseInt(seconds));
//    }
//
//    /**
//     * 获取年-月-日 时:分
//     *
//     * @param dateTime
//     * @return
//     */
//    public static String DateTime2String(DateTime dateTime) {
//        return String.format("%04d", dateTime.getYear())
//                + "-" + String.format("%02d", dateTime.getMonthOfYear())
//                + "-" + String.format("%02d", dateTime.getDayOfMonth())
//                + " "
//                + String.format("%02d", dateTime.getHourOfDay())
//                + ":" + String.format("%02d", dateTime.getMinuteOfHour());
//    }
//
//    /**
//     * 获取年-月-日 时:分:秒
//     *
//     * @param dateTime
//     * @return
//     */
//    public static String DateTime2String2(DateTime dateTime) {
//        return String.format("%04d", dateTime.getYear())
//                + "-" + String.format("%02d", dateTime.getMonthOfYear())
//                + "-" + String.format("%02d", dateTime.getDayOfMonth())
//                + " "
//                + String.format("%02d", dateTime.getHourOfDay())
//                + ":" + String.format("%02d", dateTime.getMinuteOfHour())
//                + ":" + String.format("%02d", dateTime.getSecondOfMinute());
//    }
//
//    /**
//     * 获取 年-月-日
//     *
//     * @param dateTime
//     * @return
//     */
//    public static String dateTime2Str(DateTime dateTime) {
//        StringBuilder stringBuilder = new StringBuilder();
//        return
//                stringBuilder.append(String.format("%04d", dateTime.getYear())).append("-")
//                        .append(String.format("%02d", dateTime.getMonthOfYear())).append("-")
//                        .append(String.format("%02d", dateTime.getDayOfMonth())).toString();
//    }
//
//    public static String getHour(DateTime dateTime) {
//        return String.format("%02d", dateTime.getHourOfDay());
//    }
//
//    public static String getMinute(DateTime dateTime) {
//        return String.format("%02d", dateTime.getMinuteOfHour());
//    }
//
//    public static long dateTime2UnixLong(DateTime dateTime) {
////        try {
////            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-22 15:00:00");
////            println(date.getTime() / 1000+ "");
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//        return dateTime.getMillis() / 1000;
//    }
//
//    public static long dateTime2UnixLong(LocalDate localDate) {
////        try {
////            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-08-22 15:00:00");
////            println(date.getTime() / 1000+ "");
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//        return localDate.toDate().getTime() / 1000;
//    }
//
//
//    /**
//     * 将Unix long形式的时间转为年月日
//     *
//     * @param time
//     * @return
//     */
//    public static String getDateStr(long time) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(time * 1000);
//        Log.i("getDateStr", formatter.format(date));
////        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai")); // ZoneId
//        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
////        for(String zone : TimeZone.getAvailableIDs()){
////
////        }
//        return formatter.format(date);
//    }
//
//    /**
//     * 获取一天中的时间
//     *
//     * @param time
//     * @return
//     */
//    @Deprecated
//    public static String getDayTime(String time) {
//        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:ss");
//        sdr.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        long i = Long.parseLong(time);
//        String times = sdr.format(new Date(i * 1000L));
//        return times;
//    }
//
//    /**
//     * yyyy-MM-dd HH:mm:ss 格式的文本时间表示转化为unix时间戳
//     *
//     * @return
//     */
//    public static long timeStr2UnixLongTime(@NonNull String timeStr) {
////        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        if (TextUtils.isEmpty(timeStr)) {
//            return 0;
//        }
//        try {
//            final String[] split = timeStr.split(" ");
//            if (split.length == 2) {
//                String timeStr01 = split[0];
//                String[] split1 = timeStr01.split("-");
//                if (split1.length == 3) {
//                    int year = Integer.parseInt(split1[0]);
//                    int month = Integer.parseInt(split1[1]);
//                    int day = Integer.parseInt(split1[2]);
//
//                    String[] split2 = split[1].split(":");
//
//                    if (split2.length == 3) {
//                        int hour = Integer.parseInt(split2[0]);
//                        int min = Integer.parseInt(split2[1]);
//                        int second = Integer.parseInt(split2[2]);
//
//                        DateTime dateTime = new DateTime(year, month, day, hour, min, second);
//                        long longTime = dateTime.toDate().getTime() / 1000;
////                    Log.wtf("test", "longtime: " + longTime);
//                        return longTime;
//                    } else {
//                        return 0;
//                    }
//                } else {
//                    return 0;
//                }
//            } else {
//                return 0;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    /**
//     * 获取上午/下午表示时间的字符串
//     *
//     * @param unixTime
//     * @return
//     */
//    public static String getCurrentDayFormat(long unixTime) {
//        String hour = TimeStamp2Date(unixTime, "HH");
//        String minutes = TimeStamp2Date(unixTime, "mm");
//        String seconds = TimeStamp2Date(unixTime, "ss");
//        int hourInt = Integer.parseInt(hour);
//        if (hourInt > 12) {
//            return "下午" + (hourInt - 12) + ":" + minutes;
//        }
//        return "上午" + hourInt + ":" + minutes;
//    }
//
//    public static String getCurrentDayFormat2(long unixTime) {
//        String month = TimeStamp2Date(unixTime, "MM");
//        String day = TimeStamp2Date(unixTime, "dd");
//        return month + "月" + day + "日" + " " + getCurrentDayFormat(unixTime);
//    }
//
//    /**
//     * 是否是同一天
//     *
//     * @param time
//     * @param time2
//     * @return
//     */
//    public static boolean isSameDay(long time, long time2) {
//        if (TimeStamp2Date(time, "yyyy").equals(TimeStamp2Date(time2, "yyyy"))
//                && TimeStamp2Date(time, "MM").equals(TimeStamp2Date(time2, "MM"))
//                && TimeStamp2Date(time, "dd").equals(TimeStamp2Date(time2, "dd"))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static String setConversationTime(long when) {
//        return formatTimeNew(when, false, false);
//    }
//
//    public static String setChatTime(long when) {
//        return formatTimeNew(when, false, true);
//    }
//
//    /**
//     * @param when   需要处理的时间
//     * @param isFull 是否显示全格式 ？
//     * @return
//     */
//    public static String formatTimeNew(long when, boolean isFull, boolean showTime) {
//        if (isFull) {
//            return timeStamp2Date(when, 7);
//        } else {
//            int TodayDiff = getTodayDiff(when);
//            if (showTime) {
//                if (TodayDiff == 0) {
//                    return timeStamp2Date(when, 3);
//                } else if (TodayDiff == 1) {
//                    return "昨天" + " " + timeStamp2Date(when, 3);
//                } else if (TodayDiff == 2) {
//                    return "前天" + " " + timeStamp2Date(when, 3);
//                } else {
//                    return timeStamp2Date(when, 8);
//                }
//            } else {
//                if (TodayDiff == 0) {
//                    return timeStamp2Date(when, 3);
//                } else if (TodayDiff == 1) {
//                    return "昨天";
//                } else if (TodayDiff == 2) {
//                    return "前天";
//                } else {
//                    return timeStamp2Date(when, 9);
//                }
//            }
//        }
//    }
//
//    /**
//     * @return 0为 相等  1为 昨天  依次类推
//     */
//    public static int getTodayDiff(long when) {
//        Time time = new Time();
//        time.set(when);
//
//        int thenYear = time.year;
//        int thenMonth = time.month;
//        int thenMonthDay = time.monthDay;
//
//        time.set(System.currentTimeMillis());
//        if ((thenYear == time.year) && (thenMonth == time.month)) {
//            return time.monthDay - thenMonthDay;
//        }
//        return -1;
//    }
//
//    /**
//     * 时间戳转换成日期格式字符串
//     *
//     * @param seconds 精确到秒的字符串
//     * @param type
//     * @return
//     */
//    public static String timeStamp2Date(long seconds, int type) {
//        String s = seconds + "";
//        if (s.length() < 11)
//            seconds = seconds * 1000;
//        String format;
//        switch (type) {
//            case 1:
//                format = "yyyy-MM-dd\nHH:mm:ss";
//                break;
//            case 2:
//                format = "HH:mm:ss";
//                break;
//            case 3:
//                format = "HH:mm";
//                break;
//            case 4:
//                format = "yyyy/MM/dd";
//                break;
//            case 5:
//                format = "MM-dd HH:mm";
//                break;
//            case 6:
//                format = "mm:ss";
//                break;
//            case 7:
//                format = "yyyy/MM/dd HH:mm:ss";
//                break;
//            case 8:
//                format = "MM/dd HH:mm";
//                break;
//            case 9:
//                format = "MM/dd";
//                break;
//            case 10:
//                format = "MM-dd";
//                break;
//            case 11:
//                format = "yyyyMMddHHmmssS";//毫秒为 3位
//                break;
//            case 12:
//                format = "yyyyMMddHHmmsss";//毫秒为 1位
//                break;
//            default:
//                format = "yyyy-MM-dd HH:mm:ss";
//                break;
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        return sdf.format(new Date(seconds));
//    }
//}
