package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;

/**
 * Define a realm flag that controls animal actions.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 02:50
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagAnimal extends RealmFlag {
    public RealmFlagAnimal() {
        super("ANIMAL");
    }

    @Override
    public void onEntityLeave(PlayerRealm pr, EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();
        if ( !(entity instanceof Animals) ) return;

        if ( !pr.getFlag("ANIMAL") )
            event.setCancelled(true);
    }
}
