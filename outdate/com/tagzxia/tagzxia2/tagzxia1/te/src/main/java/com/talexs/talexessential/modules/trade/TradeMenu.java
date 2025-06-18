package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.trade;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.trade.TradeObject;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.trade.TradeType;
import com.talexs.talexessential.base.PlayerPicker;
import com.talexs.talexessential.modules.trade.sub.ITradeItem;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TradeMenu extends MenuBasic {

    private TradeObject to;

    public TradeMenu(Player player, TradeObject to) {
        super(player, "&e交易系统", 5);

        this.to = to;

//        inventoryUI.setClosed(false);
        if ( to.getTarget() != null && to.getType() != null )
            inventoryUI.setAutoRefresh(true);
    }

//    @Override
//    public void onTryCloseMenu(InventoryCloseEvent e) {
////        new PlayerUser(player).delayRun(new PlayerDataRunnable() {
////            @Override
////            public void run() {
////                player.openInventory(e.getView());
////            }
////        }, 10);
//    }

    @Override
    public boolean allowPutItem(String inventorySymbol) {
        return true;
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();

        if ( to == null || to.getType() == null ) drawCategories();
        else if ( to.getTarget() == null ) getTarget();
        else drawFrame();
    }

    static int slots[] = new int[] { 10, 11, 12, 19, 20, 21, 28, 29, 30 };

    static int targets[] = new int[] { 14, 15, 16, 23, 24, 25, 32, 33, 34 };

    private void drawFrame() {
        drawItems(slots, to.getTradesSelf());
        drawItems(targets, to.getTradesTarget());

        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ORANGE_WOOL)
                        .setName("&e确认交易")
                        .setLore(
                                "",
                                "&8| &7确认交易内容",
                                "&8| &7双方均确认的情况下交易成功",
                                "&8| &7交易税收由双方通行等级一起决定",
                                "",
                                "&8| &a点击确定 ...",
                                "",
                                "&8| 对方未确认",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                return false;
            }
        });
    }

    private void drawItems(int[] slots, List<ITradeItem> items) {
        for (int i = 0; i < slots.length; i++) {
            int slot = slots[i];

            if ( items.size() > i + 1 ) {
                inventoryUI.setItem(slot, new InventoryUI.EmptyCancelledClickableItem(items.get(i).getDisplay()));
            } else inventoryUI.setItem(slot, null);
        }
    }

    private void getTarget() {

        new PlayerPicker(player, 0, (target) -> {
            to.setTarget(target);
            open(true);
        });

    }

    private void drawCategories() {
        int startSlot = 20;

        for (TradeType value : TradeType.values()) {

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(value.getMaterial())
                            .setLore(value.getLore())
                            .setName(value.getName())
                            .toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    TradeMenu.this.to = new TradeObject()
                            .setSender(player)
                            .setType(value);
                    open();
                    return false;
                }
            });

            startSlot += 1;

        }

    }

}
