package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.event.PlayerDoubleSneakEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;

public class GlobalListener implements Listener {

    public static Map<String, Long> diffMap = new HashMap<>();

    public static Map<String, Runnable> moveCheckers = new HashMap<>();

    public interface IChatFunction {
        void run(String msg);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if ( !TalexEssential.getInstance().done ) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Component.text("数据仍未完成加载，请稍后！"));
        }
    }

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        new OnlinePlayerData(event.getPlayer());

        OnlinePlayerData pd = PlayerData.g(event.getPlayer());

        if ( pd == null ) {
            event.getPlayer().kickPlayer("数据加载失败，请联系管理员！");
            return;
        }

        long updated = pd.getUpdated();
        long diff = System.currentTimeMillis() - updated;

        String lastOnline = String.format("&8| &e距上次上线: &b%d天%d小时%d分钟%d秒", diff / 86400000, diff % 86400000 / 3600000, diff % 3600000 / 60000, diff % 60000 / 1000);

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lastOnline));
//        event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&a➷ &7" + pd.getName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
//        event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&c➶ &8" + event.getPlayer().getName()));

        OnlinePlayerData data = PlayerData.g(event.getPlayer());

        if (data != null) {
            data.save();
        }

    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if ( !player.isSneaking() ) return;

        long time = System.currentTimeMillis();
        long diff = time - diffMap.getOrDefault(player.getName(), -1L);
        if (diff < 300) {
            Bukkit.getPluginManager().callEvent(new PlayerDoubleSneakEvent(player, event.isSneaking()));
        }
        diffMap.put(player.getName(), time);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);

        pd.setLastPlace(System.currentTimeMillis());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if ( !moveCheckers.containsKey(player.getName()) ) return;

        moveCheckers.get(player.getName()).run();
    }

//    @EventHandler
//    public void onClick(InventoryClickEvent event) {
//        if ( event.getWhoClicked().hasPermission("tea.admin") ) return;
//
//        ItemStack stack = event.getCurrentItem();
//        if ( stack != null && stack.getType() == Material.DRAGON_EGG ) {
//            event.setResult(Result.DENY);
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onClick(PlayerDropItemEvent event) {
//        if ( event.getPlayer().hasPermission("tea.admin") ) return;
//        Item itemDrop = event.getItemDrop();
//
//        if (itemDrop.getItemStack().getType() == Material.DRAGON_EGG) {
//            event.setCancelled(true);
//        }
//    }
//
//    @EventHandler
//    public void onClick(PlayerInteractEvent event) {
//        if ( event.getPlayer().hasPermission("tea.admin") ) return;
//
//        Block clickedBlock = event.getClickedBlock();
//        if ( clickedBlock != null && clickedBlock.getType() == Material.DRAGON_EGG ) {
//            event.setUseInteractedBlock(Result.DENY);
//            event.setUseItemInHand(Result.DENY);
//            event.setCancelled(true);
//
//            event.getPlayer().knockback(1.5, 3, 1.5);
//        }
//
//        ItemStack stack = event.getItem();
//        if ( stack != null && stack.getType() == Material.DRAGON_EGG ) {
//            event.setUseInteractedBlock(Result.DENY);
//            event.setUseItemInHand(Result.DENY);
//            event.setCancelled(true);
//
//            event.getPlayer().knockback(2.5, 4, 2.5);
//        }
//    }

}
