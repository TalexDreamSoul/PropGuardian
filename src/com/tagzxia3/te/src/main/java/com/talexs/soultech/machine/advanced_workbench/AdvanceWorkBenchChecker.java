package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.advanced_workbench;

import com.talexs.soultech.machine.bsae.IMachineChecker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdvanceWorkBenchChecker implements IMachineChecker {

    public AdvanceWorkBenchChecker() {

    }


    public static boolean onInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        if ( action != Action.RIGHT_CLICK_BLOCK ) {
            return false;
        }

        Block block = event.getClickedBlock();

        if ( block != null && block.getType() == Material.CRAFTING_TABLE ) {

            Block block2 = block.getLocation().clone().add(0, 1, 0).getBlock();

            return block2.getType() == Material.GLASS;

        }

        return false;

    }

    @Override
    public boolean onCheck(PlayerEvent event) {

        if ( event instanceof PlayerInteractEvent ) {

            return onInteract((PlayerInteractEvent) event);

        }

        return false;

    }

}
