package com.railwayservice.order;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe
 */
public class DateTime {
    /**
     * 获取当前系统当前日期差
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        return date;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}
