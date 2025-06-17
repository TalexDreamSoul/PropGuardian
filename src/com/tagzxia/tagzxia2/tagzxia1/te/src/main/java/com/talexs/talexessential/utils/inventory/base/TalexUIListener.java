package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.IInvView;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvOptions;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.TalexInvHolder;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.utils.LogUtil;
import com.talexs.talexessential.utils.inventory.addon.PlayerInvStack;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * Define a listener that complete the InvUI logic.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/21 上午 12:34
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class TalexUIListener implements Listener {

    private Plugin plugin;

    private static Map<UUID, PlayerInvStack> playerInvStackMap = new WeakHashMap<>();

    public TalexUIListener(Plugin plugin) {

        this.plugin = plugin;

        run();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getClickedInventory();

        if ( inv == null ) {
            return;
        }

        InventoryView view = event.getView();
        Inventory topInv = view.getTopInventory();

        InventoryHolder topInvHolder = topInv.getHolder();

        if ( !( topInvHolder instanceof TalexInvHolder) ) {
            return;
        }

        TalexInvHolder talexInvHolder = (TalexInvHolder) topInvHolder;
        IInvView invView = talexInvHolder.getInv();

        if ( topInv != inv ) {
            if ( !invView.getInvOptions().isAllowClickPlayerInv() )
                event.setCancelled( true );
            return;
        }

        invView.onClick(event);

        IInvClickableItem clickableItem = invView.getItem(event.getSlot());

        if ( clickableItem == null ) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        OnlinePlayerData opd = PlayerData.g(player);

        if ( !opd.getUser().autoCoolDown("inv-click#", invView.getInvOptions().getClickInterval()) ) {

            event.setCancelled(true);
            opd.getUser()
                    .playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1)
                    .playSound(Sound.BLOCK_ANCIENT_DEBRIS_PLACE, 1, .5f)
                    .errorActionBar("您的点击速度过快!");
            return;
        }

        try {

            boolean res = clickableItem.onClick(event);

            if ( !invView.getInvOptions().isAllowPutItems() || res ) {

                event.setCancelled(true);

            }

        } catch ( Exception e ) {

            event.setCancelled(true);

            opd.getUser()
                    .sendMessage("§c§l出现未知错误，请联系管理员!")
                    .playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1)
                    .playSound(Sound.BLOCK_ANCIENT_DEBRIS_PLACE, 1, .5f)
                    .errorActionBar("出现未知错误，请联系管理员!");

            LogUtil.log("TalexUIListener.onClick ERROR | " + e.getMessage());
            e.printStackTrace();
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder invHolder = inv.getHolder();

        if ( !( invHolder instanceof TalexInvHolder) ) {
            return;
        }

        TalexInvHolder talexinvHolder = (TalexInvHolder) invHolder;
        IInvView invView = talexinvHolder.getInv();

        HumanEntity player = event.getPlayer();

        if ( !invView.getInvOptions().isAllowClose() ) {

            invView.onCloseInventory(event);

        } else if ( !invView.getInvOptions().isClosed() ) {

            // Deny to close
            if ( invView.onCloseInventory(event) ) {

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        player.openInventory(inv);

                    }
                }.runTask(this.plugin);

            } else invView.getInvOptions().setClosed(true);

        }

        PlayerInvStack invStack = this.playerInvStackMap.get(player.getUniqueId());
        if ( invStack != null && invStack.isAutoClearWhenClosed() && invView.getInvOptions().isClosed() ) {
            this.playerInvStackMap.remove(player.getUniqueId());
        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder invHolder = inv.getHolder();

        if ( !( invHolder instanceof TalexInvHolder) ) {
            return;
        }

        TalexInvHolder talexinvHolder = (TalexInvHolder) invHolder;
        IInvView invView = talexinvHolder.getInv();

        HumanEntity player = event.getPlayer();

        invView.getInvOptions().setClosed(false);

        getPlayerInvStack((Player) player).addStackHistory(inv);
    }

    public static PlayerInvStack getPlayerInvStack(Player player) {
        PlayerInvStack orDefault = playerInvStackMap.getOrDefault(player.getUniqueId(), new PlayerInvStack((Player) player));

        playerInvStackMap.put(player.getUniqueId(), orDefault);

        return orDefault;
    }


    static Map<IInvView, Integer> refreshCounter = new WeakHashMap<>();

    private void processPlayerTimerInvEvent(Player player) {
        InventoryView invView = player.getOpenInventory();
        Inventory inv = invView.getTopInventory();
        InventoryHolder invHolder = inv.getHolder();

        if (!(invHolder instanceof TalexInvHolder)) {
            return;
        }

        TalexInvHolder talexInvHolder = (TalexInvHolder) invHolder;
        IInvView tInvView = talexInvHolder.getInv();

        InvOptions invOptions = tInvView.getInvOptions();

        if ( invOptions.getAutoRefresh() < 1 ) return;

        Integer orDefault = refreshCounter.getOrDefault(tInvView, 0);
        if ( orDefault < invOptions.getAutoRefresh() ) {
            refreshCounter.put(tInvView, orDefault + 1);
            return;
        }

        tInvView.refresh();

        refreshCounter.remove(tInvView);

    }

    public void run() {

        new BukkitRunnable() {

            @Override
            public void run() {

                Bukkit.getOnlinePlayers().forEach(TalexUIListener.this::processPlayerTimerInvEvent);

            }
        }.runTaskTimer(plugin, 40, 5);

    }

}
