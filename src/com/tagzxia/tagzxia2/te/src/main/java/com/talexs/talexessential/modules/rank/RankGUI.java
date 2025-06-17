package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.rank;

import cn.hutool.core.convert.Convert;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.rank.RankLevel;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.player.InventoryUtils;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RankGUI extends MenuBasic {
    public RankGUI(Player player) {
        super(player, "&a通行等级", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);
        inventoryUI.setItem(12, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        PlayerData pd = PlayerData.g(player);

        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
        Object[] array = Arrays.stream(RankLevel.values()).toArray();

        int startSlot = 20;

        for ( int i = 0; i < array.length; ++i ) {
            RankLevel rank = (RankLevel) array[i];

            if ( i <= rankLevel ) {
                int finalI = i;
                inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                    @Override
                    public ItemStack getItemStack() {

                        ItemBuilder ib = new ItemBuilder(Material.BOOK).setName("§a" + rank.name())
                                .addEnchant(Enchantment.ARROW_FIRE, 1).addFlag(ItemFlag.HIDE_ENCHANTS)
                                .setLore(
                                        "§8#" + finalI,
                                        "",
                                        "&8| &e通行等级 &7是你的身份象征",
                                        "&8| &7其会影响到吃穿住行各方各面",
                                        "",
                                        "&8| &7住宿: &b影响领域设置、规模、图标等",
                                        "&8| &7吃穿：&b影响装备耐久、附魔、食物加成等",
                                        "&8| &7出行: &b影响跑图规模、随机传送、出行便捷等",
                                        "&8| &7购物: &b影响商店种类、每日限售、抢购、拍卖等",
                                        "",
                                        "&8| &7金币: §e" + Convert.numberToSimple(rank.getMoney()) + " &8(" + rank.getMoney() + ")",
                                        "§8| &7消耗: ");

                        int x = 0;
                        for ( String cost : rank.getCosts() ) {

                            String[] split = cost.split(":");
                            ItemStack stack = new ItemBuilder(Material.valueOf(split[0].toUpperCase())).setAmount(Integer.parseInt(split[1])).toItemStack();

                            ib.addLoreLine("&a✔ &b" + rank.getTranslations().get(x) + " x" + stack.getAmount() + " &8已缴纳");

                            x += 1;
                        }

                        ib.addLoreLine("");

                        return ib.toItemStack();
                    }

                    @Override
                    public boolean onClick(InventoryClickEvent e) {
                        return false;
                    }
                });
            }

            else {

                break;

            }

            if ( (startSlot + 3) % 9 == 0 ) startSlot += 5;
            else startSlot += 1;

        }

        if ( rankLevel + 1 < array.length) {
            RankLevel nextRank = (RankLevel) array[rankLevel + 1];

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {

                    ItemBuilder ib = new ItemBuilder(Material.KNOWLEDGE_BOOK).setName("§a" + nextRank.name()).setLore(
                            "§8#" + (rankLevel + 1),
                            "",
                            "&8| &7金币: §e" + Convert.numberToSimple(nextRank.getMoney()) + " &8(" + nextRank.getMoney() + ")",
                            "§8| &7消耗: ");

                    int x = 0;
                    for ( String cost : nextRank.getCosts() ) {

                        String[] split = cost.split(":");
                        ItemStack stack = new ItemBuilder(Material.valueOf(split[0].toUpperCase())).setAmount(Integer.parseInt(split[1])).toItemStack();

                        int amo = InventoryUtils.getPlayerItemInInventory(player, stack.getType());
                        if ( amo < stack.getAmount() ) {
                            ib.addLoreLine("&8- &c✘ &e" + amo + "/" + stack.getAmount() + " " + nextRank.getTranslations().get(x));
                        } else {
                            ib.addLoreLine("&8- &a✔ &b" + amo + "/" + stack.getAmount() + " " + nextRank.getTranslations().get(x));
                        }

                        x += 1;

                    }

                    ib.addLoreLine("");
                    ib.addLoreLine("&3◆ &2点击解锁 ...");
                    ib.addLoreLine("");

                    return ib.toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    PlayerUser user = new PlayerUser(player);

                    for ( String cost : nextRank.getCosts() ) {

                        String[] split = cost.split(":");
                        ItemStack stack = new ItemBuilder(Material.valueOf(split[0].toUpperCase())).setAmount(Integer.parseInt(split[1])).toItemStack();

                        if ( stack.getType() == Material.DRAGON_EGG ) {
                            user.playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                                    .playSound(Sound.BLOCK_VINE_BREAK, 1, 1)
                                    .actionBar("§c无法使用 不稳定材料 解锁此段位!");

                            return false;
                        }

                        int amo = InventoryUtils.getPlayerItemInInventory(player, stack.getType());
                        if ( amo < stack.getAmount() ) {

                            user.playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                                    .playSound(Sound.BLOCK_VINE_BREAK, 1, 1)
                                    .actionBar("§c你没有足够的 材料 解锁此段位!");

                            return false;
                        }

                    }

                    EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, nextRank.getMoney());

                    if ( !economyResponse.transactionSuccess() ) {

                        user.playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                                .playSound(Sound.BLOCK_VINE_BREAK, 1, 1)
                                .actionBar("§c你没有足够的 金币 解锁此段位!(" + economyResponse.errorMessage + ")");

                        return false;
                    }

                    for ( String cost : nextRank.getCosts() ) {

                        String[] split = cost.split(":");
                        ItemStack stack = new ItemBuilder(Material.valueOf(split[0].toUpperCase())).setAmount(Integer.parseInt(split[1])).toItemStack();

                        InventoryUtils.deletePlayerItem(player, stack.getType(), stack.getAmount());

                    }

                    pd.getInfo().set("Rank.Level", rankLevel + 1);
                    pd.getInfo().set("Rank.Title", nextRank.name());

                    user.playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1)
                            .playSound(Sound.BLOCK_ANVIL_USE, 1, 1)
                            .firework()
                            .closeInventory()
                            .title("", "&7您已成功解锁 &a" + nextRank.name(), 0, 60, 40)
                            .actionBar("§a解锁成功!");

                    Bukkit.broadcastMessage("§8[§b通行§e等级§8] §e" + player.getName() + " §a成功晋升 §e" + nextRank.name() + " §a段位!");

                    return false;
                }
            });
        }

    }
}
