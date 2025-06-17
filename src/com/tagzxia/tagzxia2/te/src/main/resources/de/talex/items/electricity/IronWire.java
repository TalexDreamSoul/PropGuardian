package com.tagzxia2.te.src.main.resources.de.talex.items.electricity;

import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.electricity.function.wire.IWire;
import pubsher.talexsoultech.talex.items.machine.rooter.BaseWire;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.talex.managers.ElectricityManager;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MachineBlockItem;
import pubsher.talexsoultech.utils.item.MineCraftItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pubsher.talexsoultech.utils.NBTsUtil.Location2String;
import static pubsher.talexsoultech.utils.NBTsUtil.String2Location;

/**
 * @author TalexDreamSoul
 */
public class IronWire extends MachineBlockItem {

    public IronWire() {

        super("iron_wire", new ItemBuilder(Material.getMaterial(101))

                .setName("§b铁质导线")
                .setLore("", "§f损耗: §e0.5 §lSE ⚡§e/m", "§f电压: §e0 - 330 §lSV ⚡", "§f上限: §e50 §lSE ⚡", "")

                .toItemStack());

    }

    @Override
    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("iron_wire", this)

                .addRequired("resin")
                .addRequired(new MineCraftItem(Material.getMaterial(101)))
                .addRequired("resin")
                .addRequired(new MineCraftItem(Material.getMaterial(101)))
                .addRequired("resin")
                .addRequired(new MineCraftItem(Material.getMaterial(101)))
                .addRequired("resin")
                .addRequired(new MineCraftItem(Material.getMaterial(101)))
                .addRequired("resin")

                .setAmount(4);

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

        BaseWire bw = new BaseWire(50, 330, 30) {

            @Override
            public double getSingleTransmissionLoss() {

                return 0.5;
            }

        };

        bw.setSymbol(getID());

        ElectricityManager.INSTANCE.registerElectricity(event.getBlock().getLocation(), (IWire) bw);

        playerData.actionBar("§f你放置了 §b铁质导线");

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

        ElectricityManager.INSTANCE.unRegisterWire(event.getBlock().getLocation());

        return true;
    }

    @Override
    public String onSave() {

        List<JsonObject> list = new ArrayList<>();

        for ( Map.Entry<Location, IWire> entry : ElectricityManager.INSTANCE.getWireHashMap().entrySet() ) {

            IWire wire = entry.getValue();

            if ( !( wire instanceof BaseWire ) ) {
                continue;
            }

            BaseWire bw = (BaseWire) wire;

            if ( !bw.getSymbol().equalsIgnoreCase(getID()) ) {
                continue;
            }

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("loc", Location2String(entry.getKey()));

            list.add(jsonObject);

        }

        return new Gson().toJson(list);

    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {

        JsonElement je = new JsonParser().parse(str);

        JsonArray ja = je.getAsJsonArray();

        for ( JsonElement element : ja ) {

            JsonObject json = element.getAsJsonObject();

            Location loc = String2Location(json.get("loc").getAsString());

            if ( loc == null ) {
                continue;
            }

            Block block = loc.getBlock();

            if ( block == null || block.getType() != Material.getMaterial(101) ) {
                continue;
            }

            BaseWire bw = new BaseWire(50, 330, 30) {

                @Override
                public double getSingleTransmissionLoss() {

                    return 0.5;
                }

            };

            bw.setSymbol(getID());

            ElectricityManager.INSTANCE.registerElectricity(loc, (IWire) bw);

        }

    }

}
