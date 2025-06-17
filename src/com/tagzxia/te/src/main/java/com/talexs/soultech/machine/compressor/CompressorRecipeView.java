package com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor;

import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CompressorRecipeView extends MenuBasic {

    private CompressorRecipe cr;

    private IndicateBook gb;

    public CompressorRecipeView(Player player, CompressorRecipe cr, IndicateBook gb) {
        super(player, "&b高温压缩机 &8> &r" + cr.getDisplayItem().getItemMeta().getDisplayName(), 6);

        this.cr = cr;
        this.gb = gb;
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawBorder().drawThese(15, 24, 33, 42);

        int i = 10;
        for (TalexItem item : cr.getRequiredList() ) {
            if ( item != null )
                inventoryUI.setItem(i, new InventoryUI.EmptyCancelledClickableItem(item.getItemBuilder().toItemStack()));

            i++;
            if ( (i + 3) % 9 == 0 ) i += 4;
        }

        int j = 16;
        for ( TalexItem item : cr.getLoadRequiredList() ) {
            if ( item != null )
                inventoryUI.setItem(j, new InventoryUI.EmptyCancelledClickableItem(item.getItemBuilder().toItemStack()));

            j += 9;
        }

        inventoryUI.setItem(48, new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(cr.getExport().clone())
                .addLoreLine("")
                .addLoreLine("&8| &7冶炼时间: &e" + cr.getProcessTime() + " 秒")
                .addLoreLine("&8| &7冶炼温度: &e" + cr.getTemperature() + " ℃")
                .addLoreLine("&8| &7冶炼压强: &e" + cr.getPressure() + " MPa")
                .addLoreLine("")
                .toItemStack()));

        inventoryUI.setItem(52, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ARROW)
                        .setName("&f返回")
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                gb.lastLevel().openForPlayer(player, true);
                return false;
            }
        });

    }
}
