package com.tagzxia2.src.main.java.alarm.star.alarmstarsystem.config;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.entity.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataConfig {
    private static PlayerDataConfig instance;

    public static PlayerDataConfig getInstance() {
        if (instance == null) {
            instance = new PlayerDataConfig();
        }
        return instance;
    }

    public PlayerData getPd(Player player) {
        return playerDataMap.get(player.getName());
    }

    public Collection<PlayerData> getPlayerDatas() {
        return playerDataMap.values();
    }

    private static final Map<String, PlayerData> playerDataMap = new HashMap<>();

    private File mainFile;

    private PlayerDataConfig() {
        this.mainFile = new File(AlarmStarSystem.getInstance().getDataFolder(), "playerData");

        if ( !mainFile.exists() ) {
            mainFile.mkdirs();
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            playerDataMap.put(player.getName(), new PlayerData(player));
        });

    }

    public void saveAll() {
        for ( PlayerData pd : playerDataMap.values() ) {
            pd.save();
        }
//        playerDataMap.values().forEach(PlayerData::save);
    }

    public static class ConfigListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent e) {

            playerDataMap.put(e.getPlayer().getName(), new PlayerData(e.getPlayer()));

        }

        @EventHandler
        public void onQuit(PlayerQuitEvent e) {
            Player player = e.getPlayer();
            PlayerData pd = playerDataMap.get(player.getName());
            if ( pd == null ) {
                Bukkit.getLogger().warning("PlayerData is null: " + player.getName());
                return;
            }

            pd.save();
            playerDataMap.remove(player.getName());

        }

    }

}
