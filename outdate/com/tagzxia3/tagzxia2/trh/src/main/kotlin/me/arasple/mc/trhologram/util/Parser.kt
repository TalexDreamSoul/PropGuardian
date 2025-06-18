package me.arasple.mc.trhologram.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.adaptPlayer
import taboolib.common5.Coerce
import taboolib.module.chat.colored
import taboolib.module.kether.KetherFunction
import taboolib.platform.compat.replacePlaceholder

/**
 * @author Arasple
 * @date 2021/2/10 21:15
 */
private val PLACEHOLDER_API = "[%{](.+?)[%}]".toRegex()

fun String.containsPlaceholder(): Boolean {
    return PLACEHOLDER_API.find(this) != null
}

fun String.parseLocation(): Location {
    val (world, loc) = split("~", limit = 2)
    val (x, y, z) = loc.split(",", limit = 3).map { it.toDouble() }

    return Location(Bukkit.getWorld(world), x, y, z)
}

fun Location.parseString(): String {
    val world = world?.name
    val x = Coerce.format(x)
    val y = Coerce.format(y)
    val z = Coerce.format(z)

    return "$world~$x,$y,$z"
}

fun Player.parseString(string: String): String {
    return KetherFunction.parse(string) { sender = adaptPlayer(this@parseString) }.colored().replacePlaceholder(this)
}