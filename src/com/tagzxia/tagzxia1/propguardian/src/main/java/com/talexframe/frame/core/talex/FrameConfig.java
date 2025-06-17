package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.talex;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.talex.FrameCreator;
import lombok.Getter;

import java.io.Serializable;

/**
 * {@link com.talexframe.frame.core.talex Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 07:55 Project: TalexFrame
 */
public abstract class FrameConfig extends FrameCreator implements Serializable {

    private final String path;

    public FrameConfig(String provider, String path) {

        super("FRAME_CONFIG", provider);

        this.path = path;

    }

    /**
     *
     * @return if return true, then this config file must be loaded
     */
    public boolean isRequired() { return true; }

    /**
     * @return the data to serialize
     */
    public abstract String serialize();

    /**
     * @param data the data to deserialize
     * @return
     */
    public abstract FrameConfigWrapper<?> deserialize(String data);

    public String getPath() {

        return path;
    }

    public static class FrameConfigWrapper<T extends FrameConfig> {

        @Getter
        private final T config;

        public FrameConfigWrapper(T config) {

            this.config = config;

        }

    }

}
