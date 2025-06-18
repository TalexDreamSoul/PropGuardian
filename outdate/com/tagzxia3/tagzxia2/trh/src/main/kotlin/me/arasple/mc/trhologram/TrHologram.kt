package me.arasple.mc.trhologram

import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import me.arasple.mc.trhologram.module.service.Updater
import org.bukkit.Bukkit
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.disablePlugin
import taboolib.common.platform.function.pluginVersion
import taboolib.module.lang.sendLang
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.BukkitPlugin

/**
 * @author Arasple
 * @date 2021/1/25 12:11
 */
object TrHologram : Plugin() {

    val plugin by lazy { BukkitPlugin.getInstance() }

    override fun onLoad() {
        console().sendLang("Plugin-Loading", Bukkit.getBukkitVersion())
    }

    override fun onEnable() {
        if (MinecraftVersion.majorLegacy <= 10900) {
            console().sendLang("Plugin-UnsupportedVersion", pluginVersion)
            disablePlugin()
            return
        }

        Settings.init()
        HologramLoader.load(Bukkit.getConsoleSender())
        console().sendLang("Plugin-Enabled", pluginVersion)

        Updater.init()
    }

    override fun onDisable() {
        Hologram.holograms.forEach(Hologram::destroy)
    }

}