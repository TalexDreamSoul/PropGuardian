package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.EmptyClickableItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Defines an item that will automatically cancel click event (deny).
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:12
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public class EmptyCancelledClickableItem extends EmptyClickableItem {

    @Override
    public boolean onClick(InventoryClickEvent event) {
        return true;
    }

    public EmptyCancelledClickableItem(@NotNull ItemStack origin) {
        super(origin);
    }

}
