package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * {@link # alarm.star.alarmstarsystem.utils }
 *
 * @author TalexDreamSoul
 * <p>
 * Project: AlarmStarSystem
 * <p>
 */
public class StringUtil {

    public static String generateProgressString(double percent, int maxWidth, String fill, String will) {

        int fillAmo = (int) ( percent * maxWidth ) - 1;

        StringBuilder sb = new StringBuilder();

        for ( int i = 0; i < maxWidth; ++i ) {

            sb.append(i <= fillAmo ? fill : will);

        }

        return sb.toString();

    }

    // return random str
    public static String genRandomStr(int len) {

            StringBuilder sb = new StringBuilder();

            for ( int i = 0; i < len; ++i ) {

                sb.append((char) ( Math.random() * 26 + 'a' ));

            }

            return sb.toString();

    }

    public static final String[] CONVERT_NUMBER_MAPPER = new String[] {
            "零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"
    };

    public static String covertNumber2Char(int num) {
        if ( num > 10 ) {
            return covertNumber2Char(num - 10) + CONVERT_NUMBER_MAPPER[num % 10];
        } else return CONVERT_NUMBER_MAPPER[num];
    }

}
