package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import me.arasple.mc.trhologram.module.display.Hologram
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

/**
 * @author Arasple
 * @date 2021/2/12 17:59
 */
object CommandList : CommandExecutor {

    override val command = subCommand {
        dynamic(optional = true) {
            execute<ProxyCommandSender> { sender, _, argument ->
                commandList(sender, argument)
            }
        }
        execute<ProxyCommandSender> { sender, _, _ ->
            commandList(sender, null)
        }
    }

    override val name: String = "list"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandList(sender: ProxyCommandSender, filter: String?) {
        val holograms = Hologram.holograms.filter { filter == null || it.id.contains(filter, true) }.sortedBy { it.id }

        if (holograms.isEmpty()) {
            sender.sendLang("Command-List-Error", filter ?: "*")
        } else {
            sender.sendLang("Command-List-Header", holograms.size, filter ?: "*")

            holograms.forEach {
                sender.sendLang(
                    "Command-List-Format",
                    it.id,
                    it.components.size
                )
            }
        }
    }

}