package com.tagzxia3.tagzxia2.src.main.java.alarm.star.alarmstarsystem.entity;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class ChatReq {

    public static Map<String, ChatReq> reqList = new HashMap<>();

    private final ReqEnum reqEnum;

    private final Player player;

    public ChatReq(ReqEnum reqEnum, Player player) {
        this.reqEnum = reqEnum;
        this.player = player;
    }

    public abstract void onChatInput(AsyncPlayerChatEvent event);

    public static enum ReqEnum {
        SHOP_PRICE
    }

}
