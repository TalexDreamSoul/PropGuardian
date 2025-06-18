package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.shop;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.shop.ShopSlot;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.shop.sell.SellCategory;
import com.talexs.talexessential.modules.shop.sell.SellOrder;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.player.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SellShopMenu extends MenuBasic {

    private int startIndex;

    private SellCategory category;

    private SellOrder order;

    private static int DAY_SELL_ORIGIN_LIMIT = 256;

    public static List<ShopSlot> shopSlots = new ArrayList<>();

    public SellShopMenu(Player player, int startIndex, @NotNull SellCategory category, @NotNull SellOrder order) {
        super(player, "&e收购商店", 6);

        this.startIndex = Math.max(startIndex, 0);
        this.category = category;
        this.order = order;
    }

    public SellShopMenu(Player player) {
        this(player, 0, SellCategory.ALL, SellOrder.DEFAULT);
    }

    @Override
    public void SetupForPlayer(Player player) {
        PlayerData pd = PlayerData.g(player);
        PlayerUser user = new PlayerUser(player);

        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
//        if ( rankLevel < 3 ) {
//            user.closeInventory().errorActionBar("请提升您的 通行等级 以打开收购商店！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
//            return;
//        }

        if ( player.getInventory().getItemInOffHand().getType() != Material.AIR ) {
            user.closeInventory().errorActionBar("要出售物品，你的副手不能有任何物品！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
            return;
        }

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, .1f, .25f);

        int DAY_SELL_LIMIT = DAY_SELL_ORIGIN_LIMIT;

        DAY_SELL_LIMIT = (int) Math.pow(1.4, rankLevel) * DAY_SELL_LIMIT;

        new InventoryPainter(this).drawFull().drawFull();
        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        if ( shopSlots == null || shopSlots.isEmpty()) return;

        final int[] index = {0, 0};
        final int[] slot = {10};

        String today = Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int finalDAY_SELL_LIMIT = DAY_SELL_LIMIT;
        new ArrayList<>(shopSlots).stream().sorted((o1, o2) -> {
            if ( order == SellOrder.DEFAULT ) return 0;
            else if ( order == SellOrder.MONEY_UP ) return (int) (o1.getOriginalPrice() - o2.getOriginalPrice());
            else return (int) (o2.getOriginalPrice() - o1.getOriginalPrice());
        }).forEach(shopSlot -> {
            if ( slot[0] >= 44 ) return;

            String path = "Shop.Sells." + shopSlot.getMaterial().name().toUpperCase() + "." + today;

            if ( index[0] < startIndex || (this.category != SellCategory.ALL && shopSlot.getSellCategory() != this.category) ) {
                index[0] += 1;
                return;
            }

            index[1] += 1;

            int anInt = pd.getInfo().getInt(path, 0);
            double originalPrice = shopSlot.getOriginalPrice();
//            double price = originalPrice;
            double price = shopSlot.calcPrice(anInt, finalDAY_SELL_LIMIT);

            // 格式化
            String finalPrice = String.format("%.2f", price);
            Material material = shopSlot.getMaterial();

            inventoryUI.setItem(slot[0], new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    String text;

                    if ( price != originalPrice ) {
                        text = "&b" + finalPrice + "&8(&m" + String.format("%.2f", originalPrice) + "&8)";
                    } else text = finalPrice;

                    return new ItemBuilder(material)
                            .setLore(
                                    "&8#" + shopSlot.getSellCategory().getName(),
                                    "",
                                    "&8| &e出售价格的 &c1% &e会用作税收!",
                                    "&8| &7每日出售数量达到 &c" + finalDAY_SELL_LIMIT,
                                    "&8| &7后出售价格降低50%",
                                    "&8| &7公式：(50% + (超出部分 % 1000) 10%",
                                    "&8| &7单价低于 &c1.00 &7的商品无此限制",
                                    "&8| &7最低价格不会低于原价的 &e30%&7.",
                                    "",
                                    "&e◆ &7收购价格: &e" + text + " &7金币",
                                    "&e◆ &7今日已售出 &c" + anInt + " &7个",
                                    "",
                                    "&e左/右键 &a出售 1/64 个",
                                    "&eSHIFT + 左键 &a出售背包所有",
                                    ""
                            )
                            .toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    if ( player.getInventory().getItemInOffHand().getType() != Material.AIR ) {
                        user.closeInventory().errorActionBar("要出售物品，你的副手不能有任何物品！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
                        return false;
                    }

                    int absorb = 1;

                    if ( e.isShiftClick() ) absorb = -1;
                    else if ( e.isRightClick() ) absorb = 64;

                    Integer playerItemInInventory = InventoryUtils.getPlayerItemInInventory(player, material);
                    if ( absorb == -1 ) {
                        absorb = playerItemInInventory;
                    }

                    if ( playerItemInInventory < absorb) {
                        user
                                .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                                .playSound(Sound.ENTITY_VILLAGER_HURT, 1, 1)
                                .errorActionBar("您没有足够的物品!");
                        return false;
                    }

                    int thisAbsorb = absorb;

                    absorb = InventoryUtils.deletePlayerItem(player, material, absorb);

                    pd.getInfo().set(path, anInt + absorb);

                    double gPrice = absorb * price;
                    double tax = gPrice * 0.01;

                    TalexEssential.getInstance().getEcon().depositPlayer(player, gPrice);

                    user
                            .playSound(Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1)
                            .playSound(Sound.ENTITY_VILLAGER_YES, 1, 1)
                                    .actionBar("&a出售成功，获得 &e" + String.format("%.2f", gPrice));
//                            .actionBar("&a出售成功, 获得 &e" + String.format("%.2f", gPrice - tax) + " §8(税收: §7" + String.format("%.2f", tax) + " §8金币)");

                    openForPlayer(player);

                    return false;
                }
            });

            if ( (slot[0] + 2 ) % 9 == 0 ) slot[0] += 3;
            else slot[0] +=1 ;

            index[0] += 1;

        });

        if ( startIndex > 0 ) {
            inventoryUI.setItem(47, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.ARROW).setName("&e上一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    new SellShopMenu(player, startIndex - 28, category, order).openForPlayer(player);
                    return false;
                }
            });
        }

        if ( startIndex + 28 < shopSlots.size() - 1 ) {
            inventoryUI.setItem(51, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.ARROW).setName("&e下一页").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    new SellShopMenu(player, startIndex + 28, category, order).openForPlayer(player);
                    return false;
                }
            });
        }

        double[] worthy = calcInventoryWorth(pd, DAY_SELL_ORIGIN_LIMIT, /*finalDAY_SELL_LIMIT*/ false);

        inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.GOLD_INGOT)
                        .setName("&e出售背包物品")
