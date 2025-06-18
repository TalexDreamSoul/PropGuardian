package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LogUtil {

    public static void log(String message) {

        Bukkit.getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', message)
        );

    }

}
