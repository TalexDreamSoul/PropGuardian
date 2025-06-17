package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.inventory.addon;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * Define a stack that accumulates player's inv open history.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/21 上午 10:10
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class PlayerInvStack {

    private final Player player;

    /**
     * Define the history stack whether it will auto cleared
     * <p>If it is false, attention stack overflow and memory run out!
     * You should extend this stack, and manage stack history.</p>
     */
    @Setter
    @Getter
    private boolean autoClearWhenClosed = true;

    private final Stack<Inventory> stackHistory = new Stack<>();

    public PlayerInvStack(@NotNull Player player) {
        this.player = player;
    }

    public void addStackHistory(Inventory inv) {
//        int size = stackHistory.size();
        this.stackHistory.add(inv);
    }

    public void clear() {
        this.stackHistory.clear();
    }

}
