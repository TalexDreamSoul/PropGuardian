package com.tagzxia2.te.src.main.java.com.talexs.soultech.machine.machine_info;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MachineList extends MenuBasic {

    private final int start;

    public MachineList(Player player, int start) {

        super(player, "机器一览", 5);

        this.start = start;

    }

    @Override
    public void SetupForPlayer(Player player) {

        new InventoryPainter(this).drawFull().drawBorder();

        int startSlot = 10, i = -1;

        for ( Map.Entry<String, BaseMachine> entry : AddonHolder.getINSTANCE().get(MachineAddon.class).getMachinesClone() ) {

            ++i;
            if ( i < start ) {
                continue;
            }

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {

                @Override
                public ItemStack getItemStack() {

                    return entry.getValue().getDisplayItem();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    entry.getValue().onOpenMachineInfoViewer((Player) e.getWhoClicked());

                    return true;

                }
            });

            startSlot++;

            if ( ( startSlot + 1 ) % 9 == 0 ) {

                startSlot += 2;

            }

            if ( startSlot >= 36 ) {
                break;
            }

        }

        int size = AddonHolder.getINSTANCE().get(MachineAddon.class).getMachinesClone().size();

        int maxPage = size / 21;

        if ( size % 21 != 0 ) {
            maxPage++;
        }

        int nowPage = start / 21;

        if ( startSlot % 21 != 0 ) {
            nowPage++;
        }

        if ( nowPage == 1 && maxPage != 1 ) {

            placeNextPage(nowPage, maxPage);

        } else if ( nowPage == maxPage ) {

            placePreviousPage(nowPage, maxPage);

        } else if ( maxPage != 1 ) {

            placeNextPage(nowPage, maxPage);

            placePreviousPage(nowPage, maxPage);

        }

        inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.BOOK).setName("§e一览").setLore("", "§8> §e快速返回主菜单.", "").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                PlayerData pd = PlayerData.g(player);

                pd.getPlayerSoul().getIndicateBook()
                        .setClassifies(null)
                        .open();

                return true;

            }
        });

    }

    private void placeNextPage(int now, int max) {

        if ( now == max ) {
            return;
        }

        inventoryUI.setItem(41, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§a下一页   §8(§a" + now + "§7/§e" + max + "§8)").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new MachineList((Player) e.getWhoClicked(), start + 21).open();

                return true;

            }
        });

    }

    private void placePreviousPage(int now, int max) {

        if ( now == 1 ) {
            return;
        }

        inventoryUI.setItem(39, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 3).setName("§a上一页   §8(§a" + now + "§7/§e" + max + "§8)").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new MachineList((Player) e.getWhoClicked(), start - 21).open();

                return true;

            }
        });

    }

}
