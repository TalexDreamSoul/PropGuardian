package me.arasple.mc.trhologram.module.hook.impl

import me.arasple.mc.trhologram.module.hook.HookAbstract

/**
 * @author Mical
 * @date 2021/8/19 21:05
 */
class HookTrMenu : HookAbstract() {

    override val isHooked: Boolean
        get() = super.isHooked && plugin!!.description.version.startsWith("3")

}