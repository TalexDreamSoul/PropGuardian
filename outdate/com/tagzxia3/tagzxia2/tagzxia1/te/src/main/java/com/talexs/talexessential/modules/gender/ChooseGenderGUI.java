package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.gender;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.gender.GenderType;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ChooseGenderGUI extends MenuBasic {
    public ChooseGenderGUI(Player player) {
        super(player, "&a选择性别", 6);

        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        PlayerData pd = PlayerData.g(player);
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(12, new InventoryUI.EmptyCancelledClickableItem(
                new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        String gender = pd.getInfo().getString("Manifest.Gender.Type", "UNKNOWN");
        GenderType gt = GenderType.valueOf(gender);

        int startSlot = 19;

        for ( GenderType type : GenderType.values() ) {

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD)
                            .setName("&e" + type.getDisplayName())
                            .setLore(
                                    ""
                            );

                    if ( gt == type )
                        ib.addEnchant(Enchantment.ARROW_FIRE, 1).addFlag(ItemFlag.HIDE_ENCHANTS);

                    String[] descs = type.getDescription().split("。");

                    for ( String desc : descs ) {
                        ib.addLoreLine("&8| &7" + desc + "。");
                    }

                    ib.addLoreLine("");
                    ib.addLoreLine("&8| &a点击设置性别 &" + type.getColor() + type.getDisplayName());
                    ib.addLoreLine("");
                    ib.addLoreLine("&7每次设置性别需要消耗 &e100.00 &7登记费");
                    ib.addLoreLine("");

                    return ib
                            .setSkullOwner(player.getName()).toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, 100);
                    if ( !economyResponse.transactionSuccess() ) {
                        new PlayerUser(player)
                                .playSound(Sound.ENTITY_WANDERING_TRADER_NO, 1, 1)
                                .errorActionBar("你没有足够的余额来支付！（" + economyResponse.errorMessage + "）");
                        return false;
                    }

                    pd.getInfo().set("Manifest.Gender.Type", type.name());
                    pd.getInfo().set("Manifest.Gender.Name", type.getDisplayName());
                    pd.getInfo().set("Manifest.Gender.Color", type.getColor());

                    new PlayerUser(player).infoActionBar("你的性别已经设置为 " + type.getDisplayName()).playSound(Sound.ENTITY_WANDERING_TRADER_YES, 1, 1);

                    open();

                    return false;
                }
            });

            if ( (startSlot + 2) % 9 == 0 ) startSlot += 3;
            else startSlot += 1;

        }
    }
}
