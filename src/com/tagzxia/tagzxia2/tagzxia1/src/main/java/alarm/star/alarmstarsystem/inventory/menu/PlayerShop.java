package com.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.inventory.menu;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.config.TalexShopConfig;
import alarm.star.alarmstarsystem.entity.PlayerData;
import alarm.star.alarmstarsystem.entity.TalexShop;
import alarm.star.alarmstarsystem.inventory.InventoryPainter;
import alarm.star.alarmstarsystem.inventory.MenuBasic;
import alarm.star.alarmstarsystem.utils.MathUtils;
import alarm.star.alarmstarsystem.utils.inventory.InventoryUI;
import alarm.star.alarmstarsystem.utils.item.ItemBuilder;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.inventory.menu.ShopPutMenu;
import lombok.Getter;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerShop extends MenuBasic {
    public PlayerShop(Player player, String shopOwner) {
        super(player, "§8[§6§l商店§8] §a" + shopOwner + " §7的商店", 6);

        if ( shopOwner == null )
            throw new NullPointerException("shopOwner 不能为 null!");

        this.shopOwner = shopOwner;
        this.shop = TalexShopConfig.getShop(shopOwner);

        if ( this.shop == null )
            throw new NullPointerException("shop 不能为 null!");

        super.inventoryUI.setAutoRefresh(true);

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1.25f);
    }

    private final String shopOwner;

    @Getter
    private final TalexShop shop;

    private boolean doEdit(Player player) {
        return Objects.equals(this.shopOwner, player.getName()) || player.hasPermission("alarmstarsystem.shop.edit.other");
    }

    @Override
    public void SetupForPlayer(Player player) {
        PlayerShop that = this;
        new InventoryPainter(this) {

            @Override
            public InventoryUI.ClickableItem onDrawFull(int slot) {
                return new InventoryUI.AbstractSuperClickableItem() {
                    @Override
                    public ItemStack getItemStack() {
                        return getItem(player, slot);
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e) {
                        if ( !doEdit(player) ) {
                            TalexShop.ShopGood good = shop.getShopGoods().get(slot);
                            if ( good == null ) return false;

                            EconomyResponse economyResponse = AlarmStarSystem.getEcon().withdrawPlayer(player, good.getPrice());

                            if ( !economyResponse.transactionSuccess() ) {
                                PlayerData playerData = PlayerDataConfig.getInstance().getPd(player);
                                playerData.actionBar("§c§l你没有足够的余额！");
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
                                return false;
                            }

                            shop.getShopGoods().remove(slot);
                            shop.addBalance(good.getPrice());

                            ItemStack itemStack = good.getGoodItem().clone();
                            itemStack.setAmount(good.getAmount());

                            player.getInventory().addItem(itemStack);
//                            player.getWorld().dropItem(player.getLocation(), good.getGoodItem().clone());

                            return false;
                        }

                        if ( !e.isShiftClick() ) return false;

                        if ( e.isLeftClick() ) {

                            new ShopPutMenu(that, that.player, slot).openForPlayer(player);

                        } else if ( e.isRightClick() ) {
                            TalexShop.ShopGood good = shop.getShopGoods().get(slot);

                            ItemStack itemStack = good.getGoodItem().clone();

                            shop.getShopGoods().remove(slot);
                            openForPlayer(player);
//                            player.getWorld().dropItem(player.getLocation(), good.getGoodItem().clone());
                            player.getInventory().addItem(itemStack);

                        }

                        return false;
                    }
                };
            }
        }.drawFull().drawBorder();

        String rate = AlarmStarSystem.getInstance().getConfig().getString("Settings.economy.rob", "0-0");
        PlayerData playerData = PlayerDataConfig.getInstance().getPd(player);

        inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                List<String> lore = new ArrayList<>(Arrays.asList("", "§7通过抢劫, 你可以获得商店的部分物品!", "", "§7当前商店余额： §e" + Math.round(shop.getShopBalance()), "§7当前抢劫倍率: §e" + rate, ""));
                if ( playerData.getMoneyStreams().stream().anyMatch(stream -> Objects.equals(stream.getShopOwner(), shopOwner)) ) {
                    lore.add("§e§l✔ §a您已抢劫过.");
                    lore.add("");
                    return new ItemBuilder(Material.STONE_SWORD)
                            .setName("§c抢劫")
                            .setLore(lore)
                            .toItemStack();
                }

                if ( shop.getShopBalance() > 0 ) {
                    lore.add("§7§k|§r §e左键§7抢劫");
                    lore.add("");
                    return new ItemBuilder(Material.IRON_SWORD)
                            .setName("§c抢劫")
                            .addEnchant(Enchantment.DAMAGE_ALL, 1)
                            .addFlag(ItemFlag.HIDE_ENCHANTS)
                            .setLore(lore)
                            .toItemStack();
                }

                lore.add("§c§l✘ §e无法抢劫.");
                lore.add("");

                return new ItemBuilder(Material.WOOD_SWORD)
                        .setName("§c抢劫")
                        .setLore(lore)
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( shop.getShopBalance() <= 0 ) return false;

                double robMoney = MathUtils.randomRate(rate, shop.getShopBalance());
                robMoney = Math.round(robMoney * 100) / 100.0;
                if ( robMoney > shop.getShopBalance() ) robMoney = shop.getShopBalance();

                shop.addBalance(-robMoney);

                playerData.addWarningDummy(shopOwner.equalsIgnoreCase("admin") ? 20 : 40);
                playerData.addDayMoney(new PlayerData.MoneyStream(robMoney, shopOwner));

                playerData.actionBar("§a§l抢劫成功! §7获得 §e" + robMoney + " §7金币!");
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                EconomyResponse economyResponse = AlarmStarSystem.getEcon().depositPlayer(player, robMoney);
//
//                if ( economyResponse.transactionSuccess() ) {
//                    PlayerData playerData = PlayerDataConfig.getInstance().getPd(player);
//
//                    playerData.addWarningDummy(40);
//
//                    player.sendActionBar("§a§l抢劫成功! §7获得 §e" + robMoney + " §7金币!");
//                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                } else {
//                    player.sendActionBar("§c§l抢劫失败! §7获得 §e" + robMoney + " §7金币!");
//                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1, 1);
//                }

                return false;
            }
        });
    }

    private ItemStack getItem(Player player, int slot) {
        TalexShop.ShopGood good = shop.getShopGoods().get(slot);

        if ( !doEdit(player) ) {
            if ( good != null ) {
                return new ItemBuilder(good.getGoodItem().clone())
                        .setAmount(good.getAmount())
                        .addLoreLine("")
                        .addLoreLine("§7§o价格: §e" + good.getPrice() + " §7§o金币")
                        .addLoreLine("")
                        .addLoreLine("§e左键§7购买")
                        .toItemStack();
            }
            return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)8).setName("§e-")
                    .toItemStack();
        }
        if ( good != null ) {
            return new ItemBuilder(good.getGoodItem().clone())
                    .setAmount(good.getAmount())
                    .addLoreLine("")
                    .addLoreLine("§7§o价格: §e" + good.getPrice() + " §7§o金币")
                    .addLoreLine("")
                    .addLoreLine("§eSHIFT + 左键§7编辑 §8| §eSHIFT + 右键§7删除")
                    .toItemStack();
        }
        return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)8).setName("§e-")
                .setLore("", "§7此位置待编辑商品.", "", "§8| §eSHIFT + 左键§7编辑", "")
                .toItemStack();
    }

}
