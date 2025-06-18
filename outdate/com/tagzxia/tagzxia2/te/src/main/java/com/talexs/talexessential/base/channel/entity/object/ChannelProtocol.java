package com.tagzxia2.te.src.main.java.com.talexs.talexessential.base.channel.entity.object;

/**
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/26 下午 07:48
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public enum ChannelProtocol {
    Connect(),
    ConnectOther(),
    IP(),
    PlayerCount(),
    PlayerList(),
    GetServers(),
    Message(),
    GetServer(),
    Forward(),
    ForwardToPlayer(),
    UUID(),
    UUIDOther(),
    ServerIP(),
    KickPlayer();
}
