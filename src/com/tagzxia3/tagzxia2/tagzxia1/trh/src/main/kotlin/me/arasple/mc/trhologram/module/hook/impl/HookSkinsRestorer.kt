package me.arasple.mc.trhologram.module.hook.impl

import com.mojang.authlib.properties.Property
import me.arasple.mc.trhologram.module.hook.HookAbstract
import net.skinsrestorer.api.SkinsRestorerAPI
import org.bukkit.Bukkit

/**
 * @author Mical
 * @date 2021/8/19 20:52
 */
class HookSkinsRestorer : HookAbstract() {

    private val skinsRestorerAPI: SkinsRestorerAPI? =
        if (isHooked) {
            SkinsRestorerAPI.getApi()
        } else {
            null
        }
        get() {
            if (field == null) reportAbuse()
            return field
        }

    fun getPlayerSkinTexture(name: String): String? {
        skinsRestorerAPI?.let {
            val uuid = Bukkit.getPlayerExact(name)?.uniqueId?.toString() ?: return null
            if (it.getProfile(uuid) == null) {
                return null
            }

            val skinData = it.getSkinData(name)
            return (skinData as Property).value
        }
        return null
    }

}