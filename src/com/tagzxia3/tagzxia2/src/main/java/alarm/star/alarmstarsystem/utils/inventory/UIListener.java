package com.tagzxia3.tagzxia2.src.main.java.alarm.star.alarmstarsystem.utils.inventory;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.utils.inventory.InventoryUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * @author TalexDreamSoul
 */
public class UIListener implements Listener {

    HashMap<String, Long> ts = new HashMap<>(32);

    public UIListener() {

        run();

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory inv = event.getClickedInventory();

        if ( inv == null || event.getCurrentItem() == null ) {
            return;
        }

        if ( !( inv.getHolder() instanceof InventoryUI.InventoryUIHolder ) ) {
            return;
        }

        InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) inv.getHolder();
        InventoryUI ui = inventoryUIHolder.getInventoryUI();

        ui.onInventoryClick(event);

        InventoryUI.ClickableItem item = ui.getCurrentUI().getItem(event.getSlot());

        if ( item == null ) {

            return;

        }

        Player player = (Player) event.getWhoClicked();

        if ( ts.containsKey(player.getName()) ) {

            if ( System.currentTimeMillis() - ts.get(player.getName()) < ui.getInterval() ) {

                event.setCancelled(true);
                player.sendMessage(AlarmStarSystem.getInstance() + " §c您的点击速度过快...");
                return;

            }

        }

        ts.put(player.getName(), System.currentTimeMillis());

        boolean a = item.onClick(event);

        if ( !ui.allowPutItem() || a ) {

            event.setCancelled(true);

        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if ( !( event.getInventory().getHolder() instanceof InventoryUI.InventoryUIHolder ) ) {
            return;
        }

        InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) event.getInventory().getHolder();
        InventoryUI ui = inventoryUIHolder.getInventoryUI();

        if ( ui.isCanClose() ) {

            ui.onTryInventoryClose(event);

        } else {

            if ( !ui.isClosed() ) {

                ui.setClosed(true);
                ui.onInventoryClose(event);

            }

        }

    }

    public void run() {

        new BukkitRunnable() {

            @Override
            public void run() {

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        for ( Player player : Bukkit.getOnlinePlayers() ) {

                            InventoryView invView = player.getOpenInventory();

                            if ( invView == null ) {
                                continue;
                            }

                            Inventory inv = invView.getTopInventory();

                            if ( inv == null || inv.getHolder() == null || !( inv.getHolder() instanceof InventoryUI.InventoryUIHolder ) ) {
                                continue;
                            }

                            InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) inv.getHolder();
                            InventoryUI ui = inventoryUIHolder.getInventoryUI();

                            if ( ui.isAutoRefresh() ) {

                                ui.refresh();

                            }

                        }

                    }

                }.runTask(AlarmStarSystem.getInstance());

            }
        }.runTaskTimerAsynchronously(AlarmStarSystem.getInstance(), 50, 20);

    }

}
