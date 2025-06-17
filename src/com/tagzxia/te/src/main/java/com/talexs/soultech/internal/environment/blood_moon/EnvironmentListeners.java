package com.tagzxia.te.src.main.java.com.talexs.soultech.internal.environment.blood_moon;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.manager.EnvironmentManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.environment.blood_moon }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 19:57
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class EnvironmentListeners implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {

        World world = event.getWorld();

        if ( world.getName().equalsIgnoreCase("world") ) {
            return;
        }

        EnvironmentManager.INSTANCE.tryBloodMoon(world);

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {

        Player player = event.getPlayer();

        if ( player.hasPermission("talex.soultech.admin") ) {
            return;
        }

        PlayerData playerData = PlayerData.g(player);

        if ( playerData.getPlayerSoul().doCategoryUnlock("st_magic") && BloodMoonCreator.start ) {

            Location to = event.getTo();
            if ( to.getWorld().getName().equalsIgnoreCase("world") ) return;

            event.setCancelled(true);

            new PlayerUser(player).title("§4§l℘", "§c血腥异能素 §7阻止了传送!", 5, 12, 10)
                    .playSound(Sound.ENTITY_SPIDER_AMBIENT, 1.0f, 1.1f)
                    .playSound(Sound.ENTITY_CREEPER_HURT, 1.0f, 1.1f)
                    .playSound(Sound.ENTITY_CAT_AMBIENT, 1.0f, 1.1f)
                    .playSound(Sound.ENTITY_CAT_HISS, 1.0f, 1.1f)
                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1.2f)
                    .actionBar("§c§l你现在无法进行传送!")
            ;

        }

    }

}
