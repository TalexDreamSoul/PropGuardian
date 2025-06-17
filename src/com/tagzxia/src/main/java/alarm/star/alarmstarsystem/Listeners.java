package com.tagzxia.src.main.java.alarm.star.alarmstarsystem;

import alarm.star.alarmstarsystem.config.AlarmStarConfig;
import alarm.star.alarmstarsystem.config.AreaConfig;
import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.config.TalexShopConfig;
import alarm.star.alarmstarsystem.entity.*;
import alarm.star.alarmstarsystem.enums.AreaType;
import alarm.star.alarmstarsystem.inventory.menu.PlayerShop;
import alarm.star.alarmstarsystem.utils.StringUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Listeners implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ChatReq req = ChatReq.reqList.get(player.getName());
        if ( req == null ) return;

        event.setCancelled(true);
        req.onChatInput(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        Stream<? extends Map.Entry<String, ?>> entryStream = AlarmStarConfig.getInstance(AlarmStarConfig.class).getMap().entrySet().stream().filter(e -> e.getKey().equals(String.valueOf(pd.getStarCount())));
        if ( entryStream.count() < 1L || !entryStream.iterator().hasNext() ) return;

        AlarmStar as = (AlarmStar) entryStream.iterator().next().getValue();

        if ( as != null ) {
            double h = player.getHealth();
            player.setHealth(as.getReserveHealth() * h);

            if ( as.isClear() ) {
                pd.setWarningDummy(0);
            }

            if ( as.isPrison() ) {
                pd.setPrison(true);
            }

            if ( as.getPay() > 0 ) {
                EconomyResponse economyResponse = AlarmStarSystem.getEcon().depositPlayer(player, as.getPay());

                if ( !economyResponse.transactionSuccess() ) {
                    player.sendMessage("§a[AlarmStar] §f보석 " + as.getPay() + "개를 받았습니다.");
                    return;
                }
            }
        }

        for (PlayerData.MoneyStream stream : pd.getMoneyStreams()) {

            TalexShop shop = TalexShopConfig.getShop(stream.getShopOwner());

            shop.addBalance(stream.getMoney());

        }

        pd.getMoneyStreams().clear();
    }

    @EventHandler
    public void onDamagedByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if ( !(damager instanceof Player) ) return;

        Player player = (Player) damager;
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        // Kill other players
        if ( entity instanceof Player ) {
            Player victim = (Player) entity;
            PlayerData vpd = PlayerDataConfig.getInstance().getPd(victim);

            if ( !victim.isDead() ) return;

            if ( vpd.getStarCount() > 0 ) pd.addWarningDummy(5);
            else pd.addWarningDummy(10);

            return;
        }

        if ( entity instanceof Boat ) {
            pd.addWarningDummy(1);
            return;
        }

