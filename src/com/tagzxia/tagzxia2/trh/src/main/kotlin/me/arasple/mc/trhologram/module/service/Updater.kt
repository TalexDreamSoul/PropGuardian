package me.arasple.mc.trhologram.module.service

import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.console
import taboolib.common.platform.function.pluginVersion
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import taboolib.platform.util.sendLang
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Arasple
 * @date 2020/7/28 18:30
 */
object Updater {

    // FIXME: 从某种意义上来说, 有没有可能网络延迟太大然后无法获取到版本号? 也许应该换一个地方存.
    private val url = URL("https://github.com/Micalhl/TrHologram/raw/master/Version.txt")
    private var LATEST_VERSION: String? = ""
    private var NOTIFIED = false
    private val NOTIFIED_PLAYER = mutableSetOf<UUID>()

    fun init() {
        submit(delay = 20, period = (10 * 60 * 20), async = true) {
            grabInfo()
        }
    }

    @Schedule(true, 20, 20 * 60 * 10)
    private fun grabInfo() {
        LATEST_VERSION = getLatestVersion().also {
            if (it != null && !NOTIFIED && it != pluginVersion) {
                console().sendLang("Plugin-Update", it)
                NOTIFIED = true
            }
        }
    }

    private fun getLatestVersion(): String? {
        var connection: HttpURLConnection? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            val buffer = StringBuilder(255)
            BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)).use { reader ->
                val buffer0 = CharArray(255)
                while (true) {
                    val length = reader.read(buffer0)
                    if (length == -1) break
                    buffer.append(buffer0, 0, length)
                }
            }
            return buffer.toString().trim()
        } catch (ignored: Throwable) {
        } finally {
            connection?.disconnect()
        }
        return null
    }

    @SubscribeEvent(EventPriority.HIGHEST)
    fun e(e: PlayerJoinEvent) {
        LATEST_VERSION?.let {
            if (e.player.isOp && LATEST_VERSION != pluginVersion && !NOTIFIED_PLAYER.contains(e.player.uniqueId)) {
                e.player.sendLang("Plugin-Update", it)
                NOTIFIED_PLAYER.add(e.player.uniqueId)
            }
        }
    }

}