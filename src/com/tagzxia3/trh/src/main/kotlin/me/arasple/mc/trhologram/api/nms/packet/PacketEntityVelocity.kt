package me.arasple.mc.trhologram.api.nms.packet

import me.arasple.mc.trhologram.api.InstantVector

class PacketEntityVelocity(entityId: Int, val x: Double, val y: Double, val z: Double): PacketEntity(entityId)