//                        .setSkullOwner(player.getName())
                        .setLore(
                                "",
                                "&8| &7出售背包所有可以出售的物品",
                                "&8| &c请在点击前检测，售出后不可找回",
                                "",
                                "&e◆ &7预期收益: &e" + String.format("%.2f", worthy[0]) + " &7金币",
                                "&e◆ &7官方纳税: &e" + String.format("%.2f", worthy[1]) + " &7金币",
                                "",
                                "&7&ki&a 点击立即售出",
                                ""
                        )
                        .toItemStack()
                        ;
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( worthy[0] == 0 ) return false;

                if ( player.getInventory().getItemInOffHand().getType() != Material.AIR ) {
                    user.closeInventory().errorActionBar("要出售物品，你的副手不能有任何物品！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
                    return false;
                }

                calcInventoryWorth(pd, DAY_SELL_ORIGIN_LIMIT/*finalDAY_SELL_LIMIT*/, true);

                double income = worthy[0] - worthy[1];

                TalexEssential.getInstance().getEcon().depositPlayer(player, income);

                new PlayerUser(player)
                        .firework()
                        .playSound(Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1)
                        .playSound(Sound.ENTITY_VILLAGER_YES, 1, 1)
                        .actionBar("&a出售成功, 获得 &e" + String.format("%.2f", income) + " §8(税收: §7" + String.format("%.2f", worthy[1]) + " §8金币)");

                openForPlayer(player);

                return false;
            }
        });

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

                new SellShopMenu(player, startIndex, (SellCategory) array[index], order).open();

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

                for (SellOrder value : SellOrder.values()) {
                    ib.addLoreLine(((order == null || value != order) ? "&7◇ &e" : "&b◆ &a") + value.getName());
                }

                ib.addLoreLine("");
                ib.addLoreLine("&7&ki&a 点击切换.");
                ib.addLoreLine("");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                Object[] array = Arrays.stream(SellOrder.values()).toArray();
                int index = Arrays.asList(array).indexOf(order);

                if ( index + 1 >= array.length ) index = 0;
                else index += 1;

                new SellShopMenu(player, 0, category, (SellOrder) array[index]).open();

                player.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1);

                return false;
            }
        });

    }

    private double[] calcInventoryWorth(PlayerData pd, int DAY_SELL_LIMIT, boolean take) {

        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);
        AtomicReference<Double> totalTax = new AtomicReference<>((double) 0);

        String today = Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        shopSlots.forEach(shopSlot -> {
            try {

                String path = "Shop.Sells." + shopSlot.getMaterial().name().toUpperCase() + "." + today;

                int anInt = pd.getInfo().getInt(path, 0);

                double price = shopSlot.calcPrice(anInt, DAY_SELL_LIMIT);
                Material material = shopSlot.getMaterial();

                Integer playerItemInInventory = InventoryUtils.getPlayerItemInInventory(player, material);
                if ( playerItemInInventory < 1 ) return;

                if ( take ) {
                    InventoryUtils.deletePlayerItem(player, material, playerItemInInventory);
                    pd.getInfo().set(path, anInt + playerItemInInventory);
                }

                double gPrice = playerItemInInventory * price;
                double tax = gPrice * 0.01;

                totalPrice.set(totalPrice.get() + gPrice);
                totalTax.set(totalTax.get() + tax);

            } catch ( Exception e ) {

                e.printStackTrace();
                TalexEssential.getInstance().log("§c收购商店配置文件出错");

            }
        });

        return new double[]{totalPrice.get(), totalTax.get()};

    }
}
