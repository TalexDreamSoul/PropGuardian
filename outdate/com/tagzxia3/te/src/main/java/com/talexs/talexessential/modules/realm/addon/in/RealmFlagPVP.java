package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.realm.addon.in;

import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.RealmSets;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Define a realm flag which is controller of realm damage management.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/25 下午 01:51
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmFlagPVP extends RealmFlag {
    public RealmFlagPVP() {
        super("PVP");
    }

    @Override
    public void onPVP(OnlinePlayerData opd, PlayerRealm pr, EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        // If damager is player attack
        if ( damager instanceof Player ) {
            Player player = (Player) damager;
            if ( !player.hasPermission("te.realm.admin") && !pr.getFlag(RealmSets.PVP) ) {
                event.setCancelled(true);
                new PlayerUser(player).errorActionBar("这个领域未开放战斗!").playSound(Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
            return;
        }

        if ( !(damager instanceof Projectile) ) return;

        Projectile projectile = (Projectile) damager;
        ProjectileSource shooter = projectile.getShooter();
        if ( shooter instanceof Player ) {
            Player player = (Player) shooter;
            if ( !player.hasPermission("te.realm.admin") && !pr.getFlag(RealmSets.PVP) ) {
                event.setCancelled(true);
                new PlayerUser(player).errorActionBar("这个领域未开放战斗!").playSound(Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        }
    }
}
