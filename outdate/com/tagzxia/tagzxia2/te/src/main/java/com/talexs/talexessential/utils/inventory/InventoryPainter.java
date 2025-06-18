package com.tagzxia2.te.src.main.java.com.talexs.talexessential.utils.inventory;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.IInventoryPainter;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.LocationFloat;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TalexDreamSoul
 * @Description: 打印类的具体实现
 */
public class InventoryPainter implements IInventoryPainter {

    private com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI inventoryUI;

    public InventoryPainter(com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI inventoryUI) {

        this.inventoryUI = inventoryUI;

    }

    public InventoryPainter(MenuBasic menuBasic) {

        this(menuBasic.inventoryUI);

    }

    @Override
    public InventoryPainter drawProgressBarHorizontal(int row, int maxWidth, double percent,
                                                      com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.LocationFloat location, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem finished, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem will) {

        int fillAmo = (int) ( percent * maxWidth ) - 1;

        int startSlot = 9 * row;

        if ( location == com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.LocationFloat.FLOAT_LEFT ) {

            drawProgressBar(startSlot, maxWidth, fillAmo, finished, will);

        } else {

            int startAddonSlot = ( 9 - maxWidth );

            if ( location == LocationFloat.FLOAT_CENTER ) {

                startAddonSlot >>= 1;

            }

            drawProgressBar(startSlot + startAddonSlot, maxWidth, fillAmo, finished, will);

        }

        return this;

    }

    private void drawProgressBar(int startSlot, int maxWidth, int fillAmo, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem finished, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem will) {

        for ( int i = 0; i < maxWidth; ++i ) {

            if ( i <= fillAmo ) {

                inventoryUI.setItem(startSlot + i, finished);

            } else {

                inventoryUI.setItem(startSlot + i, will);

            }

        }

    }

    @Override
    public InventoryPainter drawArena9(int slot, com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem item) {

        Set<Integer> slots = new HashSet<Integer>();

        slots.add(slot - 10);
        slots.add(slot - 9);
        slots.add(slot - 8);
        slots.add(slot - 1);
        slots.add(slot + 1);
        slots.add(slot + 8);
        slots.add(slot + 9);
        slots.add(slot + 10);

        for ( int s : slots ) {

            if ( s < 0 || s > inventoryUI.getSize() ) {
                continue;
            }

            inventoryUI.setItem(s, item);

        }

        return this;

    }

    @Override
    public InventoryPainter drawBorder() {

        int size = this.inventoryUI.getSize();

        for ( int i = 0; i < size; ++i ) {

            if ( i < 9 || i >= size - 9 || i % 9 == 0 || ( i + 1 ) % 9 == 0 ) {

                com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem stack = onDrawBorder(i);

                if ( stack == null ) {
                    continue;
                }

                this.inventoryUI.setItem(i, stack);

            }

        }

        return this;

    }

    public InventoryPainter drawThese(int... slots) {

            for ( int slot : slots ) {

                com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem stack = onDrawBorder(slot);

                if ( stack == null ) {
                    continue;
                }

                this.inventoryUI.setItem(slot, stack);

            }

            return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawBorder(int slot) {

        return new com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack());

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawLine(int slot) {

        return new com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#").toItemStack());

    }

    @Override
    public InventoryPainter drawLineRow(int row) {

        row--;

        for ( int i = row * 9 + 1; i < row * 9 + 8; ++i ) {

            com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem stack = onDrawLine(i);

            if ( stack == null ) {
                continue;
            }

            this.inventoryUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public InventoryPainter drawLine(int slot) {

        int row = slot / 9;

        if ( slot % 9 != 0 ) {
            row++;
        }

        drawLineRow(row);

        return this;

    }

    @Override
    public InventoryPainter drawLine() {

        drawLineRow(1);

        return this;

    }

    @Override
    public InventoryPainter drawLineRowFull(int row) {

        row--;

        for ( int i = row * 9; i < row * 9 + 9; ++i ) {

            com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem stack = onDrawLine(i);

            if ( stack == null ) {
                continue;
            }

            this.inventoryUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public InventoryPainter drawLineFull(int startSlot) {

        int row = startSlot / 9;

        if ( startSlot % 9 != 0 ) {
            row++;
        }

        drawLineRowFull(row);

        return this;

    }

    @Override
    public InventoryPainter drawFull() {

        for ( int i = 0; i < this.inventoryUI.getSize(); ++i ) {

            com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem stack = onDrawFull(i);

            if ( stack == null ) {
                continue;
            }

            inventoryUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.InventoryUI.ClickableItem onDrawFull(int slot) {

        return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#").toItemStack());

    }

    @Override
    public InventoryPainter drawLineFull() {

        drawLineRowFull(1);

        return this;

    }

}