//        LivingEntity livingEntity = (LivingEntity) entity;
        if ( entity.getCustomName() != null ) {
            List<String> policeForcesMatcher = AlarmStarSystem.getInstance().getConfig().getStringList("Settings.dummy.triggers.entity.police");

            if ( policeForcesMatcher.stream().anyMatch(entity.getCustomName()::contains) ) {
                pd.addWarningDummy(5);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player entity = event.getEntity();
        Player killer = entity.getKiller();
        if ( killer == null ) return;

        PlayerData pd = PlayerDataConfig.getInstance().getPd(entity);
        PlayerData kpd = PlayerDataConfig.getInstance().getPd(killer);

        if ( pd.getStarCount() > 0 ) kpd.addWarningDummy(5);
        else kpd.addWarningDummy(10);
    }

    public static Map<String, Long> playerSafeList = new HashMap<>();

    private static Area safeArea = null;

    public static void electSafeArea() {

        Stream<?> areas = AreaConfig.getInstance(AreaConfig.class).getMap().values().stream().filter(v -> ((Area) v).getAreaType() == AreaType.SAFE);

        List<Area> areaList = areas.map(v -> (Area) v).collect(Collectors.toList());

        if ( areaList.size() == 0 ) return;

        int index = (int) (Math.random() * areaList.size());

        Area area = areaList.get(index);

        safeArea = area;

        Bukkit.broadcastMessage("§a[AlarmStar] §f安全区已刷新在 §e" + area.getMiddlePoint().toVector() + "§8(§a" + area.getWorld().getName() + "§8) §f附近.");

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);
        if ( pd == null ) throw new NullPointerException("PlayerData is null: " + player.getName());

        AreaConfig.getInstance(AreaConfig.class).getMap().forEach((k, v) -> {
            Area area = (Area) v;
            if ( area == null ) throw new NullPointerException("Area is null: " + k);

            if ( pd.isPrison() && area.getAreaType() == AreaType.PRISON ) {
                if ( !area.isInArea(event.getTo()) ) {
                    Location pLoc = player.getLocation();
                    Location mLoc = area.getMiddlePoint();

                    mLoc = mLoc.getWorld().getHighestBlockAt(mLoc).getLocation();

                    mLoc.setPitch(pLoc.getPitch());
                    mLoc.setYaw(pLoc.getYaw());

                    player.teleport(mLoc);
                    pd.actionBar("§c§l你已被囚禁，无法离开监狱！").sound(Sound.BLOCK_ANVIL_FALL);
                    player.playEffect(player.getLocation(), org.bukkit.Effect.SMOKE, 1);
                }
            } else if ( area.getAreaType() == AreaType.POLICE && area == safeArea && area.isInArea(event.getTo()) ) {

                pd.actionBar("§a§l你已进入警局！").sound(Sound.ENTITY_ENDERDRAGON_FLAP);

                pd.addWarningDummy(40);
            }

            if ( pd.getStarCount() < 1 || area.getAreaType() != AreaType.SAFE  || !area.isInArea(event.getFrom()) ) return;

            boolean inArea = area.isInArea(event.getTo());
            Area.SafeData areaData = (Area.SafeData) area.getAreaData();

            if ( playerSafeList.containsKey(player.getName()) ) {
                long diff = System.currentTimeMillis() - playerSafeList.get(player.getName());
                long time = areaData.getTimeMap().getOrDefault(pd.getStarCount(), 100);
                if ( !inArea ) {

                    if ( diff > time ) {
                        pd.setWarningDummy(0);
                    }

                    playerSafeList.remove(player.getName());
                    pd.actionBar("§c§l你已离开安全区域！").sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                    player.playEffect(player.getLocation(), org.bukkit.Effect.SMOKE, 1);
                } else {
                    if ( pd.getStarCount() < 1 ) return;

                    double percent = (double) diff / time / 100;

//                    System.out.println(percent);

                    if ( percent >= 100 ) {
//                        pd.actionBar("§a离开安全区已清空警星!");

                        double money = 0;

                        for ( PlayerData.MoneyStream stream : pd.getMoneyStreams() ) {
                            money += stream.getMoney();
                        }

                        EconomyResponse economyResponse = AlarmStarSystem.getEcon().depositPlayer(player, money);

                        if ( economyResponse.transactionSuccess() ) {
                            pd.getMoneyStreams().clear();
                            pd.setWarningDummy(0);
                            playerSafeList.remove(player.getName());
                            pd.actionBar("§a§l你的警星已清空，收益已到账！").sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                            player.playEffect(player.getLocation(), Effect.VILLAGER_PLANT_GROW, 1);
                        } else {
                            pd.actionBar("§c§l你的金额奖励错误，请联系管理员！").sound(Sound.BLOCK_ANVIL_FALL).msg("§c§l你的金额奖励错误，请联系管理员！");
                            player.playEffect(player.getLocation(), org.bukkit.Effect.SMOKE, 1);

                            throw new RuntimeException(money + "| 警星奖励错误: " + economyResponse.errorMessage);
                        }

                    } else pd.actionBar("§e清空警星进程: §r" + StringUtil.generateProgressString(percent, 10, "§b|", "§7|"));
                }
            } else if ( inArea ) {
                playerSafeList.put(player.getName(), System.currentTimeMillis());

                pd.actionBar("§a§l你已进入安全区域！").sound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                player.playEffect(player.getLocation(), org.bukkit.Effect.SMOKE, 1);
            }
        });

    }

//    @EventHandler
//    public void onClick(NPCRightClickEvent event) {
//        NPC npc = event.getNPC();
//        if ( npc.data().has("TALEX_LEASE_SHOP_ID") ) return;
//
//        TalexShop shop = TalexShopConfig.getShop(npc.getFullName());
//
//        if ( shop == null || shop.getType() == 999 ) return;
//
//        new PlayerShop(event.getClicker(), npc.getFullName()).open();
//    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        // if player prison
        if ( !pd.isPrison() ) return;

        // if inventory = PlayerInventory
        if ( event.getInventory().getType() == InventoryType.PLAYER ) {
            pd.actionBar("§c§l你已被囚禁，无法打开背包！").sound(Sound.BLOCK_CHEST_CLOSE);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        if ( !pd.isPrison() ) return;

        event.setCancelled(true);
    }
}
