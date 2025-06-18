package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.item;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Defines an item that is clickable in an InvUI.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 2023-08-20 下午 09:04
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public interface IInvClickableItem {

    /**
     * Called when the item is clicked.
     * @param event The event.
     * @return Return true for cancelled the event, but still influenced by global invOption: allowPutItems
     */
    boolean onClick(InventoryClickEvent event);

    /**
     * Get the item stack of this clickable item.
     * @return The current item stack.
     */
    @NotNull ItemStack getItemStack();

    /**
     * Set the item stack of this clickable item.
     * @param itemStack The item stack.
     * @return Return true if set successfully.
     * Here are some reason for return false:
     * <ul>
     *     <li>The item stack is null.</li>
     *     <li>The item stack is air.</li>
     *     <li>The item stack is not valid.</li>
     *     <li>The item stack is not a valid item.</li>
     *     <li>The item stack is not a valid item stack.</li>
     *     <li>The item stack is not a valid item meta.</li>
     *     <li>The item stack may occur problems</li>
     *     <li>Other plugins deny this action(BETA todo)</li>
     * </ul>
     */
    boolean setItemStack(@NotNull ItemStack itemStack);

    /**
     * Get the original item stack of this clickable item.
     * @return The original item stack.
     */
    ItemStack getOriginalItemStack();

}
