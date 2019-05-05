package com.nuu.util;

import java.util.Calendar;
import java.util.Locale;

public class BCDUtil {

    public static long bcdTime(long time) {

        byte[] bcd = new byte[8];
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(time);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        bcd[0] = int2Bcd((year / 100 & 0xFF));
        bcd[1] = int2Bcd((year % 100 & 0xFF));
        bcd[2] = int2Bcd((month & 0xFF));
        bcd[3] = int2Bcd((day & 0xFF));
        bcd[4] = int2Bcd((hour & 0xFF));
        bcd[5] = int2Bcd((minute & 0xFF));
        bcd[6] = int2Bcd((second & 0xFF));
        bcd[7] = (byte) (0xFF);
        return HexUtil.getLongByBytes(bcd, false);
    }


    public static Calendar getTimeByBcd(long bcd) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);

        byte[] bcdTime = HexUtil.longToBytes(bcd, false);

        int yearBig = bcdToInt(bcdTime[0]);
        int yearSmall = bcdToInt(bcdTime[1]);
        int year = yearBig * 100 + yearSmall;
        int month = bcdToInt(bcdTime[2]) - 1;
        int day = bcdToInt(bcdTime[3]);
        int hour = bcdToInt(bcdTime[4]);
        int minute = bcdToInt(bcdTime[5]);
        int second = bcdToInt(bcdTime[6]);

        calendar.set(year, month, day, hour, minute, second);


        return calendar;
    }


    /**
     * int 转 bcd码
     *
     * @param num int
     * @return bcd 编码
     */
    private static byte int2Bcd(int num) {
        int mod = num % 10;
        return (byte) ((num / 10) << 4 | mod);
    }


    /**
     * bcd码 转 int
     *
     * @param b byte
     * @return int
     */
    private static int bcdToInt(byte b) {
        return (b >> 4) * 10 + (b & 0x0f);
    }

}
