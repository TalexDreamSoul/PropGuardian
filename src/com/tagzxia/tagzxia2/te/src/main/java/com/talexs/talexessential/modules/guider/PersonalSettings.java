package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.guider;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalSettings extends MenuBasic {
    public PersonalSettings(Player player) {
        super(player, "&e个人设置", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    private static Map<String, ToggleSettings> options = new HashMap<>();

    static {

        options.put("luckybag", new ToggleSettings() {
            @Override
            public ItemStack stack(PlayerData pd, boolean doEnable) {
                HeadDatabaseAPI api = new HeadDatabaseAPI();

                return new ItemBuilder(api.getItemHead("28597"))
                        .setName("&a新春礼品福利")
                        .setLore(
                                "", "&8| &a新春龙年新福利，吉祥晨曦照新辞", "&8| &a各种行为都可能掉落福袋",
                                "&8| &e是否启用福袋掉落功能", "",
                                "&8| &e福袋统计：",
                                "&8| &c龙年新辞：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "coal"),
                                "&8| &c烔欣龙融：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "copper"),
                                "&8| &c儒学龙钟：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "iron"),
                                "&8| &c金蚕龙态：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "gold"),
                                "&8| &c钻湘龙奔：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "diamond"),
                                "&8| &c黑望曜龙：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "obsidian"),
                                "&8| &c绿龙点璨：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "emerald"),
                                "&8| &c紫吉喜龙：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "amethyst"),
                                "&8| &c下届龙髋：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "nether_star"),
                                "&8| &c幽弯终龙：&a" + BaseLuckyBag.getPlayerOpenedAmo(pd, "end"),
                                "&8| &e*数据统计有系统误差，计算需要时间.",
                                "",
                                "&8| &7当前状态： " + (doEnable ? "&a已启用" : "&e已禁用"),
                                "&8| &7点击切换.", ""
                        )
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .isTrueAccessEnchant(doEnable, Enchantment.DURABILITY, 1)
                        .toItemStack();
            }
        });

    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(13, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        PlayerData pd = PlayerData.g(player);

        int startSlot = 28;

        for ( Map.Entry<String, ToggleSettings> entry : options.entrySet() ) {
            ToggleSettings value = entry.getValue();

            boolean doEnable = pd.getOptionsEnabled(entry.getKey());

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                        @Override
                        public ItemStack getItemStack() {
                            return value.stack(pd, doEnable);
                        }

                        @Override
                        public boolean onClick(InventoryClickEvent e) {

                            if ( doEnable ) {
                                value.onDisable();
                            } else value.onEnable();

                            pd.setOptionsEnabled(entry.getKey(), !doEnable);

                            open();

                            return false;
                        }
                    }
            );

            startSlot += 2;
        }
    }

    @Getter
    private abstract static class ToggleSettings {

        public abstract ItemStack stack(PlayerData pd, boolean doEnable);

        public void onEnable() {}

        public void onDisable() {}
    }
}
