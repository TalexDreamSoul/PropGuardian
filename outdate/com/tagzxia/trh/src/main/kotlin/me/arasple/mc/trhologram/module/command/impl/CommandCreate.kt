package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import me.arasple.mc.trhologram.module.conf.HologramLoader
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/2/12 14:52
 */
object CommandCreate : CommandExecutor {

    override val command = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                Hologram.holograms.map { it.id }
            }
            execute<Player> { sender, _, argument ->
                val args = argument.split(" ")
                commandCreate(sender, args[0])
            }
        }
    }

    override val name: String = "create"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandCreate(sender: Player, name: String) {
        val hologram = Hologram.findHologram { it.id.equals(name, true) }

        if (hologram != null) {
            sender.sendLang("Command-Existed")
            return
        }

        HologramLoader.create(name, sender.location.clone().add(0.0, 2.0, 0.0)).refreshVisibility(sender)
        sender.sendLang("Command-Created")
    }

}