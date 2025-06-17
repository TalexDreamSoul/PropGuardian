package com.tagzxia3.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender;

import com.google.common.io.ByteArrayDataOutput;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender.ChannelSender;
import com.talexs.talexessential.TalexEssential;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Define a multiple channel sender.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:38
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@RequiredArgsConstructor
public class ChannelMultipleSender extends ChannelSender {

    private final Collection<? extends Player> players;

    @Override
    public void sendMessage(ByteArrayDataOutput out) {
        players.forEach(player -> player.sendPluginMessage(TalexEssential.getInstance(), "BungeeCord", out.toByteArray()));
    }
}
