package me.arasple.mc.trhologram.module.listener

import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:55
 */
object ListenerJoin {

    @SubscribeEvent
    fun onJoin(e: PlayerJoinEvent) {
        Hologram.refreshAll(e.player)
    }

}