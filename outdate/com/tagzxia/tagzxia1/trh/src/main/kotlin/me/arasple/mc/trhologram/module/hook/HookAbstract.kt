package me.arasple.mc.trhologram.module.hook

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * @author Mical
 * @date 2021/8/19 21:00
 */
abstract class HookAbstract {

    open val name by lazy { getPluginName() }

    val plugin: Plugin? by lazy {
        Bukkit.getPluginManager().getPlugin(name)
    }

    open val isHooked by lazy {
        plugin != null && plugin!!.isEnabled
    }

    open fun getPluginName(): String {
        return javaClass.simpleName.substring(4)
    }

    open fun checkHooked(): Boolean {
        return if (isHooked) true else false.also { reportAbuse() }
    }

    fun reportAbuse() {
        console().sendLang("Plugin-Dependency-Abuse", name)
    }

}
