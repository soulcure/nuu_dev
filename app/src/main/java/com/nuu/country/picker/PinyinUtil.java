package com.nuu.country.picker;

public class PinyinUtil {
    public static String getPingYin(String inputString) {
        try {
            //return Pinyin.toPinyin(inputString, "");
            return inputString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
