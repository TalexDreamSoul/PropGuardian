package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.items.electricity;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MachineItem;
import pubsher.talexsoultech.utils.item.MineCraftItem;

public class CircuitBoard extends MachineItem {

    public CircuitBoard() {

        super("circuit_board", new ItemBuilder(Material.DETECTOR_RAIL)

                .setName("§b电路板")
                .setLore("", "§8> §f奇思妙想!", "")

                .toItemStack());

    }

    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("circuit_board", this)

                .addRequired("iron_wire")
                .addRequired(new MineCraftItem(Material.REDSTONE))
                .addRequired("iron_wire")
                .addRequired("iron_wire")
                .addRequired(new MineCraftItem(Material.GOLD_INGOT))
                .addRequired("iron_wire")
                .addRequired("iron_wire")
                .addRequired(new MineCraftItem(Material.REDSTONE))
                .addRequired("iron_wire")

                ;

    }

    @Override
    public void onClickedMachineItemBlock(PlayerData playerData, PlayerInteractEvent event) {

    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

    }

    /**
     * @param playerData : 玩家数据
     * @param event      : 事件传递
     *
     * @return 是否从BlockManager中移除
     *
     * @Description: 设置EventCancel 代表方块不破坏 - 如果返回真将会把这个物品从BLOCKMANAGER中移除!
     */
    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {

        return false;
    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

    }

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {

        event.setCancelled(true);

        playerData.actionBar("§c电路板 放置在地上会损坏它!").playSound(Sound.BLOCK_ANVIL_LAND, 1.1F, 1.1F);

        return false;

    }

    @Override
    public void onCrafted(PlayerData playerData) {

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

    }

    /**
     * 当放置的方块被破坏时
     *
     * @param playerData 玩家数据
     * @param event      事件传递
     *
     * @return 返回真则从BlockManager中删除
     */
    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock tb, BlockBreakEvent event) {

        return true;
    }

}
