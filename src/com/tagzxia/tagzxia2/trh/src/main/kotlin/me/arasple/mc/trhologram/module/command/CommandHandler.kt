package me.arasple.mc.trhologram.module.command

import me.arasple.mc.trhologram.module.command.impl.*
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.adaptCommandSender
import taboolib.common.platform.function.pluginVersion
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.MinecraftVersion
import taboolib.platform.util.asLangText

/**
 * @author Arasple
 * @author Score2
 * @date 2021/2/11 16:46
 */
@CommandHeader(name = "trhologram", aliases = ["hologram", "trhd", "hd"], permission = "trhologram.access")
object CommandHandler {

    val sub = hashMapOf<String, CommandExecutor>()

    @CommandBody(permission = "trhologram.command.list", optional = true)
    val list = CommandList.command

    @CommandBody(permission = "trhologram.command.teleport", optional = true)
    val teleport = CommandTeleport.command

    @CommandBody(permission = "trhologram.command.movehere", optional = true)
    val movehere = CommandMovehere.command

    @CommandBody(permission = "trhologram.command.create", optional = true)
    val create = CommandCreate.command

    @CommandBody(permission = "trhologram.command.delete", optional = true)
    val delete = CommandDelete.command

    @CommandBody(permission = "trhologram.command.reload", optional = true)
    val reload = CommandReload.command

    @CommandBody(permission = "trhologram.command.mirror", optional = true)
    val mirror = CommandMirror.command

    @CommandBody
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            generateMainHelper(sender)
        }
    }

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, argument ->
            if (argument.isEmpty()) {
                generateMainHelper(sender)
                return@execute
            }
            sender.sendMessage("§8[§2Tr§aHologram§8] §cERROR §3| Args §6$argument §3not found.")
            TellrawJson()
                .append("§8[§2Tr§aHologram§8] §bINFO §3| Type ").append("§f/trhologram help")
                .hoverText("§f/trhologram help §8- §7more help...")
                .suggestCommand("/trhologram help")
                .append("§3 for help.")
                .sendTo(adaptCommandSender(sender))
        }
    }

    private fun generateMainHelper(sender: CommandSender) {
        val proxySender = adaptCommandSender(sender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  ").append("§2TrHologram")
            .hoverText("§7TrHologram is modern and advanced Hologram-Plugin")
            .append(" ").append("§f${pluginVersion}")
            .hoverText(
                """
                §7Plugin version: §2${pluginVersion}
                §7NMS version: §b${MinecraftVersion.minecraftVersion}
            """.trimIndent()
            ).sendTo(proxySender)
        proxySender.sendMessage("")
        TellrawJson()
            .append("  §7${sender.asLangText("Command-Help-Type")}: ").append("§f/trhologram §8[...]")
            .hoverText("§f/trhologram §8[...]")
            .suggestCommand("/trhologram ")
            .sendTo(proxySender)
        proxySender.sendMessage("  §7${sender.asLangText("Command-Help-Args")}:")

        fun displayArg(name: String, desc: String) {
            TellrawJson()
                .append("    §8- ").append("§f$name")
                .hoverText("§f/trhologram $name §8- §7$desc")
                .suggestCommand("/trhologram $name ")
                .sendTo(proxySender)
            proxySender.sendMessage("      §7$desc")
        }
        sub.forEach { (name, executor) -> displayArg(name, executor.description(proxySender)) }
        proxySender.sendMessage("")
    }

}