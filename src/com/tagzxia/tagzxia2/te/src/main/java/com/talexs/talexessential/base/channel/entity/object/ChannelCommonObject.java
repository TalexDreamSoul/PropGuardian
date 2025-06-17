package com.tagzxia2.te.src.main.java.com.talexs.talexessential.base.channel.entity.object;

import com.google.common.io.ByteArrayDataOutput;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.object.ChannelObject;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.object.ChannelProtocol;

/**
 * Define a channel object to transform data for common situation.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:48
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class ChannelCommonObject extends ChannelObject {
    public ChannelCommonObject(ChannelProtocol channelProtocol, String[] args) {
        super(channelProtocol);

        this.args = args;
    }

    private final String[] args;

    @Override
    public void _buildData(ByteArrayDataOutput out) {
        for (String arg : args) {
            out.writeUTF(arg);
        }
    }
}
