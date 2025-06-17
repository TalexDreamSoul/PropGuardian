package me.arasple.mc.trhologram.api

import me.arasple.mc.trhologram.api.nms.NMS
import me.arasple.mc.trhologram.api.nms.packet.PacketEntityVelocity
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit

// 模 / 时间 = 速度
class InstantVector(val x: Double, val y: Double, val z: Double, val arrival: Long = 0): Cloneable {

    val useing = mutableMapOf<Int, InstantVector>()

    val hasArrival get() = arrival > 0

    val planVector: InstantVector get() {
        if (!hasArrival) {
            return this
        }
        return InstantVector(x / arrival, y / arrival, z / arrival)
    }

    fun useToVanilla(player: Player, entityId: Int) {
        if (!hasArrival) {
            NMS.INSTANCE.sendEntityPacket(player, PacketEntityVelocity(entityId, x, y, z))
            return
        }
        val vec = planVector
        useing[entityId] = vec
        NMS.INSTANCE.sendEntityPacket(player, PacketEntityVelocity(entityId, vec.x, vec.y, vec.z))
        submit(delay = arrival) {
            NMS.INSTANCE.sendEntityPacket(player, PacketEntityVelocity(entityId, 0.0, 0.0, 0.0))
            useing.remove(entityId)
        }
    }

    fun toPosition(w: World) =
        Position(w, x, y, z)

    override fun clone() =
        super.clone() as InstantVector

    companion object {

        @JvmStatic
        fun fromVector(v: Vector, arrival: Long= 0) =
            InstantVector(v.x, v.y, v.z, arrival)

    }

}