package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.utils;

/**
 * <br /> {@link com.talexframe.frame.utils Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 0:34 <br /> Project: TalexFrame <br />
 */
public class UrlUtil {

    @Deprecated
    public static boolean advancedUrlChecker(String url1, String url2) {

        url1 = formatUrl("/" + url1);

        if ( urlChecker(url1, url2 = formatUrl("/" + url2)) ) return true;

        String[] urls1 = url1.split("/");
        String[] urls2 = url2.split("/");

        int i = 0;
        int atI = -1;

        for ( String url : urls1 ) {

            ++i;
            if ( url.length() < 1 ) {
                continue;
            }

            if ( url.contains("{") && url.contains("}") ) {
                ++atI;
                continue;
            }

            boolean match = false;

            for ( int j = 0; j < i && j <= urls2.length - 1; ++j ) {
                if ( !urls2[j].equalsIgnoreCase(url) ) {
                    continue;
                }
                match = true;
                break;
            }

            if ( match )  continue;

            return false;

        }

        return urls2.length + atI >= urls1.length;

    }

    public static boolean urlChecker(String url1, String url2) {

        url1 = formatUrl(url1);
        url2 = formatUrl(url2);
        return url1.equalsIgnoreCase(url2);
    }

    public static String formatUrl(String url) {

        String result = ("/" + url);

        while( result.contains("\\") ) result = result.replace("\\", "/");
        while( result.contains("//") ) result = result.replace("//", "/");

        return result;
    }

}
