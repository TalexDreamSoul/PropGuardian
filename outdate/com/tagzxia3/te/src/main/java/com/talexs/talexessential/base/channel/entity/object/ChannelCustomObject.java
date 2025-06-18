package com.tagzxia3.te.src.main.java.com.talexs.talexessential.base.channel.entity.object;

import com.google.common.io.ByteArrayDataOutput;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.object.ChannelObject;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.object.ChannelProtocol;

import java.io.ByteArrayOutputStream;

/**
 * Define a channel object to transform data for custom situation.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:49
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class ChannelCustomObject extends ChannelObject {
    public ChannelCustomObject(ChannelProtocol channelProtocol, ByteArrayOutputStream msgbytes) {
        super(channelProtocol);

        this.msgbytes = msgbytes;
    }

    private final ByteArrayOutputStream msgbytes;

    @Override
    public void _buildData(ByteArrayDataOutput out) {
        out.writeShort(this.msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());
    }
}
