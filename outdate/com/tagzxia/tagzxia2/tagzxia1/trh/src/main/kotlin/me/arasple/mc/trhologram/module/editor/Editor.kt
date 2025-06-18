package me.arasple.mc.trhologram.module.editor

import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * @author Arasple
 * @date 2021/2/12 17:09
 */
object Editor {

    fun modify(hologram: Hologram, modify: (YamlConfiguration) -> Unit) {
        val file = File(hologram.loadedPath!!)
        val conf = YamlConfiguration.loadConfiguration(file)
        modify(conf)
        conf.save(file)
        hologram.destroy()
        Hologram.holograms.remove(hologram)
        HologramLoader.load(file).run {
            Bukkit.getOnlinePlayers().forEach(this::refreshVisibility)
        }
    }

}