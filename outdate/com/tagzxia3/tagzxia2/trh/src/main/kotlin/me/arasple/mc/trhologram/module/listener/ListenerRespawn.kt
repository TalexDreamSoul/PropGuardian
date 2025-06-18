package me.arasple.mc.trhologram.module.listener

import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.player.PlayerRespawnEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @date 2021/2/12 13:58
 */
object ListenerRespawn {

    @SubscribeEvent
    fun onRespawn(e: PlayerRespawnEvent) {
        submit(delay = 2, async = true) {
            Hologram.refreshAll(e.player)
        }
    }

}