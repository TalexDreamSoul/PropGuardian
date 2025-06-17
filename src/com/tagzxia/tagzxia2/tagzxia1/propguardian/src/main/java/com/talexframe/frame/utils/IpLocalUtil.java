package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.utils;

import cn.hutool.http.HttpUtil;

/**
 * <br /> {@link com.talex.frame.utils Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/25 23:37 <br /> Project: TFunction <br />
 */
public class IpLocalUtil {

    public static String getLocal(String ip) {

        String result = HttpUtil.get("http://pv.sohu.com/cityjson?ie=utf-8/ip=103.97.200.41");

        int ind = result.indexOf("\"cname\": \"") + "\"cname\": \"".length();

        return result.substring(ind, result.indexOf("\"}", ind));

    }

}
