package me.arasple.mc.trhologram.api.nms

import com.mojang.authlib.GameProfile
import me.arasple.mc.trhologram.api.nms.packet.PacketEntity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import taboolib.common.reflect.Reflex.Companion.setProperty
import taboolib.module.nms.nmsProxy
import taboolib.module.nms.sendPacket

/**
 * @author Arasple
 * @date 2020/12/4 21:20
 */
abstract class NMS {

    companion object {

        val INSTANCE by lazy {
            nmsProxy<NMS>()
        }
    }

    abstract fun sendEntityPacket(player: Player, vararg packets: PacketEntity)

    abstract fun sendEntityMetadata(player: Player, entityId: Int, vararg objects: Any)

    abstract fun parseVec3d(obj: Any): Vector

    fun sendPacket(player: Player, packet: Any, vararg fields: Pair<Any, Any>) {
        fields.forEach { packet.setProperty(it.first.toString(), it.second) }
        player.sendPacket(packet)
    }

    abstract fun getGameProfile(player: Player): GameProfile

}