package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Define a realm flag that controls player usements.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 05:17
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagUse extends RealmFlag {
    public RealmFlagUse() {
        super("USE");
    }

    @Override
    public void onInteract(OnlinePlayerData opd, PlayerRealm realm, PlayerInteractEvent event) {
        if ( !realm.use(opd.getPlayer()) ) {
            event.setCancelled(true);
            new PlayerUser(opd.getPlayer())
                    .playSound(Sound.BLOCK_ANVIL_HIT, 1, 1)
                    .errorActionBar("在这个领域中你不可以这么做！");
        }
    }
}
