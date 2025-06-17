package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.space.pickaxe;

import com.talexs.soultech.items.space.BaseSpaceItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BaseDiggerPickaxe extends BaseSpaceItem {
    public BaseDiggerPickaxe(@NotNull String ID, @NotNull ItemStack stack) {
        super("digger_pickaxe_" + ID, stack);
    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {
        return super.useItemBreakBlock(playerData, event);
    }
}
