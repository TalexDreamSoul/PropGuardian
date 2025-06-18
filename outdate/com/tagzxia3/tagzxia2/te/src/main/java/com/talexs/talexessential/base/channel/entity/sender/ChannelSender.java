package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender.ChannelMultipleSender;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.sender.ChannelSingleSender;
import com.talexs.talexessential.base.channel.entity.object.ChannelObject;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Define a channel sender which is a wrapper.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:36
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public abstract class ChannelSender {

    public abstract void sendMessage(ByteArrayDataOutput out);

    public void sendMessage(ChannelObject co) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        co.buildData(out);

        sendMessage(out);
    }

    public static ChannelSender random() {
        return new ChannelSingleSender(Iterables.getFirst(Bukkit.getOnlinePlayers(), null));
    }

    public static ChannelSender fromPlayer(Player player) {
        return new ChannelSingleSender(player);
    }

    public static ChannelSender fromOpd(OnlinePlayerData opd) {
        return fromPlayer(opd.getPlayer());
    }

    public static ChannelSender forAll() {
        return new ChannelMultipleSender(Bukkit.getOnlinePlayers());
    }

}
