package me.arasple.mc.trhologram.module.command

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.SimpleCommandBody
import taboolib.module.chat.colored
import taboolib.module.lang.asLangText

/**
 * @author Mical
 * @date 2021/8/17 11:56
 */
interface CommandExecutor {

    val command: SimpleCommandBody

    fun description(sender: ProxyCommandSender): String =
        sender.asLangText("COMMAND-${name.uppercase()}-DESCRIPTION").colored()

    val name: String

}