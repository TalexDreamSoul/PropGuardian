package me.arasple.mc.trhologram.api.nms

import me.arasple.mc.trhologram.api.event.HologramInteractEvent
import me.arasple.mc.trhologram.api.event.HologramInteractEvent.Type.*
import me.arasple.mc.trhologram.module.display.Hologram
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common5.mirrorNow
import taboolib.module.nms.MinecraftVersion
import taboolib.module.nms.PacketReceiveEvent

/**
 * @author Arasple
 * @date 2021/2/10 11:27
 */
object NMSListener {

    @SubscribeEvent
    fun e(e: PacketReceiveEvent) {
        if (e.packet.name == "PacketPlayInUseEntity") {
            mirrorNow("Hologram:Event:Interact") {
                val entityId = e.packet.read<Int>("a").also {
                    if (it == null || it < 1197897763) {
                        return@mirrorNow
                    }
                }
                val hologram =
                    Hologram.findHologram { it -> it.components.any { it.entityId == entityId } } ?: return@mirrorNow

                val sneaking = e.player.isSneaking
                if (MinecraftVersion.isUniversal) {
                    val action = e.packet.read<Any>("action")!!
                    when (action.javaClass.simpleName) {
                        // ATTACK
                        "d" -> if (sneaking) SHIFT_LEFT else LEFT
                        else -> if (sneaking) SHIFT_RIGHT else RIGHT
                    }
                } else {
                    val type = when (e.packet.read<Any>("action").toString()) {
                        "ATTACK" -> if (sneaking) SHIFT_LEFT else LEFT
                        else -> if (sneaking) SHIFT_RIGHT else RIGHT
                    }

                    HologramInteractEvent(e.player, type, hologram).call()
                }
            }
        }
    }

}