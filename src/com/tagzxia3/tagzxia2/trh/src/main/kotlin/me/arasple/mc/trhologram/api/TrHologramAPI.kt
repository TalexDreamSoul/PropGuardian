package me.arasple.mc.trhologram.api

import me.arasple.mc.trhologram.api.hologram.HologramBuilder
import me.arasple.mc.trhologram.api.hologram.HologramComponent
import me.arasple.mc.trhologram.api.hologram.ItemHologram
import me.arasple.mc.trhologram.api.hologram.TextHologram
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.mirrorNow
import taboolib.library.kether.LocalizedException
import taboolib.module.kether.KetherShell
import java.util.concurrent.CompletableFuture

/**
 * @author Arasple
 * @date 2021/2/10 9:38
 */
object TrHologramAPI {

    /**
     * 实体 ID 取得
     */
    private var INDEX = resetIndex()

    fun getIndex(): Int {
        return INDEX++
    }

    internal fun resetIndex(): Int {
        return 1197897763 + (0..7763).random()
    }

    @JvmStatic
    fun eval(player: Player, script: String): CompletableFuture<Any?> {
        return mirrorNow("Hologram:Handler:ScriptEval") {
            return@mirrorNow try {
                KetherShell.eval(script, namespace = listOf("trhologram", "trmenu")) {
                    sender = adaptPlayer(player)
                }
            } catch (e: LocalizedException) {
                println("[TrHologram] Unexpected exception while parsing kether shell:")
                e.localizedMessage.split("\n").forEach {
                    println("[TrHologram] $it")
                }
                CompletableFuture.completedFuture(false)
            }
        }
    }

    @JvmStatic
    fun getHologramById(id: String): Hologram? {
        return Hologram.holograms.find { it.id == id }
    }

    @JvmStatic
    fun createTextCompoent(
        initText: String,
        location: Location,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ): TextHologram {
        return TextHologram(initText, Position.fromLocation(location), tick, onTick)
    }

    @JvmStatic
    fun createItemCompoent(
        initItem: ItemStack,
        location: Location,
        tick: Long = -1,
        onTick: (HologramComponent) -> Unit = {}
    ): ItemHologram {
        return ItemHologram(initItem, Position.fromLocation(location), tick, onTick)
    }

    @JvmStatic
    fun builder(location: Location): HologramBuilder {
        return HologramBuilder(location = location)
    }


}