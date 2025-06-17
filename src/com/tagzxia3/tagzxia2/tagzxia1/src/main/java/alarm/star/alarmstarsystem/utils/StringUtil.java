package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.utils;

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

}
