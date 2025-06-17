package me.arasple.mc.trhologram.module.service

import me.arasple.mc.trhologram.module.display.Hologram
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.function.pluginVersion
import taboolib.module.metrics.Metrics
import taboolib.module.metrics.charts.SingleLineChart

/**
 * @author Arasple
 * @date 2020/3/7 22:15
 */
object Metrics {

    private val B_STATS by lazy { Metrics(6387, pluginVersion, Platform.BUKKIT) }

    @Awake(LifeCycle.INIT)
    fun initialization() {
        B_STATS.let {
            it.addCustomChart(SingleLineChart("holograms") {
                Hologram.holograms.size + Hologram.externalHolograms.size
            })
        }
    }

}