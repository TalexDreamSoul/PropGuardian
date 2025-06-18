package me.arasple.mc.trhologram.module.display.texture

import me.arasple.mc.trhologram.api.base.ItemTexture
import me.arasple.mc.trhologram.util.Heads
import me.arasple.mc.trhologram.util.ItemHelper
import me.arasple.mc.trhologram.util.containsPlaceholder
import me.arasple.mc.trhologram.util.parseString
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import taboolib.common.util.Strings.similarDegree
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.ItemBuilder
import taboolib.platform.util.buildItem
import kotlin.math.min

/**
 * @author Arasple
 * @date 2021/1/24 11:50
 */
class Texture(
    val raw: String,
    val type: TextureType,
    val texture: String,
    val dynamic: Boolean,
    val static: ItemStack?,
    val meta: Map<TextureMeta, String>
) : ItemTexture {

    override fun generate(player: Player): ItemStack {
        if (static != null) return static
        val temp = if (dynamic) player.parseString(texture) else texture

        val itemStack = when (type) {
            TextureType.NORMAL -> parseMaterial(temp)
            TextureType.HEAD -> Heads.getHead(temp)
            TextureType.RAW -> ItemHelper.fromJson(temp)
        }

        if (itemStack != null) {
            val itemMeta = itemStack.itemMeta
            meta.forEach { (meta, metaValue) ->
                val value = player.parseString(metaValue)
                when (meta) {
                    TextureMeta.DATA_VALUE -> itemStack.durability = value.toShortOrNull() ?: 0
                    TextureMeta.MODEL_DATA -> {
                        itemMeta?.setCustomModelData(value.toInt()).also { itemStack.itemMeta = itemMeta }
                    }
                    TextureMeta.LEATHER_DYE -> if (itemMeta is LeatherArmorMeta) {
                        itemMeta.setColor(serializeColor(value)).also { itemStack.itemMeta = itemMeta }
                    }
                }
            }
        }

        return itemStack ?: FALL_BACK
    }

    companion object {

        val FALL_BACK = ItemStack(Material.BEDROCK)

        fun createTexture(itemStack: ItemStack): Texture {
            val material = itemStack.type.name.lowercase().replace("_", " ")
            val itemMeta = itemStack.itemMeta

            // Head Meta
            val texture = let {
                if (itemMeta is SkullMeta) {
                    return@let if (itemMeta.hasOwner()) "head:${itemMeta.owningPlayer?.name}"
                    else "head:${Heads.seekTexture(itemStack)}"
                }
                // Model Data
                if (MinecraftVersion.majorLegacy >= 11400 && itemMeta != null && itemMeta.hasCustomModelData()) {
                    return@let "$material{model-data:${itemMeta.customModelData}}"
                }
                // Leather
                if (itemMeta is LeatherArmorMeta) {
                    return@let "$material{dye:${deserializeColor(itemMeta.color)}}"
                }
                return@let material
            }

            return createTexture(texture)
        }


        fun createTexture(raw: String): Texture {
            var type = TextureType.NORMAL
            var static: ItemStack? = null
            var texture = raw
            val meta = mutableMapOf<TextureMeta, String>()

            TextureMeta.values().forEach {
                it.regex.find(raw)?.groupValues?.get(1)?.also { value ->
                    meta[it] = value
                    texture = texture.replace(it.regex, "")
                }
            }

            TextureType.values().filter { it.group != -1 }.forEach {
                it.regex.find(texture)?.groupValues?.get(it.group)?.also { value ->
                    type = it
                    texture = value
                }
            }

            val dynamic = texture.containsPlaceholder()
            if (type == TextureType.NORMAL) {
                if (texture.startsWith("{")) {
                    type = TextureType.RAW
                    if (!dynamic) static = ItemHelper.fromJson(texture)!!
                }
            }
            return Texture(raw, type, texture, dynamic, static, meta)
        }

        private fun parseMaterial(material: String): ItemStack {
            val split = material.split(":", limit = 2)
            val data = split.getOrNull(1)?.toIntOrNull() ?: 0
            val id = split[0].toIntOrNull() ?: split[0].uppercase().replace("[ _]".toRegex(), "_")

            return buildItem(XMaterial.matchXMaterial(FALL_BACK)) {
                var rawMaterial = id

                if (id is Int) {
                    XMaterial.matchXMaterial(id, 0).let {
                        if (it.isPresent) {
                            this.material = it.get().parseMaterial()!!
                            this.damage = data
                        } else {
                            XMaterial.STONE
                        }
                    }

                } else {
                    val name = id.toString()
                    try {
                        this.material = XMaterial.valueOf(name).parseMaterial()!!
                    } catch (e: Throwable) {
                        val xMaterial =
                            XMaterial.values().find { it.name.equals(name, true) }
                                ?: XMaterial.values()
                                    .find { it -> it.legacy.any { it == name } }
                                ?: XMaterial.values()
                                    .maxByOrNull { similarDegree(name, it.name) }
                        xMaterial?.parseItem() ?: FALL_BACK
                    }
                }
            }
        }

        fun serializeColor(color: String): Color {
            val rgb = color.split(",").toTypedArray()
            if (rgb.size == 3) {
                val r = min(rgb[0].toIntOrNull() ?: 0, 255)
                val g = min(rgb[1].toIntOrNull() ?: 0, 255)
                val b = min(rgb[2].toIntOrNull() ?: 0, 255)
                return Color.fromRGB(r, g, b)
            }
            return Color.BLACK
        }

        fun deserializeColor(color: Color): String {
            return "${color.red},${color.green},${color.blue}"
        }

    }

}