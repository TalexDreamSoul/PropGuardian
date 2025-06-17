package com.tagzxia3.tagzxia2.te.src.main.resources.de.soultech.inventory.guider;

import com.tagzxia.te.src.main.resources.de.soultech.inventory.guider.BaseGuider;
import com.tagzxia.te.src.main.resources.de.soultech.inventory.guider.GuiderBook;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.entity.PlayerDataRunnable;
import pubsher.talexsoultech.inventory.InventoryPainter;
import pubsher.talexsoultech.talex.items.GuiderBookItem;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.inventory.InventoryUI;
import pubsher.talexsoultech.utils.item.ItemBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstGuider extends BaseGuider {


    public FirstGuider(PlayerData playerData) {

        super(playerData, "引导", 6);

    }

    @Override
    public boolean allowPutItem(String inventorySymbol) {

        return false;

    }

    @Override
    public void onCloseMenu(InventoryCloseEvent e) {

    }

    @Override
    public void onTryCloseMenu(InventoryCloseEvent e) {

    }

    @Override
    public void SetupForPlayer(Player player, PlayerData playerData) {

        if ( !playerData.isFirstUse() ) {
            playerData.delayRun(new PlayerDataRunnable() {

                @Override
                public void run() {

                    new GuiderBook(playerData).open();

                }

            }, 20);
        }

        new InventoryPainter(this).drawFull();

        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public boolean onClick(InventoryClickEvent e) {

                for ( ItemStack stack : new ArrayList<>(Arrays.asList(player.getInventory().getContents())) ) {

                    if ( stack == null ) {
                        continue;
                    }

                    if ( NBTsUtil.hasTag(stack, "talex_soul_tc") && "guide".equals(NBTsUtil.getTag(stack, "talex_soul_tc")) ) {

                        player.getInventory().remove(stack);

                    }

                }

                playerData.addProperty("installed", true);
                new GuiderBookItem(playerData);

                playerData.closeInventory().title("§5§l灵魂§b科技", "§a新的迷航 启动了!", 5, 15, 5, 12)
                        .delayRun(new PlayerDataRunnable() {

                            @Override
                            public void run() {

                                new GuiderBook(playerData).open();

                            }

                        }, 12);

                return true;

            }

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.BOOK)

                        .setName("§a万物之启")
                        .setLore("", "§e新的迷航即将启动...", "§e因为未知,所以恐惧.", "", "§8> §7点击启动 §5灵魂科技")

                        .toItemStack();

            }

        });


    }

    public void onlyOpenForPlayer(PlayerData playerData) {

        if ( !playerData.isFirstUse() ) {
            new GuiderBook(playerData).open();
        }

    }

}
