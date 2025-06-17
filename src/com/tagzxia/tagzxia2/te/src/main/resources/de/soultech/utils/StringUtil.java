package com.tagzxia2.te.src.main.resources.de.soultech.utils;

/**
 * <p>
 * {@link # pubsher.talexsoultech.utils }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 16:20
 * <p>
 * Project: TalexSoulTech
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

}
