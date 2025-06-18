package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import com.talexs.talexessential.utils.item.ItemBuilder;

/**
 * Define a realm flag that controls teleport and move ability.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/23 下午 11:06
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagVisible extends RealmFlag {
    public RealmFlagVisible() {
        super("VISIBLE");
    }

    @Override
    public boolean onTeleport(PlayerRealm pr, OnlinePlayerData opd) {

        return pr.allowPlayerTeleport(opd.getPlayer());
    }

}
