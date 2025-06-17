package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Define a realm flag that controls whether player could open empty sign without any permisisons.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 02:52
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagSign extends RealmFlag {
    public RealmFlagSign() {
        super("SIGN");
    }

    @Override
    public void onOpenSign(OnlinePlayerData opd, PlayerRealm pr, SignSide side, PlayerInteractEvent event) {
        if ( pr.doPlayerHasPer(opd.getPlayer()) ) return;

        if ( event.getAction() == Action.LEFT_CLICK_BLOCK ) {
            opd.getUser().errorActionBar("你不能操作告示牌!").playSound(Sound.BLOCK_ANVIL_HIT);
            event.setCancelled(true);
            return;
        }

        if ( !pr.getFlag("SIGNIN") ) {
            opd.getUser().errorActionBar("你不能操作告示牌!").playSound(Sound.BLOCK_ANVIL_HIT);
            event.setCancelled(true);
            return;
        }

        boolean b = side.lines().stream().allMatch(line -> MiniMessage.miniMessage().serialize(line).isEmpty());

        if ( b ) event.setCancelled(true);
    }
}
