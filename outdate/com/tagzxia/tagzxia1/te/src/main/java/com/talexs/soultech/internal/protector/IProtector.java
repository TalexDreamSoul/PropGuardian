package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.internal.protector;

import com.talexs.talexessential.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public interface IProtector {

    boolean shouldLoad();

    boolean checkProtect(PlayerData playerData, PlayerEvent event);

    boolean checkBuild(Player player, Location loc);

    boolean checkBreak(Player player, Location loc);

}
