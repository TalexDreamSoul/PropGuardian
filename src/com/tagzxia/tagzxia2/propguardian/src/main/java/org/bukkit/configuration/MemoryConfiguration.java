package com.tagzxia2.propguardian.src.main.java.org.bukkit.configuration;

import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.ConfigurationSection;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemoryConfigurationOptions;
import com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.MemorySection;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * This is a {@link com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration {
    protected com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration defaults;
    protected MemoryConfigurationOptions options;

    /**
     * Creates an empty {@link MemoryConfiguration} with no default values.
     */
    public MemoryConfiguration() {
    }

    /**
     * Creates an empty {@link MemoryConfiguration} using the specified {@link
     * com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
    public MemoryConfiguration(@Nullable com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration defaults) {
        this.defaults = defaults;
    }

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        Validate.notNull(path, "Path may not be null");

        if (defaults == null) {
            defaults = new MemoryConfiguration();
        }

        defaults.set(path, value);
    }

    @Override
    public void addDefaults(@NotNull Map<String, Object> defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            addDefault(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addDefaults(@NotNull com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        addDefaults(defaults.getValues(true));
    }

    @Override
    @Nullable
    public com.tagzxia.propguardian.src.main.java.org.bukkit.configuration.Configuration getDefaults() {
        return defaults;
    }

    @Override
    public void setDefaults(@NotNull Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        this.defaults = defaults;
    }

    @Nullable
    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    @Override
    @NotNull
    public MemoryConfigurationOptions options() {
        if (options == null) {
            options = new MemoryConfigurationOptions(this);
        }

        return options;
    }
}
