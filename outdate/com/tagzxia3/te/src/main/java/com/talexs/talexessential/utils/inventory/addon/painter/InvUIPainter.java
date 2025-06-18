package com.tagzxia3.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter;
import com.talexs.talexessential.utils.inventory.LocationFloat;
import com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.item.EmptyCancelledClickableBuilderItem;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * The IInvPainter's implements that adapt TalexInvUI.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:35
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public class InvUIPainter implements com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter {

    private InvUI invUI;

    public InvUIPainter(InvUI invUI) {

        this.invUI = invUI;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawProgressBarHorizontal(int row, int maxWidth, double percent,
                                                                                                                                      LocationFloat location, IInvClickableItem finished, IInvClickableItem will) {

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

    private void drawProgressBar(int startSlot, int maxWidth, int fillAmo, IInvClickableItem finished, IInvClickableItem will) {

        for ( int i = 0; i < maxWidth; ++i ) {

            if ( i <= fillAmo ) {

                invUI.setItem(startSlot + i, finished);

            } else {

                invUI.setItem(startSlot + i, will);

            }

        }

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawArena9(int slot, IInvClickableItem item) {

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

            if ( s < 0 || s > invUI.getSize() ) {
                continue;
            }

            invUI.setItem(s, item);

        }

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawBorder() {

        int size = this.invUI.getSize();

        for ( int i = 0; i < size; ++i ) {

            if ( i < 9 || i >= size - 9 || i % 9 == 0 || ( i + 1 ) % 9 == 0 ) {

                IInvClickableItem stack = onDrawBorder(i);

                if ( stack == null ) {
                    continue;
                }

                this.invUI.setItem(i, stack);

            }

        }

        return this;

    }

    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawThese(int... slots) {

        for ( int slot : slots ) {

            IInvClickableItem stack = onDrawBorder(slot);

            if ( stack == null ) {
                continue;
            }

            this.invUI.setItem(slot, stack);

        }

        return this;

    }

    @Override
    public IInvClickableItem onDrawBorder(int slot) {

        return new EmptyCancelledClickableBuilderItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*"));

    }

    @Override
    public IInvClickableItem onDrawLine(int slot) {

        return new EmptyCancelledClickableBuilderItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#"));

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawLineRow(int row) {

        row--;

        for ( int i = row * 9 + 1; i < row * 9 + 8; ++i ) {

            IInvClickableItem stack = onDrawLine(i);

            if ( stack == null ) {
                continue;
            }

            this.invUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawLine(int slot) {

        int row = slot / 9;

        if ( slot % 9 != 0 ) {
            row++;
        }

        drawLineRow(row);

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawLine() {

        drawLineRow(1);

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawLineRowFull(int row) {

        row--;

        for ( int i = row * 9; i < row * 9 + 9; ++i ) {

            IInvClickableItem stack = onDrawLine(i);

            if ( stack == null ) {
                continue;
            }

            this.invUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawLineFull(int startSlot) {

        int row = startSlot / 9;

        if ( startSlot % 9 != 0 ) {
            row++;
        }

        drawLineRowFull(row);

        return this;

    }

    @Override
    public com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.addon.painter.IInvPainter drawFull() {

        for ( int i = 0; i < this.invUI.getSize(); ++i ) {

            IInvClickableItem stack = onDrawFull(i);

            if ( stack == null ) {
                continue;
            }

            invUI.setItem(i, stack);

        }

        return this;

    }

    @Override
    public IInvClickableItem onDrawFull(int slot) {

        return new EmptyCancelledClickableBuilderItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7#"));

    }

    @Override
    public IInvPainter drawLineFull() {

        drawLineRowFull(1);

        return this;

    }

}
