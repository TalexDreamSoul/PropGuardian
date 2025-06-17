package com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Defines an item with empty click event handler, return value depends the event's param.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:15
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public class EmptyClickableItem extends AbstractClickableItem {

    public EmptyClickableItem(@NotNull ItemStack origin) {
        super(origin);
    }

    @Override
    public boolean onClick(InventoryClickEvent event) {
        return event.isCancelled();
    }
}
