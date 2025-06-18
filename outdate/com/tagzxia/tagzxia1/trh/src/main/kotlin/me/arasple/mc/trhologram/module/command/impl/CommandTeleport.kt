package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import me.arasple.mc.trhologram.module.display.Hologram
import org.bukkit.entity.Player
import taboolib.common.platform.command.subCommand
import taboolib.platform.util.sendLang

/**
 * @author Arasple
 * @date 2021/2/13 10:43
 */
object CommandTeleport : CommandExecutor {

    override val command = subCommand {
        dynamic {
            suggestion<Player> { _, _ ->
                Hologram.holograms.map { it.id }
            }
            execute<Player> { sender, _, argument ->
                val args = argument.split(" ")
                commandTeleport(sender, args[0])
            }
        }
    }

    override val name: String = "teleport"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandTeleport(sender: Player, name: String) {
        val hologram = Hologram.findHologram { it.id.equals(name, true) }

        if (hologram == null) {
            sender.sendLang("Command-Not-Exists", name)
            return
        }

        sender.teleport(hologram.position.toLocation())
    }

}