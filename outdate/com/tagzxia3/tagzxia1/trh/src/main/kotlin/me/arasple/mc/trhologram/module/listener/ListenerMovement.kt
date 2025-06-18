package me.arasple.mc.trhologram.module.listener

import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.event.player.PlayerMoveEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.Baffle
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2021/2/11 9:58
 */
object ListenerMovement {

    val cd = Baffle.of(1, TimeUnit.SECONDS)

    @SubscribeEvent
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player

        if (!cd.hasNext(player.name)) {
            Hologram.refreshAll(player)
        }
    }

}