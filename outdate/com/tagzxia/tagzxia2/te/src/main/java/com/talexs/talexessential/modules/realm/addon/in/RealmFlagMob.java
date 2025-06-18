package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Define a realm flag that controls mobs.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 02:09
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagMob extends RealmFlag {
    public RealmFlagMob() {
        super("MOB");
    }

    @Override
    public void onMobSpawn(PlayerRealm pr, EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        if ( !(entity instanceof Monster) ) return;

        event.setCancelled(!pr.getFlag("MOB"));
    }

    @Override
    public void onEntityEnter(PlayerRealm pr, EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();

        if ( !(entity instanceof Monster) ) return;

        if ( !pr.getFlag("MOB") ) {

            drawBarrierMarker(entity.getEyeLocation());
            drawBarrierMarker(entity.getLocation());

            entity.knockback(1.5, .25, .25);

            event.setCancelled(true);

        }

    }
}
