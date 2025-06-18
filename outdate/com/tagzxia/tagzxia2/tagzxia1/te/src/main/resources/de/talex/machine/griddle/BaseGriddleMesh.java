package com.tagzxia2.tagzxia1.te.src.main.resources.de.talex.machine.griddle;

import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleMachine;
import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.SoulTechItem;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.griddle }
 *
 * @author TalexDreamSoul
 * @date 2021/8/16 0:25
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
@Getter
@Setter
public abstract class BaseGriddleMesh extends SoulTechItem {

    private Short maxDurability;

    public BaseGriddleMesh(ItemBuilder stack, short maxDurability) {

        super("griddle_mesh", stack.toItemStack());

        this.maxDurability = maxDurability;

    }

    /**
     * 是否更细致筛网
     *
     * @return 是否
     */
    public abstract boolean isIron();

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {

        return false;
    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

    }

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {

        Block block = event.getBlock();

        if ( block != null ) {

            Block block2 = block.getLocation().add(0, -1, 0).getBlock();

            if ( block2.getType() == Material.HOPPER ) {

                String str = NBTsUtil.Location2String(block2.getLocation());

                GriddleObject obj = GriddleMachine.map.get(str);

                if ( obj == null ) {

                    obj = new GriddleObject();

                    obj.setBlock(block);

                    GriddleMachine.map.put(str, obj);

                }

                obj.setMaxDurability(this.maxDurability).setIron(this.isIron());

                return false;

            }

            event.setCancelled(true);

            playerData.playSound(Sound.BLOCK_ANVIL_LAND, 1.1f, 1.2f).title("§c§l✖", "§7筛子不可以放在这里..", 5, 12, 5);

            return true;

        }

        return true;

    }

    @Override
    public void onCrafted(PlayerData playerData) {

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

    }

    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {

        return false;

    }

}
