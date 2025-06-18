package me.arasple.mc.trhologram.module.listener

import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.player.PlayerChangedWorldEvent
import taboolib.common.platform.event.SubscribeEvent

/**
 * @author Arasple
 * @date 2021/2/12 13:58
 */
object ListenerWorldChange {

    @SubscribeEvent
    fun onChange(e: PlayerChangedWorldEvent) {
        Hologram.destroyAll(e.player)
        Hologram.refreshAll(e.player)
    }

}