package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.bukkit.Sound;
import org.bukkit.block.Container;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Define a realm flag that controls whther player could open any containers.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 03:21
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagContainer extends RealmFlag {
    public RealmFlagContainer() {
        super("CONTAINER");
    }

    @Override
    public void onOpenContainer(OnlinePlayerData opd, PlayerRealm pr, Container container, PlayerInteractEvent event) {
        if ( pr.doPlayerHasPer(opd.getPlayer()) ) return;

        if ( !pr.getFlag("CONTAINERIN") ) {
            opd.getUser().errorActionBar("你不能打开容器!").playSound(Sound.BLOCK_NOTE_BLOCK_BIT);
            opd.getPlayer().knockback(5, 2.5, 2.5);
            event.setCancelled(true);
        }
    }
}
