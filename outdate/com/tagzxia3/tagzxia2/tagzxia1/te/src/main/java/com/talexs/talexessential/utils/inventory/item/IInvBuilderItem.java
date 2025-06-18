package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Define an interface to build clickable item.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:40
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */

public interface IInvBuilderItem extends IInvClickableItem {

    /**
     * Get the item builder of this clickable item.
     * @return The item builder.
     */
    ItemBuilder getItemBuilder();

    /**
     * Get the item builder of this clickable item.
     * @return The itemBuilder
     */
    @Override
    @NotNull ItemStack getItemStack();

    /**
     * Set the item stack of this clickable item.
     * @param itemStack The item stack.
     * @return Return true if set successfully.
     */
    @Override
    boolean setItemStack(@NotNull ItemStack itemStack);

    /**
     * Get the original item stack of this clickable item.
     * @return The original item stack.
     */
    @Override
    ItemStack getOriginalItemStack();

    /**
     * Get the original builder of this clickable item.
     * @return The original builder.
     */
    ItemBuilder getOriginalBuilder();
}
