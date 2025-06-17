package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.talexframe.frame.core.talex.FrameConfig;
import lombok.SneakyThrows;

/**
 * {@link com.talexframe.frame.core.modules.plugins.adapt.config Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:59 Project: TalexFrame
 */
public class JSONConfig extends FrameConfig  {

    private JSONObject json = new JSONObject();

    public JSONConfig(String provider, String path) {

        super(provider, path);
    }

    @Override
    public String serialize() {

        return json.toString();
    }

    @SneakyThrows
    @Override
    public FrameConfigWrapper<?> deserialize(String data) {

        this.json = JSONUtil.parseObj(data);

        return new FrameConfigWrapper<>(this);
    }

}
