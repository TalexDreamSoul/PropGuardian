package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.entity;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.entity.BaseConfiguration;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ListConfiguration<T> extends BaseConfiguration {

    private static Map<Class<?>, ListConfiguration<?>> INSTANCES = new HashMap<>();

    public static ListConfiguration<?> getInstance(Class<?> cls) {
        return INSTANCES.get(cls);
    }

    @Getter
    private final String rootListPath;

    // The string refers to (key) identifier
    @Getter
    private final Map<String, T> map = new HashMap<>();

    public ListConfiguration(String name, String rootListPath) {
        super(name);

        this.rootListPath = rootListPath;

        INSTANCES.put(getClass(), this);
    }

    @Override
    public void onLoad() {
        onPreLoadEntries();

        YamlConfiguration yaml = getYaml();
        Set<String> keys = yaml.getConfigurationSection(this.rootListPath).getKeys(false);

        for( String key : keys ) {
            T t = load(yaml, this.rootListPath + "." + key, key);

            if ( t == null ) throw new NullPointerException("The value of " + key + " is null");

            map.put(key, t);
        }

    }

    public void onPreLoadEntries() {}

    public T get(String key) {
        return map.get(key);
    }

    public Set<String> getKeys() {
        return map.keySet();
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public void put(String key, T t) {
        map.put(key, t);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public abstract T load(YamlConfiguration yaml, String path, String key);

    public void save(YamlConfiguration yaml, String path, String key, T t) {}

    @Override
    public void save() throws IOException {
        YamlConfiguration yaml = getYaml();

        for( Map.Entry<String, T> entry : map.entrySet() ) {
            if ( entry.getValue() == null ) continue;
            save(yaml, this.rootListPath + "." + entry.getKey(), entry.getKey(), entry.getValue());
        }

        yaml.save(getFile());
    }

    @Override
    public ListConfiguration<T> autoExport() {
        super.autoExport();
        return this;
    }

    @Override
    public ListConfiguration<T> autoExport(boolean autoExport) {
        super.autoExport(autoExport);
        return this;
    }

    @Override
    public ListConfiguration<T> ignoreManifest() {
        super.ignoreManifest();
        return this;
    }

    @Override
    public ListConfiguration<T> ignoreManifest(boolean ignoreManifest) {
        super.ignoreManifest(ignoreManifest);
        return this;
    }

    @Override
    public ListConfiguration<T> load() {
        super.load();
        return this;
    }
}
