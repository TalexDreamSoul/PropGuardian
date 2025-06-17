package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.addon.IRealmAddon;
import com.talexs.talexessential.modules.realm.addon.in.*;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define a manager that controls all addons.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/23 下午 10:10
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmAddons {

    public static RealmAddons INS = new RealmAddons();

    private Map<String, RealmFlag> addonMap = new HashMap<>();

    private RealmAddons() {

        regAddon(new RealmFlagVisible());
        regAddon(new RealmFlagPVP());
        regAddon(new RealmFlagMob());
        regAddon(new RealmFlagExplode());
        regAddon(new RealmFlagFlow());
        regAddon(new RealmFlagAnimal());
        regAddon(new RealmFlagSign());
        regAddon(new RealmFlagContainer());
        regAddon(new RealmFlagUse());

    }

    public void regAddon(@NotNull RealmFlag addon) {
        addonMap.put(addon.getKey(), addon);
    }

    public void unRegAddon(@NotNull RealmFlag addon) {
        addonMap.remove(addon.getKey());
    }

    public <T extends com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.addon.IRealmAddon> T getAddon(String key) {
        IRealmAddon addon = addonMap.get(key);

        if ( addon == null )
            throw new NullPointerException("Addon " + key + " not found!");

        return (T) addon;
    }

    public List<RealmFlag> getAddons() {
        return new ArrayList<>(addonMap.values());
    }

}
