package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/**
 * @author TalexDreamSoul
 */
public class UIListener implements Listener {

    private Plugin plugin;
    HashMap<String, Long> ts = new HashMap<>(32);

    public UIListener(Plugin plugin) {

        this.plugin = plugin;

        run();

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory inv = event.getClickedInventory();

        if ( inv == null || event.getCurrentItem() == null ) {
            return;
        }

        InventoryView view = event.getView();
        Inventory topInv = view.getTopInventory();

        if ( !( topInv.getHolder() instanceof InventoryUI.InventoryUIHolder ) ) {
            return;
        }

        InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) topInv.getHolder();
        InventoryUI ui = inventoryUIHolder.getInventoryUI();

        if ( topInv != inv) {
            if ( !ui.isCouldClickPlayerInventory() )
                event.setCancelled( true );
            return;
        }

        if ( !ui.onInventoryClick(event) ) {
            return;
        }

        InventoryUI.ClickableItem item = ui.getCurrentUI().getItem(event.getSlot());

        if ( item == null ) {

            return;

        }

        Player player = (Player) event.getWhoClicked();

        if ( ts.containsKey(player.getName()) ) {

            if ( System.currentTimeMillis() - ts.get(player.getName()) < ui.getInterval() ) {

                event.setCancelled(true);
                new PlayerUser(player)
                        .playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1)
                        .playSound(Sound.BLOCK_ANCIENT_DEBRIS_PLACE, 1, .5f)
                        .errorActionBar("您的点击速度过快...");
                return;

            }

        }

        ts.put(player.getName(), System.currentTimeMillis());

        try {

            boolean a = item.onClick(event);

            if ( !ui.allowPutItem() || a ) {

                event.setCancelled(true);

            }

        } catch ( Exception e ) {
            event.setCancelled(true);

            LogUtil.log("UIListener.onClick ERROR | " + e.getMessage());
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        if ( !( event.getInventory().getHolder() instanceof InventoryUI.InventoryUIHolder ) ) {
            return;
        }

        InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) event.getInventory().getHolder();
        InventoryUI ui = inventoryUIHolder.getInventoryUI();

        if ( !ui.isCanClose() ) {

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

                for ( Player player : Bukkit.getOnlinePlayers() ) {

                    InventoryView invView = player.getOpenInventory();

                    Inventory inv = invView.getTopInventory();

                    if (inv.getHolder() == null || !(inv.getHolder() instanceof InventoryUI.InventoryUIHolder)) {
                        continue;
                    }

                    InventoryUI.InventoryUIHolder inventoryUIHolder = (InventoryUI.InventoryUIHolder) inv.getHolder();
                    InventoryUI ui = inventoryUIHolder.getInventoryUI();

                    if ( ui.isAutoRefresh() ) {

                        ui.refresh();

                    }

                }

            }
        }.runTaskTimer(plugin, 40, 5);

    }

}
