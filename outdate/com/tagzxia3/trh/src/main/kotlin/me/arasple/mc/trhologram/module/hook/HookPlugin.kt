package me.arasple.mc.trhologram.module.hook

import me.arasple.mc.trhologram.module.hook.impl.HookSkinsRestorer
import me.arasple.mc.trhologram.module.hook.impl.HookTrMenu
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.module.lang.sendLang

/**
 * @author Mical
 * @date 2021/8/19 21:00
 */
object HookPlugin {

    @Awake(LifeCycle.ENABLE)
    fun printInfo() {
        registry.filter { it.isHooked }.forEach {
            console().sendLang("Plugin-Dependency-Hooked", it.name)
        }
    }

    private val registry: Array<HookAbstract> = arrayOf(
        HookSkinsRestorer(),
        HookTrMenu()
    )

    fun getSkinsRestorer(): HookSkinsRestorer {
        return registry[0] as HookSkinsRestorer
    }

    fun getTrMenu(): HookTrMenu {
        return registry[1] as HookTrMenu
    }

}