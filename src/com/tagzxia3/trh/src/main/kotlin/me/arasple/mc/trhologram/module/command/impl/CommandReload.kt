package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.api.Settings
import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang
import java.io.File

/**
 * @author Arasple
 * @date 2021/2/12 17:43
 */
object CommandReload : CommandExecutor {

    override val command = subCommand {
        dynamic(optional = true) {
            suggestion<CommandSender> { _, _ ->
                Hologram.holograms.map { it.id }
            }
            execute<CommandSender> { sender, _, argument ->
                val args = argument.split(" ")
                commandReload(sender, args[0])
            }
        }
        execute<CommandSender> { sender, _, _ ->
            commandReload(sender, null)
        }
    }

    override val name: String = "reload"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandReload(sender: CommandSender, name: String?) {
        Settings.onSettingsReload()
        val hologram = Hologram.findHologram { it.id.equals(name, true) }

        if (hologram != null) {
            hologram.destroy()
            Hologram.holograms.remove(hologram)

            hologram.loadedPath?.let {
                HologramLoader.load(File(it))
                sender.sendLang("Command-Reload", hologram.id)
            }
        } else {
            HologramLoader.load(sender)
            Bukkit.getOnlinePlayers().forEach(Hologram::refreshAll)
        }
    }

}