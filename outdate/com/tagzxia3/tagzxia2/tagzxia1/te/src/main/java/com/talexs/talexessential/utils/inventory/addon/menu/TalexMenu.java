package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.menu;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.utils.inventory.addon.PlayerInvStack;
import com.talexs.talexessential.utils.inventory.base.IInvView;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.base.InvView;
import com.talexs.talexessential.utils.inventory.base.TalexUIListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Define a menu that wrapped normal bukkit inventory apis.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/20 下午 11:54
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public abstract class TalexMenu {

    private Plugin pluginIns = TalexEssential.getInstance();

    public Map<Player, IInvView> invViewMap = new WeakHashMap<>();

    protected TalexMenu INS = this;

    private final String title;

    private final int rows;

    public TalexMenu(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }

    public abstract void establish(IInvView view, InvUI ui, Player player);

    public TalexMenu open(@NotNull Player player) {
        return open(player, false);
    }

    public TalexMenu open(@NotNull Player player, boolean bypassClosed) {
        InvUI playerInvUi = getPlayerInvUi(player);

        establish(playerInvUi.getIInvView(), playerInvUi, player);

        return onlyOpen(player, bypassClosed);
    }

    public TalexMenu onlyOpen(@NotNull Player player) {
        return onlyOpen(player, false);
    }

    public TalexMenu onlyOpen(@NotNull Player player, boolean bypassClosed) {
        InvUI playerInvUi = getPlayerInvUi(player);
        if ( !bypassClosed && playerInvUi.getIInvView().getInvOptions().isClosed() ) return this;

        Bukkit.getScheduler().runTask(pluginIns, () -> player.openInventory(playerInvUi.getCachedInv()));

        return this;
    }

    public void destroy(Player player) {
        InvUI playerInvUi = getPlayerInvUi(player);

        playerInvUi.getIInvView().getUis().clear();

        invViewMap.remove(player);
    }

    public TalexMenu reopen(Player player, int delay) {
        InvUI playerInvUi = getPlayerInvUi(player);

        boolean a = playerInvUi.getIInvView().getInvOptions().isClosed();

        player.closeInventory();
        playerInvUi.getIInvView().getInvOptions().setClosed(false);

        new BukkitRunnable() {

            @Override
            public void run() {

                open(player, true);

            }
        }.runTaskLater(TalexEssential.getInstance(), delay);

        playerInvUi.getIInvView().getInvOptions().setClosed(a);

        return this;
    }

    private @NotNull InvUI getPlayerInvUi(Player player) {
        IInvView invView = invViewMap.get(player);

        if ( invView == null ) {
            invView = new InvView(this.title, this.rows) {

                @Override
                public void onNewInv(InvUI ui) {

                    TalexMenu.this.onNewInv(ui);
                    
                }

                public boolean onClose(InvUI ui, InventoryCloseEvent event) {
                    return TalexMenu.this.onClose(ui, event);
                }

                public boolean onClick(InventoryClickEvent event) {
                    return TalexMenu.this.onClick(event);
                }

                @Override
                public @NotNull PlayerInvStack getPlayerInvStack(@NotNull Player player) {
                    return TalexUIListener.getPlayerInvStack(player);
                }

                @Override
                public void onRefresh() {
                    TalexMenu.this.onRefresh();
                }
                
            };

            invViewMap.put(player, invView);
        }

        return Objects.requireNonNull(invView.getCurrentPage());
    }

    /**
     * Define an inventory that will be closed
     * @param ui The inventory to be closed
     * @param event The event that triggered the closure
     * @return Whether to close the inventory (true for access)
     */
    public boolean onClose(InvUI ui, InventoryCloseEvent event) {
        return true;
    }

    /**
     * Define an inventory that will be clicked
     * @param event The event that triggered the click
     * @return Whether to close the inventory (true for cancelled)
     */
    public boolean onClick(InventoryClickEvent event) {
        return event.isCancelled();
    }


    /**
     * Call on before invUi add to list.
     * @param ui The inventory to be added
     */
    public void onNewInv(InvUI ui) {

    }

    public void onRefresh() {
        // get player views that not closed
//        List<IInvView> collect = this.invViewMap.values().stream().filter(view -> !view.getInvOptions().isClosed()).collect(Collectors.toList());

    }
    
}
