package com.tagzxia3.propguardian.src.main.java.org.bukkit.configuration;

import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.ConfigurationOptions;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemoryConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * Various settings for controlling the input and output of a {@link
 * com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemoryConfiguration}
 */
public class MemoryConfigurationOptions extends ConfigurationOptions {
    protected MemoryConfigurationOptions(@NotNull com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemoryConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemoryConfiguration configuration() {
        return (MemoryConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public MemoryConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public MemoryConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }
}
