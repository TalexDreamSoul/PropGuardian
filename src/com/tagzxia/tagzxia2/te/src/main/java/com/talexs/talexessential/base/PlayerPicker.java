package com.tagzxia2.te.src.main.java.com.talexs.talexessential.base;

import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class PlayerPicker extends MenuBasic {

    public interface IPickerResult {
        void onSuccess(Player player);
    }

    private IPickerResult res;

    private int start;

    public PlayerPicker(Player player, int start, IPickerResult res) {
        super(player, "&e请选择目标玩家", 6);

        this.start = start;
        this.res = res;

        inventoryUI.setAutoRefresh(true);
    }

    public Collection<? extends Player> getPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();

        int slot = -1;
        int startSlot = 10;

        for ( Player p : getPlayers() ) {
            slot += 1;
            if ( slot < start ) continue;

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.PLAYER_HEAD)
                            .setName("&e" + p.getName())
                            .setSkullOwner(p.getName()).toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    player.closeInventory();
                    res.onSuccess(p);
                    return false;
                }
            });

            if ( startSlot >= 43 ) break;
            else if ( (startSlot + 2 ) % 9 == 0 ) startSlot += 3;
            else startSlot += 1;
        }

        if ( start > 0 )
            inventoryUI.setItem(47, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&e上一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    start -= 28;
                    open();
                    return false;
                }
            });

        if ( Bukkit.getOnlinePlayers().size() - slot > 1 )
            inventoryUI.setItem(51, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("&e下一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    start += 28;
                    open();
                    return false;
                }
            });

    }
}
