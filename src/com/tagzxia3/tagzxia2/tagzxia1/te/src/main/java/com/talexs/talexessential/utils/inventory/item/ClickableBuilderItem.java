package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.IInvBuilderItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Define a more flexible way to implements IInvClickableItem.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:39
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public abstract class ClickableBuilderItem implements IInvBuilderItem {

    private final ItemBuilder origin;

    private ItemBuilder builder;

    public ClickableBuilderItem(@NotNull ItemBuilder builder) {
        this.builder = this.origin = builder;
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return builder;
    }

    @Override
    public @NotNull ItemStack getItemStack() {
        return builder.toItemStack();
    }

    @Override
    public boolean setItemStack(@NotNull ItemStack itemStack) {
        if ( itemStack == builder.toItemStack() ) return false;

        this.builder = new ItemBuilder(itemStack);

        return true;
    }

    @Override
    public ItemStack getOriginalItemStack() {
        return getOriginalBuilder().toItemStack();
    }

    @Override
    public ItemBuilder getOriginalBuilder() {
        return origin;
    }
}
