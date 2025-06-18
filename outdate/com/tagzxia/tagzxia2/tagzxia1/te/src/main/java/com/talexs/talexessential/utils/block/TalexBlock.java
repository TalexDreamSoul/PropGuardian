package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils.block;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.block.BlockAddon;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class TalexBlock {

    @Getter
    private final ItemStack stack;

    @Getter
    private final Location loc;

    @Getter
    @Setter
    private SoulTechItem item;

    public TalexBlock(Location loc, ItemStack stack) {

        this.stack = stack;
        this.loc = loc;

        AddonHolder.getINSTANCE().get(BlockAddon.class).addBlock(loc, this);

    }

    public TalexBlock(Location loc, Block block) {

        this.stack = new ItemStack(block.getType());
        this.loc = loc;

        AddonHolder.getINSTANCE().get(BlockAddon.class).addBlock(loc, this);

    }

    public void unregisterSelf() {

        AddonHolder.getINSTANCE().get(BlockAddon.class).delBlock(loc);

    }

    public void onBlockBreak(PlayerData playerData, BlockBreakEvent event) {

        event.setCancelled(true);

        if ( item != null ) {

            if ( !item.onItemBlockBreak(playerData, this, event) ) {
                return;
            }

        }

        AddonHolder.getINSTANCE().get(MachineAddon.class).onBlockBreak(this);

        unregisterSelf();

        Block block = event.getBlock();

        block.setType(Material.AIR);

        block.getWorld().dropItem(block.getLocation(), this.stack);

    }

}
