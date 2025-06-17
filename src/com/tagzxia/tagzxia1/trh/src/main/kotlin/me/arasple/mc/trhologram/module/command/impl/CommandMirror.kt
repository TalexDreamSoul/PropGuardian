package me.arasple.mc.trhologram.module.command.impl

import me.arasple.mc.trhologram.module.command.CommandExecutor
import me.arasple.mc.trhologram.module.command.CommandHandler
import org.bukkit.Bukkit
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.common5.Mirror

/**
 * @author Arasple
 * @date 2021/2/12 18:45
 */
object CommandMirror : CommandExecutor {

    override val command = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            commandMirror(sender)
        }
    }

    override val name: String = "mirror"

    init {
        CommandHandler.sub[name] = this
    }

    private fun commandMirror(sender: ProxyCommandSender) {
        submit(async = !Bukkit.isPrimaryThread()) {
            Mirror.report(sender) {
                childFormat = "§8  {0}§7{1} §2[{3} ms] §7{4}%"
                parentFormat = "§8  §8{0}§7{1} §8[{3} ms] §7{4}%"
            }.run {
                sender.sendMessage("\n§2§lHologram §a§l§nPerformance Mirror\n§r")
            }
        }
    }

}