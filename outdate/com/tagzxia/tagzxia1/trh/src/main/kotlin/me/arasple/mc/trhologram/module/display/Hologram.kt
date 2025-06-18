package me.arasple.mc.trhologram.module.display

import me.arasple.mc.trhologram.api.Position
import me.arasple.mc.trhologram.api.TrHologramAPI
import me.arasple.mc.trhologram.api.base.BaseCondition
import me.arasple.mc.trhologram.api.base.ClickHandler
import me.arasple.mc.trhologram.api.hologram.HologramBuilder
import me.arasple.mc.trhologram.api.hologram.HologramComponent
import me.arasple.mc.trhologram.api.hologram.ItemHologram
import me.arasple.mc.trhologram.api.hologram.TextHologram
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common5.mirrorNow

/**
 * @author Arasple
 * @date 2021/2/11 10:06
 */
class Hologram(
    val id: String,
    val position: Position,
    val viewDistance: Double = 0.0,
    val viewCondition: BaseCondition? = null,
    val refreshCondition: Long = -1,
    val components: List<HologramComponent>,
    val reactions: ClickHandler,
    val loadedPath: String? = null
) {

    companion object {

        internal val externalHolograms = mutableSetOf<Hologram>()
        internal val holograms = mutableSetOf<Hologram>()

        fun findHologram(predicate: (Hologram) -> Boolean): Hologram? {
            return holograms.find(predicate) ?: externalHolograms.find(predicate)
        }

        fun refreshAll(player: Player) {
            mirrorNow("Hologram:Event:Refresh") {
                externalHolograms.filter { it.sameWorld(player) }.forEach {
                    it.refreshVisibility(player)
                }
                holograms.filter { it.sameWorld(player) }.forEach {
                    it.refreshVisibility(player)
                }
            }
        }

        fun clear() {
            holograms.removeIf {
                it.destroy()
                true
            }
        }

        fun destroyAll(player: Player) {
            holograms.forEach {
                it.destroy(player)
            }
        }

    }

    private val visibleByCondition = mutableMapOf<String, Boolean>()
    private val viewers = mutableSetOf<String>()
    private var refreshTask: PlatformExecutor.PlatformTask? = null

    init {
        deployment()
    }

    private fun deployment() {
        refreshTask?.cancel()
        if (refreshCondition > 0) refreshTask =
            submit(delay = refreshCondition, period = refreshCondition, async = true) {
                viewers.removeIf {
                    val player = Bukkit.getPlayerExact(it)
                    player == null || !player.isOnline
                }
                Bukkit.getOnlinePlayers().filter { visibleByDistance(it) }.forEach {
                    refreshCondition(it)
                    refreshVisibility(it)
                }
            }
    }

    fun refreshVisibility(player: Player) {
        visible(player, visibleByDistance(player) && visibleByCondition(player))
    }

    fun sameWorld(player: Player): Boolean {
        return position.world == player.world
    }

    fun getTextLine(index: Int): TextHologram {
        return components[index] as TextHologram
    }

    fun getItemLine(index: Int): ItemHologram {
        return components[index] as ItemHologram
    }

    private fun visible(player: Player, visible: Boolean) {
        if (visible) {
            if (viewers.add(player.name)) components.forEach { it.spawn(player) }
        } else {
            if (viewers.remove(player.name)) components.forEach { it.destroy(player) }
        }
    }

    private fun visibleByDistance(player: Player): Boolean {
        return player.world == position.world && position.distance(player.location) <= viewDistance
    }

    private fun visibleByCondition(player: Player): Boolean {
        return if (visibleByCondition.containsKey(player.name)) {
            visibleByCondition[player.name] ?: true
        } else {
            refreshCondition(player)
            true
        }
    }

    private fun refreshCondition(player: Player) {
        viewCondition?.eval(player)?.thenApply {
            visibleByCondition[player.name] = it
        }
    }

    fun forViewers(viewer: (Player) -> Unit) {
        viewers.mapNotNull { Bukkit.getPlayerExact(it) }.forEach(viewer)
    }

    fun destroy(player: Player) {
        viewers.remove(player.name)
        components.forEach {
            it.viewers.remove(player.name)
        }
    }

    fun destroy() {
        refreshTask?.cancel()
        components.forEach(HologramComponent::destroy)
        externalHolograms.remove(this)
    }

    /**
     * WARNING: This will make all custom interspace invalid
     */
    fun rebuild(): HologramBuilder {
        destroy()

        return TrHologramAPI.builder(position.toLocation()).run {
            viewDistance(viewDistance)
            refreshCondition(refreshCondition)
            click(reactions)

            viewCondition?.let { viewCondition(it) }
            components.forEach {
                when (it) {
                    is TextHologram -> append(it.text, it.period0, onTick = it.onTick)
                    is ItemHologram -> append(it.display, it.period0, onTick = it.onTick)
                }
            }

            this
        }
    }

}