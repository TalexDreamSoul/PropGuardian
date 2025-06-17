package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.guider;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TeleportProfile extends MenuBasic {
    public TeleportProfile(Player player) {
        super(player, "&e跃迁传送", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    private static Map<String, TeleportedWorld> worlds = new HashMap<>();

    static {
        worlds.put("/spawn", new TeleportedWorld(0, "53095", "主城", Arrays.asList(
                "",
                "&8| &b主城&7, 展示最新咨询和排名",
                "&8| &7在这里, 高耸入云入云端",
                "&8| &7瑰丽壮阔, 玩家云集之地",
                "&8| &7探索一番新玩法",
                "",
                "&7&k|&a 点击传送 ...",
                ""
        )));

        worlds.put("world", new TeleportedWorld(0, "41025", "生存", Arrays.asList(
                "",
                "&8| &a生存&7, 捕捉自我领域",
                "&8| &7挑战四起, 自力更生建天堂",
                "&8| &7机遇遍布, 采集资源种田牧羊",
                "&8| &7与人乐, 打造家园自成一派",
                "&8| &7乐趣待君亲启",
                "",
                "&7&k|&a 点击传送 ...",
                ""
        )));

        worlds.put("world_nether", new TeleportedWorld(0, "57148", "&c地狱", Arrays.asList(
                "",
                "&8| &c地狱&7, 烈火燃烧藏掘",
                "&8| &7恶魔横行震慑, 勇气必备",
                "&8| &7火海飞扬, 海量宝藏以君掘",
                "&8| &7战斗, 止不尽的战斗",
                "&8| &7征服以危服君",
                "",
                "&7&k|&a 点击传送 ...",
                ""
        )));

        worlds.put("world_the_end", new TeleportedWorld(1, "1110", "&f末地", Arrays.asList(
                "",
                "&8| &5末地&7, 迷离传奇空悲尽",
                "&8| &7传奇久扬, 龙族统治威力长",
                "&8| &7奇珍异宝众多藏, 共新路 · 勉",
                "&8| &7勇者徽章, 虚无补岛, 耐尽千万家",
                "&8| &7迷失 · 永恒的话题",
                "",
                "&c&l！ &e末地死亡掉落, 请注意前往.",
                "",
                "&7&k|&a 点击传送 ...",
                ""
        )));

        worlds.put("parkour", new TeleportedWorld(2, "2070", "&f&lPARKOUR", Arrays.asList(
                "",
                "&8| &a活泼生命 动力健康 主题跑酷",
                "&8| &7奇怪的特别跑酷，立即出发！",
                "&8| &7生命不停，动力不止",
                "&8| &7每日重置，每个记录点都会有宝藏",
                "&8| &e动力 · 马上就出发",
                "",
                "&7&k|&a 点击传送 ...",
                ""
        )));

//        worlds.put("#SurviveOne", new TeleportedWorld(0, "60230", "&a星球I", Arrays.asList(
//                "",
//                "&8| &a星球I区&7, 始始复复量更新",
//                "&8| &7弥散万射，茫茫四海无路崖",
//                "&8| &7传新降临起变呈，点止不尽",
//                "&8| &7万里传奇此刻起，传承千万家",
//                "&8| &7新生 · 一切的开启",
//                "",
//                "&8| &a星球I区&7, 是全新开放的子服",
//                "&8| &7在这里，开启新的传奇，数据同步",
//                "&8| &7新区迭代迅速，限制开放，镜像世界",
//                "",
//                "&7&k|&a 点击传送 ...",
//                ""
//        )));
//
//        worlds.put("#SurviveTwo", new TeleportedWorld(0, "30432", "&a星球II", Arrays.asList(
//                "",
//                "&8| &a星球II区&7, 千载万秋终难变",
//                "&8| &7亿万传奇立鹤起，亿点滴",
//                "&8| &7空尽而已，悲极而鸣",
//                "&8| &7殊胜殊输，难以痞积",
//                "&8| &7与此 · 一切的终结",
//                "",
//                "&8| &a星球II区&7, 是原服务器",
//                "&8| &7在这里，继续你的传奇，数据同步",
//                "&8| &7老区不再迭代，仅修复BUG",
//                "",
//                "&7&k|&a 点击传送 ...",
//                ""
//        )));
//
//        worlds.put("#SurviveCreation", new TeleportedWorld(14, "21422", "&a星球创生", Arrays.asList(
//                "",
//                "&8| &a星球创生区&7, 异外星辰闪",
//                "&8| &7劈恐落雷惜自身以镜",
//                "",
//                "&8| &a星球创生区&7, 是生存创造服",
//                "&8| &7每日零点将与星球I区同步存档",
//                "&8| &7默认旁观者，自己领地内为创造模式",
//                "",
//                "&7&k|&c 暂未开放 ...",
//                ""
//        )));
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(13, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        PlayerData pd = PlayerData.g(player);
        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);

        int startSlot = 29;

        HeadDatabaseAPI api = new HeadDatabaseAPI();
        for ( Map.Entry<String, TeleportedWorld> entry : worlds.entrySet() ) {
            TeleportedWorld value = entry.getValue();
            int level = value.getNeedLevel();

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                        @Override
                        public ItemStack getItemStack() {
                            ItemBuilder itemBuilder = new ItemBuilder(rankLevel >= level ? api.getItemHead(value.getHeadID()) : new ItemStack(Material.BARRIER))
                                    .setName("&e" + value.getName());

                            itemBuilder.setLore(value.getDescriptions());

                            if ( rankLevel < level ) {

                                itemBuilder.addLoreLine("");
                                itemBuilder.addLoreLine("&8| &c需要 &e" + level + " &c通行等级 解锁");
                                itemBuilder.addLoreLine("");

                            }

                            return itemBuilder
                                    .toItemStack();
                        }

                        @Override
                        public boolean onClick(InventoryClickEvent e) {
                            if ( rankLevel < level ) return false;

                            String worldName = entry.getKey();
                            if ( worldName.startsWith("/") ) {
                                player.chat(worldName);
                            } else if ( worldName.startsWith("#") ) {
                                String serverName = worldName.substring(1);
                                new PlayerUser(player).sendToServer(serverName);
                            } else {
                                World world = Bukkit.getWorld(worldName);

                                Location spawnLocation = world.getSpawnLocation();
                                spawnLocation.setY(spawnLocation.getY() + .5);

                                player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 5, 2));

                                player.teleport(spawnLocation);
                            }

                            player.closeInventory();
                            player.playSound(player, Sound.ENTITY_FOX_TELEPORT, .5f, .5f);

                            return false;
                        }
                    }
            );

            startSlot += 1;
        }
    }

    @Getter
    @AllArgsConstructor
    private static class TeleportedWorld {
        private final int needLevel;
        private final String headID;
        private final String name;
        private final List<String> descriptions;
    }
}
