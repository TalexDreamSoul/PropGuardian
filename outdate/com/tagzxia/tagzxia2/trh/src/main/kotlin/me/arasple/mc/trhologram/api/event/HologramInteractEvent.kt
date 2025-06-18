package me.arasple.mc.trhologram.api.event

import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author Arasple
 * @date 2021/2/11 16:34
 */
class HologramInteractEvent(val player: Player, val type: Type, val hologram: Hologram) : BukkitProxyEvent() {

    enum class Type {

        ALL,

        LEFT,

        RIGHT,

        SHIFT_LEFT,

        SHIFT_RIGHT

    }

}