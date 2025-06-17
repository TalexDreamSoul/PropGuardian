package me.arasple.mc.trhologram.api.hologram

import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.base.ItemTexture
import me.arasple.mc.trhologram.api.base.TickEvent
import me.arasple.mc.trhologram.api.nms.packet.*
import me.arasple.mc.trhologram.module.display.texture.Texture
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.submit
import taboolib.common5.mirrorNow

/**
 * @author Arasple
 * @date 2021/2/10 21:29
 *
 * Y: position.Y + 0.75
 */
class ItemHologram(
    texture: ItemTexture,
    position: Position,
    tick: Long = -1,
    onTick: TickEvent? = null
) : HologramComponent(position.clone(y = 1.3), tick, onTick) {

    constructor(
        itemStack: ItemStack,
        position: Position,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ) : this(
        Texture.createTexture(itemStack), position.clone(y = 1.3), tick, onTick
    )

    var display: ItemTexture = texture
        set(value) {
            forViewers { updateItem(it) }
            field = value
        }

    override fun spawn(player: Player) {
        destroy(player)
        val teid = TrHologramAPI.getIndex()

        PacketEntitySpawn(teid, position).send(player)
        PacketArmorStandModify(
            teid,
            isInvisible = true,
            isGlowing = false,
            isSmall = true,
            hasArms = false,
            noBasePlate = true,
            isMarker = true
        ).send(player)

        PacketEntitySpawn(entityId, position, false).send(player)
        updateItem(player)

        submit(delay = 1L, async = !Bukkit.isPrimaryThread()) {
            PacketEntityMount(teid, IntArray(1) { entityId }).send(player)
            submit(delay = 20 * 5, async = !Bukkit.isPrimaryThread()) {
                PacketEntityDestroy(teid).send(player)
            }
        }

        viewers.add(player.name)
    }

    override fun onTick() {
        mirrorNow("Hologram:Event:Tick:ItemComponent") {
            forViewers { updateItem(it) }
        }
    }

    private fun updateItem(player: Player) {
        PacketItemModify(entityId, isInvisible = false, isGlowing = false, itemStack = display.generate(player)).send(
            player
        )
    }

}