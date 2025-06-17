package me.arasple.mc.trhologram.module.display.texture

/**
 * @author Arasple
 * @date 2021/1/24 11:55
 */
enum class TextureType(val regex: Regex, val group: Int) {

    /**
     * Red Stained Glass Pane
     * Wool:3 (Data-Value does not support variables here)
     */
    NORMAL,

    /**
     * {identifier}:{parsedArgument}
     *
     * e.g.
     * head:%player_name%
     * head:BlackSky
     *
     * if the server runs SkinsRestorer and in offline mode, the player head will automatically support
     * if argument's length is larger than 64, then identified as custom textued skull
     *
     * head:eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRmNDUyZDk5OGVhYmFjNDY0MmM2YjBmZTVhOGY0ZTJlNjczZWRjYWUyYTZkZmQ5ZTZhMmU4NmU3ODZlZGFjMCJ9fX0=
     * head:44f452d998eabac4642c6b0fe5a8f4e2e673edcae2a6dfd9e6a2e86e786edac0
     */
    HEAD("[<{]?(player|custom|textured?)?-?(head|skull)[:=]([\\w.%{}]+)[>}]?", 3),

    /**
     * {json Content}
     *
     * Will be parsed if there contains placeholders
     */
    RAW;

    constructor(regex: String = "", group: Int = -1) : this(regex.toRegex(), group)

}