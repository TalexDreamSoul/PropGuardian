package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules;

import com.talexs.talexessential.TalexEssential;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

public abstract class BaseModule implements Listener {

    protected String name;

    protected File file;

    protected YamlConfiguration yaml = new YamlConfiguration();

    protected TalexEssential plugin = TalexEssential.getInstance();

    public BaseModule(String name) {
        this.name = name;
    }

    public void _internalInit() {
        Bukkit.getPluginManager().registerEvents(this, TalexEssential.getInstance());

        if ( !configurable() ) return;

        this.file = outputs("config.yml");

        TalexEssential.getInstance().log("&7[Modules] &e" + name + " &7loading ...");

        this.yaml = YamlConfiguration.loadConfiguration(this.file);
    }

    public abstract void onEnable();

    public void onDisable() {}
    
    public void onAllModulesEnabled() {}

    protected File outputs(String name) {
        File file = new File(TalexEssential.getInstance().getDataFolder(), this.name + "/" + name);

        if ( !file.exists() ) {
            file.getParentFile().mkdirs();

            if ( shouldOutput() ) {
                TalexEssential.getInstance().writeByIS(this.name + "/" + name);

                onMkdirs();

                TalexEssential.getInstance().log("&7[Modules] &e" + this.name + " " + name + " &7created!");
            }
        }

        return file;

    }

    protected void onMkdirs() {};

    protected boolean shouldOutput() {
        return true;
    }

    protected boolean configurable() { return true; }

}
