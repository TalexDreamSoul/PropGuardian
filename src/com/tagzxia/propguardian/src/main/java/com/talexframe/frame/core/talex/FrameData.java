package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.talex;

import cn.hutool.json.JSONObject;

/**
 * <br /> {@link com.talexframe.frame.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:06 <br /> Project: TalexFrame <br />
 */
public abstract class FrameData extends FrameCreator {

    public FrameData(String provider) {

        super("DATA", provider);
    }

    public abstract JSONObject toJSONObject();

}
