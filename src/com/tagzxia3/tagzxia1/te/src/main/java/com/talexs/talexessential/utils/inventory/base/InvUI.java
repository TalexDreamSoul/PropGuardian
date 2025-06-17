package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.IInvView;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.TalexInvHolder;
import com.talexs.talexessential.utils.inventory.addon.painter.InvUIPainter;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

/**
 * Defines a UI class that implements wrapper bukkit inventory.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:05
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

@Getter
@Setter
@Accessors(chain = true)
public class InvUI {

    private final IInvView IInvView;

    private final IInvClickableItem[][] items;

    private String title;

    private final int rows;

    private Inventory cachedInv;

    private int curX = -1, curY;

    public InvUIPainter painter = new InvUIPainter(this);

    public int getSize() {
        return this.rows * 9;
    }

    public InvUI setTitle(String title) {
        this.title = title;

        cachedInv = null;

        getCachedInv();

        return this;
    }

    public Inventory getCachedInv() {
        if ( cachedInv != null ) return cachedInv;

        TalexInvHolder holder = new TalexInvHolder(this.IInvView);

        Inventory inventory = Bukkit.createInventory(holder, getSize(), ChatColor.translateAlternateColorCodes('&', this.title));

        for ( int y = 0; y < this.rows; y++ ) {
            for ( int x = 0; x < 9; x++ ) {
                int slot = y * 9 + x;
                IInvClickableItem item = this.items[x][y];
                if ( item != null ) {
                    inventory.setItem(slot, item.getItemStack());
                }
            }
        }

        return this.cachedInv = inventory;
    }

    public InvUI(IInvView IInvView, String title, int rows) {
        this.IInvView = IInvView;
        this.title = title;
        this.rows = rows;
        this.items = new IInvClickableItem[9][rows];
    }

    public InvUI setItem(int x, int y, IInvClickableItem item) {

        this.items[x][y] = item;

        if ( this.cachedInv != null ) {
            this.cachedInv.setItem(transformSlot(x, y), item == null ? null : item.getItemStack());
        }

        return this;
    }

    public InvUI setItem(int slot, IInvClickableItem item) {

        int y = Math.abs(slot / 9);
        int x = -( y * 9 - slot );

        return this.setItem(x, y, item);
    }

    public @Nullable IInvClickableItem getItem(int slot) {
        int y = Math.abs(slot / 9);
        int x = -( y * 9 - slot );
        if ( this.items.length <= x ) {
            return null;
        }
        IInvClickableItem[] items = this.items[x];
        if ( items.length <= y ) {
            return null;
        }
        return items[y];
    }

    private int transformSlot(int x, int y) {
        return (y * 9) + x;
    }

}
