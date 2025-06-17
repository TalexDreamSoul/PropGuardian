package com.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.inventory;

import alarm.star.alarmstarsystem.enums.LocationFloat;
import alarm.star.alarmstarsystem.utils.inventory.InventoryUI;
import alarm.star.alarmstarsystem.utils.item.ItemBuilder;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.inventory.IInventoryPainter;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.inventory.MenuBasic;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TalexDreamSoul
 * @Description: 打印类的具体实现
 */
public class InventoryPainter implements IInventoryPainter {

    private InventoryUI inventoryUI;

    public InventoryPainter(InventoryUI inventoryUI) {

        this.inventoryUI = inventoryUI;

    }

    public InventoryPainter(MenuBasic menuBasic) {

        this(menuBasic.inventoryUI);

    }

    @Override
    public InventoryPainter drawProgressBarHorizontal(int row, int maxWidth, double percent,
                                                      LocationFloat location, InventoryUI.ClickableItem finished, InventoryUI.ClickableItem will) {

        int fillAmo = (int) ( percent * maxWidth ) - 1;

        int startSlot = 9 * row;

        if ( location == LocationFloat.FLOAT_LEFT ) {

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

    private void drawProgressBar(int startSlot, int maxWidth, int fillAmo, InventoryUI.ClickableItem finished, InventoryUI.ClickableItem will) {

        for ( int i = 0; i < maxWidth; ++i ) {

            if ( i <= fillAmo ) {

                inventoryUI.setItem(startSlot + i, finished);

            } else {

                inventoryUI.setItem(startSlot + i, will);

            }

        }

    }

    @Override
    public InventoryPainter drawArena9(int slot, InventoryUI.ClickableItem item) {

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

                InventoryUI.ClickableItem stack = onDrawBorder(i);

                if ( stack == null ) {
                    continue;
                }

                this.inventoryUI.setItem(i, stack);

            }

        }

        return this;

    }

    @Override
    public InventoryUI.ClickableItem onDrawBorder(int slot) {

        return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack());

    }

    @Override
    public InventoryUI.ClickableItem onDrawLine(int slot) {

        return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#").toItemStack());

    }

    @Override
    public InventoryPainter drawLineRow(int row) {

        row--;

        for ( int i = row * 9 + 1; i < row * 9 + 8; ++i ) {

            InventoryUI.ClickableItem stack = onDrawLine(i);

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

            InventoryUI.ClickableItem stack = onDrawLine(i);

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

            InventoryUI.ClickableItem stack = onDrawFull(i);

            if ( stack == null ) {
                continue;
            }

            inventoryUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public InventoryUI.ClickableItem onDrawFull(int slot) {

        return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#").toItemStack());

    }

    @Override
    public InventoryPainter drawLineFull() {

        drawLineRowFull(1);

        return this;

    }

}
