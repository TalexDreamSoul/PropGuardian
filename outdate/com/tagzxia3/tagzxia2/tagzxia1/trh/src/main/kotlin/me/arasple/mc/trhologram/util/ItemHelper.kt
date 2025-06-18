package me.arasple.mc.trhologram.util

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.ItemTag
import taboolib.platform.util.buildItem

/**
 * @author Arasple
 * @date 2021/2/4 9:56
 */
object ItemHelper {

    fun isNull(item: ItemStack?): Boolean {
        return item == null || item.type == Material.AIR
    }

    fun fromJson(json: String): ItemStack? {
        try {
            val parse = JsonParser.parseString(json)
            if (parse is JsonObject) {
                val itemBuild = buildItem(parse["type"].let { it ?: XMaterial.STONE; XMaterial.valueOf(it.asString) }) {
                    parse["data"].let {
                        it ?: return@let
                        damage = it.asInt
                    }
                    parse["amount"].let {
                        it ?: return@let
                        amount = it.asInt
                    }
                }
                val meta = parse["meta"]
                return if (meta != null) itemBuild.also { ItemTag.fromJson(meta.toString()).saveTo(it) }
                else itemBuild
            }
            return null
        } catch (e: Throwable) {
            return null
        }
    }

}