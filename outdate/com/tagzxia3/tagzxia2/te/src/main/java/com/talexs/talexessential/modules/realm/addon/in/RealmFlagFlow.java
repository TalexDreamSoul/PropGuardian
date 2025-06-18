package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.bukkit.event.block.BlockFromToEvent;

/**
 * Define a realm flag whther allow liquif flow.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 02:44
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagFlow extends RealmFlag {
    public RealmFlagFlow() {
        super("FLOW");
    }

    @Override
    public void onLiquidFlow(PlayerRealm pr, BlockFromToEvent event) {
        if ( !pr.getFlag("FLOW") )
            event.setCancelled(true);
    }
}
