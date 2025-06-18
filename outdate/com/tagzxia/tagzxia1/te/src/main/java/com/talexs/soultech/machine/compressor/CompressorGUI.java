package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.machine.compressor;

import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorObject;
import com.talexs.soultech.machine.bsae.MachineGUI;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class CompressorGUI extends MachineGUI {

    private CompressorObject co;

    public CompressorGUI(CompressorObject co) {
        super("&b高温压缩机");

        this.co = co;
    }

    public void load() {

        Inventory inventory = inventoryUI.getCurrentPage();

        int slot = 10;
        for ( int i = 0; i < 20; ++i ) {
            ItemStack item = co.getRecipeItems().get(i);

            if ( item != null )
                inventory.setItem(slot, item);

            slot++;
            if ( (slot + 3) % 9 == 0 ) slot += 4;
        }

        slot = 16;
        for ( int i = 0; i < 4; ++i ) {
            ItemStack item = co.getLoadItems().get(i);

            if ( item != null )
                inventory.setItem(slot, item);

            slot += 9;
        }

    }

    public void save() {

        Inventory inventory = inventoryUI.getCurrentPage();

        int slot = 10;
        for ( int i = 0; i < 20; ++i ) {
            ItemStack item = inventory.getItem(slot);

            if ( co.getRecipeItems().size() <= i )
                co.getRecipeItems().add(item);
            else co.getRecipeItems().set(i, item);

            slot++;
            if ( (slot + 3) % 9 == 0 ) slot += 4;
        }

        slot = 16;
        for ( int i = 0; i < 4; ++i ) {
            ItemStack item = inventory.getItem(slot);

            if ( co.getLoadItems().size() <= i )
                co.getLoadItems().add(item);
            else co.getLoadItems().set(i, item);

            slot += 9;
        }

    }

//    @Override
//    public void onCloseMenu(InventoryCloseEvent e) {
//        save();
//    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawBorder().drawThese(15, 24, 33, 42);

        inventoryUI.setItem(48, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE)
                        .setName("&a合成")
                        .setLore(
                                "",
                                "&8| &e压缩压力压强",
                                "&8| &7无尽必不可少",
                                "&8| &7层层挤压能力",
                                "&8| &7现在就大不同",
                                "",
                                "&8| &e点击合成 ...",
                                ""
                        )
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .addEnchant(Enchantment.DURABILITY, 1)
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                return true;
            }
        });
    }
}
