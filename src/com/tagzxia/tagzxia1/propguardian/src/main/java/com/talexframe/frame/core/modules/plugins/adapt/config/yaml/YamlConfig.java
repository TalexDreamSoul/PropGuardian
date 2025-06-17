package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.yaml;

import com.talexframe.frame.core.talex.FrameConfig;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * {@link com.talexframe.frame.core.modules.plugins.adapt.config Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:31 Project: TalexFrame
 */
public class YamlConfig extends FrameConfig  {

    private final YamlConfiguration yaml = new YamlConfiguration();

    public YamlConfig(String provider, String path) {

        super(provider, path);
    }

    @Override
    public String serialize() {

        return yaml.saveToString();
    }

    @SneakyThrows
    @Override
    public FrameConfigWrapper<?> deserialize(String data) {

        yaml.loadFromString(data);

        return new FrameConfigWrapper<>(this);
    }

}
