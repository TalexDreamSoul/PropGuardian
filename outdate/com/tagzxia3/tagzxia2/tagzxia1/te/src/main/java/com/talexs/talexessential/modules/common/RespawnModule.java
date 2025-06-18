package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.common;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.modules.BaseModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class RespawnModule extends BaseModule {
    public RespawnModule() {
        super("Respawn");
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        PlayerData playerData = PlayerData.g(player);

        if ( player.getLocation().getWorld().getName().equalsIgnoreCase("Spawn") ) return;

        playerData.setDeathLocation(player.getLocation());

        player.sendMessage(
                MiniMessage.miniMessage().deserialize("<gradient:#6580BA:#03719E>您已死亡，点击这里返回死亡地点！")
                        .hoverEvent(HoverEvent.showText(Component.text("- 点击返回死亡地点 -")))
                        .clickEvent(ClickEvent.runCommand("/te back"))
        );
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        EntityDamageEvent.DamageCause cause = event.getCause();
        if ( EntityDamageEvent.DamageCause.STARVATION == cause ) {
            if ( ((Player) entity).getHealth() <= 1 ) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.g(player);

        if ( event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.DISMOUNT || event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            playerData.setDeathLocation(player.getLocation());
        }

    }

}
