package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.CoalLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.CopperLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.DiamondLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.GoldLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.IronLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.ObsidianLuckyBag;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyBagModule extends BaseModule {

    public static List<String> enableWorlds = new ArrayList<>();

    public LuckyBagModule() {
        super("luckybag");
    }

    public static boolean doEnable(World world) {
        return enableWorlds.stream().anyMatch(w -> world.getName().equalsIgnoreCase(w));
    }

    @EventHandler
    public void open(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if ( !doEnable(player.getWorld()) ) return;

        ItemStack stack = event.getItem();
        if ( stack == null ) return;

        ItemStack off = player.getInventory().getItemInOffHand();
        if ( off.getType() != Material.AIR ) {
            new PlayerUser(player).infoActionBar("副手禁止放置任何物品！");

            return;
        }

        ItemMeta meta = stack.getItemMeta();

        if ( meta == null ) return;

        PlayerData pd = PlayerData.g(player);

        if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c龙年新辞 &8&l· &a&l新春福袋 &8&l· &e晨曦祝福")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "coal");
            new CoalLuckyBag().open(player);
        } else if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c儒学龙钟 &8&l· &a&l新春福袋 &8&l· &e晨曦光辉")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "iron");
            new IronLuckyBag().open(player);
        } else if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c金蚕龙态 &8&l· &a&l新春福袋 &8&l· &e金光晨曦")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "gold");
            new GoldLuckyBag().open(player);
        } else if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c烔欣龙融 &8&l· &a&l新春福袋 &8&l· &e绣绿晨曦")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "copper");
            new CopperLuckyBag().open(player);
        } else if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c钻湘龙奔 &8&l· &a&l新春福袋 &8&l· &e花蓝晨曦")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "diamond");
            new DiamondLuckyBag().open(player);
        } else if ( meta.getDisplayName().contains(
                ChatColor.translateAlternateColorCodes('&', "&c黑望耀龙 &8&l· &a&l新春福袋 &8&l· &e石锦晨曦")
        )) {
            event.setCancelled(true);
            BaseLuckyBag.increasePlayerOpenedAmo(pd, "obsidian");
            new ObsidianLuckyBag().open(player);
        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
//        if ( !doEnable(event.getPlayer().getWorld()) ) return;
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);

        if ( !pd.getOptionsEnabled("luckybag", true) ) return;

        Random random = new Random();

        if ( random.nextDouble() > 0.75 ) return;

        if ( random.nextDouble() < 0.05 ) {
            new ObsidianLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(2));
        }

        if ( event.getBlock().getType().name().contains("ORE") ) {
            if ( random.nextDouble() > 0.85 ) return;

            if ( random.nextDouble() < 0.2 ) {
                new GoldLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(2));
            } else if ( random.nextDouble() < 0.4 ) {
                new IronLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(3));
            } else {
                new CoalLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(5));
            }

            return;
        }

        if ( event.getBlock().getType().name().contains("LEAVES") ) {
            if ( random.nextDouble() > 0.5 ) return;

            if ( random.nextDouble() < 0.4 ) {
                new CopperLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(5));
            }

            if ( random.nextDouble() < 0.1 ) {
                new DiamondLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(2));
            }

            return;
        }

        if ( random.nextDouble() < 0.1 ) {
            new GoldLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(2));
        } else if ( random.nextDouble() < 0.3 ) {
            new IronLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(3));
        } else if ( random.nextDouble() < 0.5 ) {
            new CoalLuckyBag().drop2Loc(event.getBlock().getLocation(), random.nextInt(5));
        }
    }

    @EventHandler
    public void onKillMonster(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

//        if ( !doEnable(entity.getWorld()) ) return;

        Player killer = entity.getKiller();

        if ( killer == null ) return;

        PlayerData pd = PlayerData.g(killer);

        if ( !pd.getOptionsEnabled("luckybag", true) ) return;

        Random random = new Random();

        if ( random.nextDouble() < 0.15 ) {
            new ObsidianLuckyBag().drop2Loc(entity.getLocation(), random.nextInt(2));
        }

        if ( random.nextDouble() < 0.25 ) {
            new DiamondLuckyBag().drop2Loc(entity.getLocation(), random.nextInt(3));
            return;
        }

        if ( random.nextDouble() > 0.5 ) return;

        if ( random.nextDouble() < 0.2 ) {
            new GoldLuckyBag().drop2Loc(entity.getLocation(), random.nextInt(2));
        } else if ( random.nextDouble() < 0.4 ) {
            new IronLuckyBag().drop2Loc(entity.getLocation(), random.nextInt(3));
        } else {
            new CoalLuckyBag().drop2Loc(entity.getLocation(), random.nextInt(5));
        }
    }

    @Override
    public void onEnable() {
        enableWorlds.addAll(this.yaml.getStringList("Settings.allowPlaceWorlds"));
    }
}
