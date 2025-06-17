package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.guider;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.shop.BuyShopMenu;
import com.talexs.talexessential.modules.shop.SellShopMenu;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ShopSelector extends MenuBasic {
    public ShopSelector(Player player) {
        super(player, "&e选择商店", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {

        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(11, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        inventoryUI.setItem(29, new InventoryUI.AbstractClickableItem(new ItemBuilder(Material.YELLOW_SHULKER_BOX)
                .setName("&E商店 (购买)").setLore("", "&8| &7在这里，购买任意你所需", "&8| &7琳琅满目但是商品，应有尽有",
                        "&8| &7大概是服务器最全的商店", "", "&7&ki&a 点击进入 ...").toItemStack()) {
            @Override
            public boolean onClick(InventoryClickEvent e) {
                new BuyShopMenu(player).open();
                return false;
            }
        });

        inventoryUI.setItem(31, new InventoryUI.AbstractClickableItem(new ItemBuilder(Material.GREEN_SHULKER_BOX)
                .setName("&E商店 (出售)").setLore("", "&8| &7在这里，出售任意你所需", "&8| &7在这里，赚取你的第一桶金",
                        "&8| &7大概是服务器最全的商店", "", "&7&ki&a 点击进入 ...").toItemStack()) {
            @Override
            public boolean onClick(InventoryClickEvent e) {
                new SellShopMenu(player).open();
                return false;
            }
        });

        inventoryUI.setItem(33, new InventoryUI.AbstractClickableItem(new ItemBuilder(Material.PURPLE_SHULKER_BOX)
                .setName("&E商店 (神秘)").setLore("", "&8| &7最神秘的神秘商店", "&8| &7一个泥土可能价值千金",
                        "&8| &7不定时限时开放", "", "&7&ki&c 暂未开启 ...").toItemStack()) {
            @Override
            public boolean onClick(InventoryClickEvent e) {
                return false;
            }
        });

        PlayerData pd = PlayerData.g(player);
        PlayerUser user = new PlayerUser(player);

        inventoryUI.setItem(40, new InventoryUI.AbstractClickableItem(new ItemBuilder(Material.END_CRYSTAL)
                .setName("&E商店 (世界)").setLore("", "&8| &7由所有玩家共同开启的商店", "&8| &7你可以快速购买别人的商品",
                        "&8| &7请一定擦亮眼睛，谨慎购买", "", "&7&ki&c 点击进入 ...").toItemStack()) {
            @Override
            public boolean onClick(InventoryClickEvent e) {
                int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
                if ( rankLevel < 4 ) {
                    user.closeInventory().errorActionBar("请提升您的 通行等级 以打开世界商店！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1);
                    return false;
                }
                player.chat("/gmp market gui");
                return false;
            }
        });
    }
}
