package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.EmptyClickableBuilderItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Define a clickable builder item with cancelled click event.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 11:00
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public class EmptyCancelledClickableBuilderItem extends EmptyClickableBuilderItem {

    public EmptyCancelledClickableBuilderItem(@NotNull ItemBuilder builder) {
        super(builder);
    }

    @Override
    public boolean onClick(InventoryClickEvent event) {
        return true;
    }
}
