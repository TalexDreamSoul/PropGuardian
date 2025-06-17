package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Define a ream flag that controls explosions in realm.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 02:42
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagExplode extends RealmFlag {
    public RealmFlagExplode() {
        super("EXPLODE");
    }

    @Override
    public void onBlockExploded(PlayerRealm pr, BlockExplodeEvent event, Block block) {
        if ( !pr.getFlag("EXPLODE") ) {
            event.blockList().remove(block);
        }
    }

    @Override
    public void onEntityExploded(PlayerRealm pr, EntityExplodeEvent event, Block block) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.CREEPER) {
            event.blockList().remove(block);
            return;
        }

        if ( !pr.getFlag("EXPLODE") ) {
            event.blockList().remove(block);
        }
    }
}
