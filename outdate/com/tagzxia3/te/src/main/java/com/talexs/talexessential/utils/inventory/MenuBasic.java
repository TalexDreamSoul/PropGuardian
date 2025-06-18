package com.tagzxia3.te.src.main.java.com.talexs.talexessential.utils.inventory;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.TalexEssential;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MenuBasic {

    public static HashMap<Player, ArrayList<MenuBasic>> uis = new HashMap<>();
    public InventoryUI inventoryUI;
    public Player player;
    protected MenuBasic mb = this;

    public MenuBasic(Player player, String title, Integer rows) {

        this(player, title, rows, null);

    }

    public MenuBasic(Player player, String title, Integer rows, String symbol) {

        this.player = player;

        MenuBasic instance = this;

        this.inventoryUI = new InventoryUI(title, rows, symbol) {

            @Override
            public boolean allowPutItem() {

                return instance.allowPutItem(symbol);

            }

            @Override
            public void refresh() {

                open();

            }

            @Override
            public boolean onInventoryClick(InventoryClickEvent e) {

                return onInvClick(e);

            }

            @Override
            public void onInventoryClose(InventoryCloseEvent e) {

                onCloseMenu(e);

            }

            @Override
            public void onTryInventoryClose(InventoryCloseEvent e) {

                onTryCloseMenu(e);

            }
        };

        if ( !uis.containsKey(uis) ) {
            uis.put(player, new ArrayList<>());
        }
        ArrayList<MenuBasic> al = uis.get(player);
        al.add(this);
        uis.put(player, al);

    }

    public MenuBasic(String title, Integer rows) {

        MenuBasic instance = this;

        this.inventoryUI = new InventoryUI(title, rows) {

            @Override
            public boolean allowPutItem() {

                return instance.allowPutItem(null);

            }

            @Override
            public void refresh() {

                open(true);

            }

            @Override
            public boolean onInventoryClick(InventoryClickEvent e) {

                return onInvClick(e);

            }

            @Override
            public void onInventoryClose(InventoryCloseEvent e) {

                onCloseMenu(e);

            }

            @Override
            public void onTryInventoryClose(InventoryCloseEvent e) {

                onTryCloseMenu(e);

            }
        };

        mb = this;
    }

    protected boolean onInvClick(InventoryClickEvent event) {

        return true;
//        Inventory clickedInventory = event.getClickedInventory();
//        if ( clickedInventory.getType() == InventoryType.PLAYER || clickedInventory.getType() == InventoryType.CRAFTING ) {
//
//            if ( !inventoryUI.isCouldClickPlayerInventory() ) event.setCancelled(true);
//
//        }

    }

    /**
     * 是否允许放置物品
     *
     * @param inventorySymbol: 给定的容器Symbol
     *
     * @return true -> 允许放置
     */
    public boolean allowPutItem(String inventorySymbol) { return false; }

    /**
     * 设置容器的刷点击间隔
     *
     * @param interval 间隔时间(单位: ms)
     */
    public void setInterval(int interval) {

        this.inventoryUI.setInterval(interval);

    }

    /**
     * 当容器被关闭时,此事件触发仅在设置 inventoryUI#canClose 为true时有效
     *
     * @param e 事件
     */
    public void onCloseMenu(InventoryCloseEvent e) {}

    /**
     * 当容器被试图关闭时,此事件触发仅在设置 inventoryUI#canClose 为false时有效
     *
     * @param e 事件
     */
    public void onTryCloseMenu(InventoryCloseEvent e) {}

    public void Setup() { SetupForPlayer(player); }

    public abstract void SetupForPlayer(Player player);

    public void openForPlayer(Player player) {

        SetupForPlayer(player);
        onlyOpenForPlayer(player);

    }

    public void open() {

        Setup();
        onlyOpen();

    }

    public void openForPlayer(Player player, boolean bypass) {

        SetupForPlayer(player);
        onlyOpenForPlayer(player, bypass);

    }

    public void open(boolean bypass) {

        Setup();
        onlyOpen(bypass);

    }

    public void onlyOpenForPlayer(Player player, boolean bypass) {

        if ( !bypass && inventoryUI.isClosed() ) {

            return;

        }

        Bukkit.getScheduler().runTask(TalexEssential.getInstance(), () -> player.openInventory(inventoryUI.getCurrentPage()));

    }

    public void onlyOpen(boolean bypass) {

        if ( player == null || !bypass && inventoryUI.isClosed() ) {

            return;

        }

        Bukkit.getScheduler().runTask(TalexEssential.getInstance(), () -> player.openInventory(inventoryUI.getCurrentPage()));
    }

    public void onlyOpen() {

        onlyOpen(false);

    }

    public void onlyOpenForPlayer(Player player) {

        onlyOpenForPlayer(player, false);

    }

    public void destroy() {

        inventoryUI = null;
        mb = null;
    }

    public void reOpen(int delay) {

        boolean a = inventoryUI.isClosed();

        player.closeInventory();
        inventoryUI.setClosed(false);
        new BukkitRunnable() {

            @Override
            public void run() {

                open(true);

            }
        }.runTaskLater(TalexEssential.getInstance(), delay);

        inventoryUI.setClosed(a);

    }

}
