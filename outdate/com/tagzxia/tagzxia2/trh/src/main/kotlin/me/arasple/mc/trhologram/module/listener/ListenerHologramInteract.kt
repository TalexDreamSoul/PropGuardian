package me.arasple.mc.trhologram.module.listener

import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.api.event.HologramInteractEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit

/**
 * @author Arasple
 * @date 2021/2/11 16:41
 */
object ListenerHologramInteract {

    @SubscribeEvent(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(e: HologramInteractEvent) {
        val player = e.player

        if (Settings.INSTANCE.interactDelay.hasNext(player.name)) {
            submit(delay = 1L, async = false) {
                e.hologram.reactions.eval(player, e.type)
            }
        }
    }

}