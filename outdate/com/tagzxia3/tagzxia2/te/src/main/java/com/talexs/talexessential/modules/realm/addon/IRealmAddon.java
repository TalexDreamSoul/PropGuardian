package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm.addon;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Define a realm addon that describe the permission sets.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/23 下午 09:17
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public interface IRealmAddon {

    /**
     * Define the flag could be entered or not.
     * @param pr the player realm data
     * @param event the move event
     */
    void onEntityEnter(PlayerRealm pr, EntityMoveEvent event);

    /**
     * Define the flag could be left or not.
     * @param pr the player realm data
     * @param event the move event
     */
    void onEntityLeave(PlayerRealm pr, EntityMoveEvent event);

    /**
     * Define the flag could be exploded or not.
     * @param pr the player realm
     * @param event the explosion event
     * @param block the block
     */
    void onBlockExploded(PlayerRealm pr, BlockExplodeEvent event, Block block);

    /**
     * Define the flag could be exploded or not.
     * @param pr the player realm
     * @param event the explosion event
     * @param block the block
     */
    void onEntityExploded(PlayerRealm pr, EntityExplodeEvent event, Block block);

    /**
     * Define the flag could be damaged or not.
     * @param opd the online player data
     * @param pr the player realm
     * @param event the damage event
     */
    void onPVP(OnlinePlayerData opd, PlayerRealm pr, EntityDamageByEntityEvent event);

    /**
     * Define the flag could be damaged or not.
     * @param opd the online player data
     * @param pr the player realm
     * @param event the damage event
     */
    void onPVE(OnlinePlayerData opd, PlayerRealm pr, EntityDamageByEntityEvent event);

    /**
     * Define the flag could spawn or not.
     * @param pr the player realm
     * @param event the spawn event
     */
    void onMobSpawn(PlayerRealm pr, EntitySpawnEvent event);

    /**
     * Define the flag could flow or not.
     * @param pr the player realm
     * @param event the flow event
     */
    void onLiquidFlow(PlayerRealm pr, BlockFromToEvent event);

    /**
     * Define the flag could interact or not.
     * @param pr the player realm
     * @param event the interact event
     */
    void onInteract(OnlinePlayerData opd, PlayerRealm pr, PlayerInteractEvent event);

    /**
     * Define the flag could teleport or not.
     * @param pr the player realm
     * @param opd the online player data
     * @return true if anyone could teleport, otherwise false
     */
    boolean onTeleport(PlayerRealm pr, OnlinePlayerData opd);

    /**
     * Define the flag could open sign or not.
     * @param opd
     * @param pr
     * @param side
     * @param event
     */
    void onOpenSign(OnlinePlayerData opd, PlayerRealm pr, SignSide side, PlayerInteractEvent event);

    /**
     * Define the flag could open container or not.
     * @param opd
     * @param pr
     * @param container
     * @param event
     */
    void onOpenContainer(OnlinePlayerData opd, PlayerRealm pr, Container container, PlayerInteractEvent event);

    /**
     * Define the flag is visible or not.
     * @param pr the player realm
     * @param opd the online player data
     * @return true if visible, otherwise false
     */
//    boolean doVisible(PlayerRealm pr, OnlinePlayerData opd);
}
