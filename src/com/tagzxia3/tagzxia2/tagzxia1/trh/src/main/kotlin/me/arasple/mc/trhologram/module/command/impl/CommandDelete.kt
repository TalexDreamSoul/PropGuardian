package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang
import java.io.File

/**
 * @author Arasple
 * @date 2021/2/12 17:41
 */
object CommandDelete : CommandExecutor {

    override val command = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                Hologram.holograms.map { it.id }
            }
            execute<Player> { sender, _, argument ->
                val args = argument.split(" ")
                commandDelete(sender, args[0])
            }
        }
    }

    override val name: String = "delete"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandDelete(sender: Player, name: String) {
        val hologram = Hologram.findHologram { it.id.equals(name, true) }

        if (hologram == null) {
            sender.sendLang("Command-Not-Exists", name)
            return
        }
        hologram.loadedPath?.let { File(it).delete() }
        hologram.destroy()
        Hologram.holograms.remove(hologram)
        sender.sendLang("Command-Deleted", name)
    }

}