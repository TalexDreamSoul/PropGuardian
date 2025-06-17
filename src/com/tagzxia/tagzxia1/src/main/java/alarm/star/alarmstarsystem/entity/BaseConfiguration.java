package com.tagzxia.tagzxia1.src.main.java.alarm.star.alarmstarsystem.entity;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class BaseConfiguration {

    @Getter
    private final String name;

    @Getter
    private File file;

    @Getter
    private YamlConfiguration yaml;

    @Getter
    private ConfigManifest manifest;

    @Getter
    private boolean autoExport, ignoreManifest;

    public BaseConfiguration(String name) {
        this.name = name;

        // Get config
        this.file = new File(AlarmStarSystem.getInstance().getDataFolder(), name + ".yml");

        this.yaml = new YamlConfiguration();
    }

    public BaseConfiguration autoExport() {
        this.autoExport = true;
        return this;
    }

    public BaseConfiguration autoExport(boolean autoExport) {
        this.autoExport = autoExport;
        return this;
    }

    public BaseConfiguration ignoreManifest() {
        this.ignoreManifest = true;
        return this;
    }

    public BaseConfiguration ignoreManifest(boolean ignoreManifest) {
        this.ignoreManifest = ignoreManifest;
        return this;
    }

    public BaseConfiguration load() {
        if ( !this.file.exists() ) {

            if ( this.autoExport )
                AlarmStarSystem.getInstance().writeByIS(this.name + ".yml");
            else throw new RuntimeException("Config file not found!");

        }

        // Load config
        try {
            this.yaml.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _onLoad();
        return this;
    }

    private void _onLoad() {
        this.manifest = new ConfigManifest(
                yaml.getString("Settings.identifier"),
                yaml.getString("Settings.version"),
                yaml.getString("Settings.author")
        );

        if ( this.ignoreManifest ) return;

        if ( this.name.equalsIgnoreCase(this.manifest.identifier) ) {
            onLoad();
        } else throw new RuntimeException("Config file name must be matched!");

    }

    @Getter
    @AllArgsConstructor
    public static class ConfigManifest {

        private final String identifier, version, author;

    }

    public abstract void onLoad();

    public abstract void save() throws IOException;

}
