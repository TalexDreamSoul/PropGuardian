package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.base;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvOptions;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.inventory.base.InvUI;
import com.talexs.talexessential.utils.inventory.addon.PlayerInvStack;
import com.talexs.talexessential.utils.inventory.item.IInvClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/21 上午 12:07
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public interface IInvView {
    List<InvUI> getUis();

    String getDefaultTitle();

    int getRows();

    int getNowPage();

    int getOffset();

    InvOptions getInvOptions();

    @Nullable InvUI getPage(int page);

    @Nullable InvUI getCurrentPage();

    int getViewSize();

    @Nullable IInvClickableItem getItem(int page, int slot);

    @Nullable IInvClickableItem getItem(int slot);

    IInvView setItem(int x, int y, IInvClickableItem item);

    IInvView setItem(int slot, IInvClickableItem item);

    IInvView addItem(IInvClickableItem item);

    public IInvView createNewInvUI();

    IInvView removeItem(int slot);

    /**
     * Call on before invUi add to list.
     *
     * @param ui The inventory to be added
     */
    void onNewInv(InvUI ui);

    /**
     * Define an event when inventory try to close,
     * If return for true => cancelled close event
     * It will automatically reopen this, and set the
     * invOptions#closed to false (not close and open)
     * @param event The inventory close event
     * @return For true to deny close. (default for false)
     */
    boolean onCloseInventory(InventoryCloseEvent event);

    /**
     * Define an inventory that will be closed
     *
     * @param ui    The inventory to be closed
     * @param event The event that triggered the closure
     * @return Whether to close the inventory (true for access)
     */
    boolean onClose(InvUI ui, InventoryCloseEvent event);

    /**
     * Define an inventory that will be clicked
     *
     * @param event The event that triggered the click
     * @return Whether to close the inventory (true for cancelled)
     */
    boolean onClick(InventoryClickEvent event);

    /**
     * Get the player open inventory stack history whose inv
     * is corresponded with InvUI (holder instanceof TalexInvHolder)
     * The stack will auto clear when inv closed. (Optioned: default)
     * @param player Target player
     * @return The open inv stack history
     */
    @NotNull PlayerInvStack getPlayerInvStack(@NotNull Player player);

    void refresh();

    void onRefresh();

    IInvView setNowPage(int nowPage);

    IInvView setOffset(int offset);
}
