package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.internal;

import org.bukkit.NamespacedKey;

public class StNameSpace {

    public StNameSpace() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static final String PLUGIN_NAME = "talex";

    public static final NamespacedKey OWNER = new NamespacedKey(PLUGIN_NAME, "owner");
    public static final NamespacedKey GUIDE_NOW = new NamespacedKey(PLUGIN_NAME, "guide_now");

    public static final NamespacedKey GUIDE_HISTORY = new NamespacedKey(PLUGIN_NAME, "guide_history");

    public static final NamespacedKey GUIDE_AUTO_BUILD = new NamespacedKey(PLUGIN_NAME, "guide_auto_build");

    public static final NamespacedKey LIQUID_TANK_MAX_STORAGE = new NamespacedKey(PLUGIN_NAME, "liquid_tank_max_storage");
    public static final NamespacedKey LIQUID_TANK_MODE = new NamespacedKey(PLUGIN_NAME, "liquid_tank_mode");
    public static final NamespacedKey LIQUID_TANK_STORAGED = new NamespacedKey(PLUGIN_NAME, "liquid_tank_storaged");
    public static final NamespacedKey LIQUID_TANK_TYPE = new NamespacedKey(PLUGIN_NAME, "liquid_tank_type");

    public static final NamespacedKey VANITY_ITEM = new NamespacedKey(PLUGIN_NAME, "vanity_item");

    public static final NamespacedKey VANITY_AMO = new NamespacedKey(PLUGIN_NAME, "vanity_amo");

    public static final NamespacedKey VANITY_AMO_MAX = new NamespacedKey(PLUGIN_NAME, "vanity_amo_max");
}
