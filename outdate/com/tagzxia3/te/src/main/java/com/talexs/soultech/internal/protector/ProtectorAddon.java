package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.protector;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.protector.IProtector;
import com.talexs.soultech.addon.BaseAddon;
import com.talexs.talexessential.data.PlayerData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.LinkedList;

/**
 * <p>
 * {@link # com.talexs.soultech.manager }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 16:05
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
@Slf4j
public class ProtectorAddon extends BaseAddon {

    @Getter
    private final LinkedList<com.tagzxia.te.src.main.java.com.talexs.soultech.internal.protector.IProtector> protectors = new LinkedList<>();

    public ProtectorAddon regProtector(com.tagzxia.te.src.main.java.com.talexs.soultech.internal.protector.IProtector protector) {
        if ( protector.shouldLoad() ) {
            protectors.add(protector);
            log.info("[Protector] " + protector.getClass().getSimpleName() + " has been loaded!");
        }
        return this;
    }

    public com.tagzxia.te.src.main.java.com.talexs.soultech.internal.protector.IProtector unRegProtector(IProtector protector) {
        protectors.remove(protector);
        return protector;
    }

    public boolean checkProtect(PlayerData playerData, PlayerEvent playerEvent) {

        if ( playerEvent.getPlayer().hasPermission("talex.soultech.admin") ) {
            return true;
        }

        boolean b = protectors.stream().anyMatch(protector -> !protector.checkProtect(playerData, playerEvent));

        return !b;
    }

    public boolean couldPlace(Player player, Location loc) {
        if ( player.hasPermission("talex.soultech.admin") ) {
            return true;
        }

        BlockPlaceEvent event = new BlockPlaceEvent(loc.getBlock(), loc.getBlock().getState(), loc.getBlock(), player.getInventory().getItemInMainHand(), player, true, null);

        Bukkit.getPluginManager().callEvent(event);

        if ( event.isCancelled() ) return false;

        return protectors.stream().allMatch(protector -> protector.checkBuild(player, loc));
    }

    public boolean couldBreak(Player player, Location loc) {
        if ( player.hasPermission("talex.soultech.admin") ) {
            return true;
        }

        BlockBreakEvent event = new BlockBreakEvent(loc.getBlock(), player);

        Bukkit.getPluginManager().callEvent(event);

        if ( event.isCancelled() ) return false;

        return protectors.stream().allMatch(protector -> protector.checkBreak(player, loc));
    }
}
