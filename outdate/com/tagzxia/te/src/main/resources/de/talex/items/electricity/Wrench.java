package com.tagzxia.te.src.main.resources.de.talex.items.electricity;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MachineItem;

public class Wrench extends MachineItem {

    public Wrench() {

        super("electricity_wrench", new ItemBuilder(Material.SHEARS).setName("§b扳手").setLore("", "§8> §b卸下你的机器!", "").toItemStack());

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
     * @param block
     * @param event      事件传递
     *
     * @return 返回真则掉落物品 - 可返回假自定义
     */
    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {

        return false;
    }

}
