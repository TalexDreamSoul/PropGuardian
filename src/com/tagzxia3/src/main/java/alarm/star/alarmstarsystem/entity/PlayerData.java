package com.tagzxia3.src.main.java.alarm.star.alarmstarsystem.entity;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.config.MonsterConfig;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.entity.Monster;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import lombok.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class PlayerData {

    private final Player player;

    private final File file;

    private final YamlConfiguration yaml = new YamlConfiguration();

    @Setter
    private int warningDummy;

    @Setter
    private List<MoneyStream> moneyStreams = new ArrayList<>();

    private boolean prison;

    public PlayerData setPrison(boolean poison) {
        this.prison = poison;

        if ( this.prison ) {
            this.actionBar("§c§l您已经开始服役！");
            player.teleport(player.getLocation().clone().add(0, 0.1, 0));
        } else {
            this.actionBar("§a§l您已被解除刑期！");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.25f);
        }

        return this;
    }

    public PlayerData addWarningDummy(int dummy) {
        this.warningDummy += dummy;

        return this;
    }

    public PlayerData addDayMoney(MoneyStream dayMoney) {
        this.moneyStreams.add(dayMoney);
        return this;
    }

    @AllArgsConstructor
    @Data
    public static class MoneyStream {
        private final double money;
        private final String shopOwner;
    }

    public int getStarCount() {
        if ( this.warningDummy >= 100 ) return 5;
        if ( this.warningDummy >= 40 ) return 4;
        if ( this.warningDummy >= 15 ) return 3;
        if ( this.warningDummy >= 5 ) return 2;
        if ( this.warningDummy >= 1 ) return 1;
        return 0;
    }

    public PlayerData(Player player) {

        this.player = player;
        this.file = new File(AlarmStarSystem.getInstance().getDataFolder(), "playerData/" + player.getName() + ".yml");

        if ( !this.file.exists() ) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {

            try {
                this.yaml.load(this.file);


            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

        }

        this.__init__();
        this.__init__func__();

    }

    private void __init__() {
//        this.starCount = this.yaml.getInt("PlayerData.Manifest.starCount", 0);
        this.warningDummy = this.yaml.getInt("PlayerData.Manifest.warningDummy", 0);
//        this.dayMoney = this.yaml.getInt("PlayerData.Manifest.dayMoney", 0);

        this.prison = this.yaml.getBoolean("PlayerData.Detailed.prison", false);
    }

    private void __init__func__() {

        HashMap<String, Long> map = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {

                if ( !player.isOnline() ) {
                    cancel();
                    return;
                }

                if ( getStarCount() < 1 ) return;

                MobManager mm = MythicMobs.inst().getMobManager();
                Location spawnLocation = player.getLocation();

//                AlarmStar as = (AlarmStar) AlarmStarConfig.getInstance(AlarmStarConfig.class).get(String.valueOf(getStarCount()));

                int radius = MonsterConfig.SPAWN_RADIUS;
                List<Monster> monster = MonsterConfig.starMap.get(getStarCount());

                if ( monster == null )
                    throw new RuntimeException("Star " + getStarCount() + " not found in MonstersConfig!");

                monster.forEach(m -> {

                    long ts = map.getOrDefault(m.getMob(), -1L);

                    if ( System.currentTimeMillis() - ts <= m.getCd() * 1000L) return;

                    map.put(m.getMob(), System.currentTimeMillis());

                   new BukkitRunnable() {
                       @Override
                       public void run() {
                           MythicMob mob  = mm.getMythicMob(m.getMob());

                           if ( mob == null )
                               return;
//                               throw new RuntimeException("MythicMob " + m.getMob() + " not found!");

                           for ( int i = 0; i < m.getAmo(); i++ ) {
                               Location loc = spawnLocation.clone().add(Math.random() * radius, 0, Math.random() * radius);
                               loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

                               /*ActiveMob am = */mm.spawnMob(m.getMob(), loc);

                           }
                       }
                   }.runTask(AlarmStarSystem.getInstance());

                });

            }
        }.runTaskTimerAsynchronously(AlarmStarSystem.getInstance(), 0, 20);

        if ( this.isPrison() )
            this.setPrison(true);
    }

    @SneakyThrows
    public void save() {
//        this.yaml.set("PlayerData.Manifest.starCount", this.starCount);
        this.yaml.set("PlayerData.Manifest.warningDummy", this.warningDummy);
//        this.yaml.set("PlayerData.Manifest.dayMoney", this.dayMoney);

        this.yaml.set("PlayerData.Detailed.prison", this.prison);

        this.yaml.save(this.file);
    }

    public boolean f() {
        return false;
    }

    public boolean t() {
        return true;
    }

    public PlayerData actionBar(String msg) {
        ProtocolManager pLib = ProtocolLibrary.getProtocolManager();
        PacketContainer barContainer = pLib.createPacket(PacketType.Play.Server.CHAT);

        barContainer.getChatComponents().write(0, WrappedChatComponent.fromText(ChatColor.translateAlternateColorCodes('&', msg)));
        barContainer.getChatTypes().write(0, EnumWrappers.ChatType.GAME_INFO);

        pLib.sendServerPacket(this.player, barContainer);
        return this;
    }

    public PlayerData msg(String msg) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return this;
    }

    public PlayerData msg(String msg, Object... args) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(msg, args)));
        return this;
    }

    public PlayerData msgs(String... msgs) {
        for (String msg : msgs) {
            this.msg(msg);
        }
        return this;
    }

    public PlayerData msgs(List<String> msgs) {
        for (String msg : msgs) {
            this.msg(msg);
        }
        return this;
    }

    public PlayerData templateMsgs(String... msgs) {

        player.sendMessage(" ");
        player.sendMessage(" ");
        player.sendMessage("  §8[§cAlarm§eStar§7System§8]");
        player.sendMessage(" ");
        for (String msg : msgs) {
            this.msg("  &r" + msg);
        }
        player.sendMessage(" ");
        player.sendMessage(" ");

        return this;
    }

    public PlayerData sound(Sound sound, float volume, float pitch) {
        this.player.playSound(this.player.getLocation(), sound, volume, pitch);
        return this;
    }

    public PlayerData sound(Sound sound) {
        this.sound(sound, 1, 1);
        return this;
    }

    public PlayerData sound(Sound sound, float pitch) {
        this.sound(sound, 1, pitch);
        return this;
    }
}
