package com.tagzxia3.src.main.java.alarm.star.alarmstarsystem.inventory.menu;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.entity.ChatReq;
import alarm.star.alarmstarsystem.entity.PlayerData;
import alarm.star.alarmstarsystem.entity.TalexShop;
import alarm.star.alarmstarsystem.inventory.InventoryPainter;
import alarm.star.alarmstarsystem.inventory.MenuBasic;
import alarm.star.alarmstarsystem.utils.inventory.InventoryUI;
import alarm.star.alarmstarsystem.utils.item.ItemBuilder;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.inventory.menu.PlayerShop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ShopPutMenu extends MenuBasic {
    public ShopPutMenu(com.tagzxia.src.main.java.alarm.star.alarmstarsystem.inventory.menu.PlayerShop playerShop, Player player, int gSlot) {
        super(player, "§7[§e商店§7] §a编辑", 3);

        this.playerShop = playerShop;
        this.gSlot = gSlot;

//        super.allowPutItem(super.inventoryUI.getSymbol());
    }

    @Override
    public boolean allowPutItem(String inventorySymbol) {
        return true;
    }

    private final PlayerShop playerShop;

    private final int gSlot;

    @Override
    public void onCloseMenu(InventoryCloseEvent e) {
        ItemStack itemStack = inventoryUI.getCurrentPage().getItem(13);

        process((Player) e.getPlayer(), itemStack);
    }

    private void process(Player player, ItemStack itemStack) {
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);
        if ( itemStack == null ) {
            TalexShop.ShopGood good = playerShop.getShop().getShopGoods().get(gSlot);
            if ( good != null ) {
                playerShop.getShop().getShopGoods().remove(gSlot);
            }
            pd.actionBar("§c你取消放入物品!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

            playerShop.openForPlayer(player);
        } else {
            pd.actionBar("§a你成功放入物品!");
            player.sendMessage("§8| §e请在 §l聊天栏 §r§e中输入您需要设置物品的价格. §7(输入 §c取消 §7取消设置)");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);

            ChatReq.reqList.put(player.getName(), new ChatReq(ChatReq.ReqEnum.SHOP_PRICE, player) {
                @Override
                public void onChatInput(AsyncPlayerChatEvent event) {
                    if ( event.getMessage().equalsIgnoreCase("取消") ) {
                        TalexShop.ShopGood good = playerShop.getShop().getShopGoods().get(gSlot);

                        if ( good != null ) {
                            pd.actionBar("§c你取消输入价格, 将自动设置为之前的价格: " + good.getPrice());
                            player.chat(good.getPrice() + "");
                            return;
                        }

                        pd.actionBar("§c你取消输入价格!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }

                    String msg = event.getMessage();

                    try {
                        double price = Double.parseDouble(msg);
                        TalexShop shop = playerShop.getShop();
                        shop.getShopGoods().put(gSlot, new TalexShop.ShopGood(shop, itemStack, price, itemStack.getAmount()));

                        ChatReq.reqList.remove(player.getName());
                        playerShop.openForPlayer(player, true);

                        pd.actionBar("§a你成功设置价格为 §e" + price + " §a的物品!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    } catch (NumberFormatException e) {
                        pd.actionBar("§c你输入的价格不合法, 请重新输入! §7(输入 §c取消 §7取消设置)");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    }
                }
            });
        }
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this) {
            @Override
            public InventoryUI.ClickableItem onDrawFull(int slot) {

                return getItem(player, slot);
            }
        }.drawFull().drawBorder();

        TalexShop shop = playerShop.getShop();

        TalexShop.ShopGood good = shop.getShopGoods().get(gSlot);

        if ( good != null ) {
            inventoryUI.setItem(13, new InventoryUI.AbstractClickableItem(good.getGoodItem()) {
                @Override
                public boolean onClick(InventoryClickEvent e) {
                    return false;
                }
            });
        }

    }

    private InventoryUI.ClickableItem getItem(Player player, int slot) {
        return new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                if ( slot == 13 )
                    return null;//new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)4).setName("§e点击设置物品.").toItemStack();;
                return new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§e-").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
//                if ( slot != 13 ) return false;
//
//                ItemStack stack = e.getCursor().clone();
//
//                InventoryUI.ClickableItem item = getItem(player, slot);
//                item.setItemStack(stack.clone());
//                inventoryUI.setItem(slot, item);
//                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
//
//                e.setCursor(null);
//
//                e.getWhoClicked().closeInventory();
//
//                process((Player) e.getClickedInventory(), stack);

                return true;
            }
        };
    }
}
