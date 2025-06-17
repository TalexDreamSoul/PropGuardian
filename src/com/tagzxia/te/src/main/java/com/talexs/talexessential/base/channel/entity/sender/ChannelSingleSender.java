package com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender;

import com.google.common.io.ByteArrayDataOutput;
import com.talexs.talexessential.TalexEssential;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

/**
 * Define a single channel sender.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:37
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@RequiredArgsConstructor
public class ChannelSingleSender extends ChannelSender {

    private final Player player;

    @Override
    public void sendMessage(ByteArrayDataOutput out) {
        player.sendPluginMessage(TalexEssential.getInstance(), "BungeeCord", out.toByteArray());
    }
}
