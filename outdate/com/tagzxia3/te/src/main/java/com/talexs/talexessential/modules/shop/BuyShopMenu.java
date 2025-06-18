package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.shop;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.ShopSlot;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.shop.buy.BuyOrder;
import com.talexs.talexessential.modules.shop.sell.SellCategory;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BuyShopMenu extends MenuBasic {

    private int startIndex;

    private SellCategory category;

    private BuyOrder order;

    public static List<com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.ShopSlot> shopSlots = new ArrayList<>();

    public BuyShopMenu(Player player, int startIndex, @NotNull SellCategory category, @NotNull BuyOrder order) {
        super(player, "&e出售商店", 6);

        this.startIndex = Math.max(startIndex, 0);
        this.category = category;
        this.order = order;
    }

    public BuyShopMenu(Player player) {
        this(player, 0, SellCategory.ALL, BuyOrder.DEFAULT);
    }

    @Override
    public void SetupForPlayer(Player player) {
        PlayerData pd = PlayerData.g(player);
        PlayerUser user = new PlayerUser(player);

        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
//        if ( rankLevel < 1 ) {
//            user.closeInventory().errorActionBar("请提升您的 通行等级 以打开出售商店！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
//            return;
//        }
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, .1f, .25f);
        new InventoryPainter(this).drawFull().drawFull();

        if ( shopSlots == null || shopSlots.isEmpty()) return;

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        // 使用log函数降低税收，等级越高降低税收越少 最低税收0.5(最高等级20)
        double taxPer = 0.3 - Math.log(rankLevel + 1) / 10;

        List<com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.ShopSlot> filtered = new ArrayList<>(shopSlots)
                .stream().filter(shopSlot -> this.category == SellCategory.ALL || shopSlot.getSellCategory() == this.category)
                .sorted((o1, o2) -> {
                    if ( order == BuyOrder.DEFAULT ) return 0;
                    else if ( order == BuyOrder.MONEY_UP ) return (int) (o1.getOriginalPrice() - o2.getOriginalPrice());
                    else return (int) (o2.getOriginalPrice() - o1.getOriginalPrice());
                }).collect(Collectors.toList());

        int index = -1, dispaly = 0;
        int slot = 10;

        double playerMoney = TalexEssential.getInstance().getEcon().getBalance(player);

        for (ShopSlot shopSlot : filtered) {
            index += 1;
            if ( index < startIndex ) continue;
            if ( slot >= 44 ) break;

            double price = shopSlot.getOriginalPrice();

            // 格式化
            String finalPrice = String.format("%.2f", price);
            String finalTax = String.format("%.2f", taxPer * 100);
            Material material = shopSlot.getMaterial();

            int maxAmo = (int) (playerMoney / (price * (1 + taxPer)));

            dispaly += 1;
            inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    ItemBuilder ib = new ItemBuilder(material)
                            .setLore(
                                    "&8#" + shopSlot.getSellCategory().getName(),
                                    "",
                                    "&8| &e购买价格的 &e" + finalTax + "% &e会用作税收!",
                                    "&8| &7请在购买前确保自己处于 &e安全位置&7 !",
                                    "&8| &7为了避免错误，所有物品将掉落在您脚下!",
                                    "&8| &7购买 640 个时将会打折 (0.95折)",
                                    "",
                                    "&e◆ &7购买价格: &e" + finalPrice + " &7金币",
                                    "&e◆ &7可以购买: &e" + maxAmo + " &7个",
                                    ""
                            );

                    if ( playerMoney > price ) {

                        ib.addLoreLine("&a✔ &e左/右键 &a购买 1/64 个");
                        ib.addLoreLine("&a✔ &eSHIFT + 左键 &a购买640个");
//                        ib.addLoreLine("&a✔ &eSHIFT + 右键 &a购买" + maxAmo + "个");

                    } else {

                        ib.addLoreLine("&c✘ &e你没有足够的金币 .");

                    }

                    ib.addLoreLine("");

                    return ib
                            .toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    int got = 1;

                    if ( e.isShiftClick() ) got = 640;
                    else if ( e.isRightClick() ) got = 64;

                    double originCost = price * got;
                    double tax = originCost * taxPer;

                    double costs = originCost + tax;

                    if ( got == 640 ) costs *= 0.95;

                    EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, costs);

                    if ( !economyResponse.transactionSuccess() ) {
                        user.playSound(Sound.ENTITY_VILLAGER_NO, 1, 1).errorActionBar("你没有足够的金币购买这么多！");
                        return false;
                    }

                    user
                            .dropItem(new ItemStack(material), got)
                            .playSound(Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1)
                            .playSound(Sound.ENTITY_VILLAGER_YES, 1, 1)
                            .actionBar("&a购买 " + got + " 个成功, 消耗 &e" + String.format("%.2f", costs) + " §8(税收: §7" + String.format("%.2f", tax) + " §8金币)");

                    openForPlayer(player);

                    return false;
                }
            });

            if ( (slot + 2 ) % 9 == 0 ) slot += 3;
            else slot +=1 ;

        }

        if ( startIndex > 0 ) {
            inventoryUI.setItem(47, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.ARROW).setName("&e上一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    new BuyShopMenu(player, startIndex - 28, category, order).openForPlayer(player);
                    return false;
                }
            });
        }

        int left = filtered.size() - dispaly - startIndex;
        if ( left > 0 ) {
            inventoryUI.setItem(51, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.ARROW).setName("&e下一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    new BuyShopMenu(player, startIndex + 28, category, order).openForPlayer(player);
                    return false;
                }
            });
        }

        inventoryUI.setItem(48, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                ItemBuilder ib = new ItemBuilder(Material.HOPPER)
                        .setName("&7过滤器")
                        .setSkullOwner(player.getName())
                        .setLore(
                                "",
                                "&8| &7通过过滤可以快速找到自己需要的",
                                "&8| &7或者可快速过滤自己不需要的物品",
                                "");

                for (SellCategory value : SellCategory.values()) {
                    ib.addLoreLine(((category == null || value != category) ? "&7◇ &e" : "&b◆ &a") + value.getName());
                }

                ib.addLoreLine("");
                ib.addLoreLine("&7&ki&a 点击切换.");
                ib.addLoreLine("");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                Object[] array = Arrays.stream(SellCategory.values()).toArray();
                int index = Arrays.asList(array).indexOf(category);

                if ( index + 1 >= array.length ) index = 0;
                else index += 1;

                new BuyShopMenu(player, startIndex, (SellCategory) array[index], order).open();

                player.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);

                return false;
            }
        });

        inventoryUI.setItem(50, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                ItemBuilder ib = new ItemBuilder(Material.RAIL)
                        .setName("&7排序器")
                        .setSkullOwner(player.getName())
                        .setLore(
                                "",
                                "&8| &7通过排序可以快速找到自己需要的",
                                "&8| &7或者可快速过滤自己不需要的物品",
                                "");

                for (BuyOrder value : BuyOrder.values()) {
                    ib.addLoreLine(((order == null || value != order) ? "&7◇ &e" : "&b◆ &a") + value.getName());
                }

                ib.addLoreLine("");
                ib.addLoreLine("&7&ki&a 点击切换.");
                ib.addLoreLine("");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                Object[] array = Arrays.stream(BuyOrder.values()).toArray();
                int index = Arrays.asList(array).indexOf(order);

                if ( index + 1 >= array.length ) index = 0;
                else index += 1;

                new BuyShopMenu(player, 0, category, (BuyOrder) array[index]).open();

                player.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);

                return false;
            }
        });

    }

}
