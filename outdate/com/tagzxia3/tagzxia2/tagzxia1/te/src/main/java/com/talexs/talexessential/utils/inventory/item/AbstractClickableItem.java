package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Define an item that implements only base stack actions.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:18
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public abstract class AbstractClickableItem implements IInvClickableItem {

    @Override
    public @NotNull ItemStack getItemStack() {
        return this.current;
    }

    @Override
    public boolean setItemStack(@NotNull ItemStack itemStack) {
        if ( itemStack == this.current ) return false;

        this.current = itemStack;

        return true;
    }

    @Override
    public ItemStack getOriginalItemStack() {
        return this.origin;
    }

    private final ItemStack origin;

    private ItemStack current;

    public AbstractClickableItem(@NotNull ItemStack origin) {
        this.current = this.origin = origin;
    }

}
