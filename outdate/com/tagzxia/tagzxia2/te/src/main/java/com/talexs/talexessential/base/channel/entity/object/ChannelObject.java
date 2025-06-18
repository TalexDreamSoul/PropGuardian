package com.tagzxia2.te.src.main.java.com.talexs.talexessential.base.channel.entity.object;

import com.google.common.io.ByteArrayDataOutput;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.base.channel.entity.object.ChannelProtocol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Define a channel object to build and hand out data.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:42
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Getter
@RequiredArgsConstructor
public abstract class ChannelObject {

    private final ChannelProtocol channelProtocol;

    protected abstract void _buildData(ByteArrayDataOutput out);

    public void buildData(ByteArrayDataOutput out) {
        out.writeUTF(channelProtocol.name());

        _buildData(out);
    }

}
