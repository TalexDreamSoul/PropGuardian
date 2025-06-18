package com.tagzxia2.te.src.main.java.com.talexs.soultech.addon;

import com.tagzxia.te.src.main.java.com.talexs.soultech.addon.BaseAddon;
import com.tagzxia.te.src.main.java.com.talexs.soultech.addon.ISoulTechAddon;
import com.talexs.talexessential.TalexEssential;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * Define a addon manager.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/28 上午 04:21
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class AddonHolder {

    private final Map<Class<? extends com.tagzxia.te.src.main.java.com.talexs.soultech.addon.BaseAddon>, ISoulTechAddon> mapper = new HashMap<>();

    @Getter
    private static final AddonHolder INSTANCE = new AddonHolder();

    public void register(com.tagzxia.te.src.main.java.com.talexs.soultech.addon.BaseAddon addon) {
        // if addon implements Listener
        if ( addon instanceof Listener) {
            // register listener
             Bukkit.getPluginManager().registerEvents((Listener) addon, TalexEssential.getInstance());
        }

        mapper.put(addon.getClass()/*.asSubclass(BaseAddon.class)*/, addon);
    }

    public <T extends BaseAddon> T get(Class<T> clazz) {
        ISoulTechAddon iSoulTechAddon = mapper.get(clazz);

//        System.out.println("Getter clz: " + clazz);
//        System.out.println("Target mapper: " + mapper);

        if ( !clazz.isAssignableFrom(iSoulTechAddon.getClass()) || iSoulTechAddon.getClass() != clazz ) {
            throw new RuntimeException("Addon is not assignable from " + clazz.getName());
        }

        return (T) iSoulTechAddon;
    }

    public void onEnable() {
        mapper.values().forEach(ISoulTechAddon::onEnable);
    }

    public void onDisable() {
        mapper.values().forEach(ISoulTechAddon::onDisable);
    }

}
