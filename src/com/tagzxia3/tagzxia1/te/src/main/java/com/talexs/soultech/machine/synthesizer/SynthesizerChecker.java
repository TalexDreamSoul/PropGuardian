package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.machine.synthesizer;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.block.BlockAddon;
import com.talexs.soultech.machine.bsae.IMachineChecker;
import com.talexs.talexessential.utils.block.TalexBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class SynthesizerChecker implements IMachineChecker {
    @Override
    public boolean onCheck(PlayerEvent event) {
        if ( !(event instanceof PlayerInteractEvent) ) return false;
        PlayerInteractEvent e = (PlayerInteractEvent) event;
        Block block = e.getClickedBlock();
        if ( block == null ) return false;

        if ( !detectVertical(block) ) return false;

        return detectHorizontal(block);
    }

    private boolean checkCenter(@NotNull Block block) {
//        TalexBlock block1 = AddonHolder.getINSTANCE().get(BlockAddon.class).getBlock(block);
//
//        if ( block1 == null ) return true;
//
//        return !Objects.equals(block1.getItem().getID(), "st_steel_fire_glass");
        return block.getType() != Material.DROPPER;
    }

    private boolean detectVertical(@NotNull Block center) {
        if (checkCenter(center)) return false;

        Location loc = center.getLocation();

        Block up = loc.add(0, 1, 0).getBlock();
        if ( up.getType() != Material.CHEST ) return false;

        Block down = loc.add(0, -2, 0).getBlock();
        return down.getType() == Material.REDSTONE_BLOCK;
    }

    private boolean detectHorizontal(@NotNull Block center) {
        if (checkCenter(center)) return false;

        Location loc = center.getLocation();

        // 左右检测
        Location base = loc.clone();

        Block left = base.add(-1, 1, 0).getBlock();
        if ( subDetectVertical(left) ) {
            if ( subDetectVertical(base.add(2, 0, 0).getBlock()) ) {
                return true;
            }
        }

        base = loc.clone();
        Block front = base.add(0, 1, 1).getBlock();
        if ( subDetectVertical(front) ) {
            return subDetectVertical(base.add(0, 0, -2).getBlock());
        }

        return false;
    }

    private boolean subDetectVertical(@NotNull Block up) {
        if ( up.getType() != Material.IRON_TRAPDOOR ) return false;

        Location loc = up.getLocation();

        Block down = loc.add(0, -1, 0).getBlock();
        if ( down.getType() != Material.PISTON ) return false;

        return loc.add(0, -1, 0).getBlock().getType() == Material.CRYING_OBSIDIAN;
    }
}
