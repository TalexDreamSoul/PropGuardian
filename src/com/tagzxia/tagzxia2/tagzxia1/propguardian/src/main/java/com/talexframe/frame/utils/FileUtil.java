package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.utils;

import java.util.Arrays;

/**
 * <br /> {@link com.talexframe.frame.utils Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/06 下午 12:58 <br /> Project: TalexFrame <br />
 */
public class FileUtil {

    private static final String[] IMAGE_TYPE = new String[] { "bmp", "jpg", "png", "tif", "gif", "pcx", "tga", "exif",
            "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp", "avif", "apng" };

    /**
     * 读取一个文件拓展名并判断文件是否为图片
     */
    public static boolean isImage(String extension) {

        return Arrays.stream(IMAGE_TYPE).anyMatch(s -> s.equalsIgnoreCase(extension));

    }

}
