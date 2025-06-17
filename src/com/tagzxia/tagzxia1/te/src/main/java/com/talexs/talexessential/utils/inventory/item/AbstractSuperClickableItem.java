package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Define an item that implements only base stack actions, but move itemstack to func.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:27
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public abstract class AbstractSuperClickableItem implements IInvClickableItem {

    private final ItemStack origin;

    private ItemStack current;

    public AbstractSuperClickableItem() {
        this.current = this.origin = getItemStack();
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        ItemStack stack = initItemStack();

        if ( !setItemStack(stack) ) throw new RuntimeException("Cannot set same item stack.");

        return stack;
    }

    public abstract @NotNull ItemStack initItemStack();

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
}
