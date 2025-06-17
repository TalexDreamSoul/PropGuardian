package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.items.electricity.storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.entity.PlayerDataRunnable;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.function.generator.BaseGeneratorObject;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;
import pubsher.talexsoultech.talex.items.machine.MachineCore;
import pubsher.talexsoultech.talex.items.machine.MachineInfo;
import pubsher.talexsoultech.talex.items.machine.rooter.BaseStorager;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.talex.managers.ElectricityManager;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MineCraftItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TalexDreamSoul
 */
public class NormalStorage extends BaseGeneratorObject {

    private final HashMap<String, BaseStorager> storages = new HashMap<>(16);

    public NormalStorage() {

        super("normal_storage", new ItemBuilder(Material.JUKEBOX)

                .setName("§a基础畜电池")
                .setLore("", "§8> §a把电存起来!"), 1500, 30, 330);


    }

    @Override
    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("normal_storage", this)

                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("iron_wire")
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired("circuit_board")
                .addRequired(new MineCraftItem(Material.REDSTONE_BLOCK))
                .addRequired("circuit_board")
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))
                .addRequired(MachineCore.INSTANCE)
                .addRequired(new MineCraftItem(Material.IRON_BLOCK))

                ;

    }

    @Override
    public void onClickedMachineItemBlock(PlayerData playerData, PlayerInteractEvent event) {

        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK ) {
            return;
        }

        Block block = event.getClickedBlock();

        if ( block == null || block.getType() != Material.JUKEBOX ) {
            return;
        }

        playerData.actionBar("§c§l这台机器没有界面!").delayRun(new PlayerDataRunnable() {

            @Override
            public void run() {

                playerData.closeInventory();

            }
        }, 3);

    }


    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock tb, BlockBreakEvent event) {

        Block block = tb.getLoc().getBlock();

        Location loc = block.getLocation();

        String strLoc = Location2String(loc);

        BaseStorager baseStorager = storages.get(strLoc);

        baseStorager.unRegisterHolograms();

        storages.remove(strLoc);

        ElectricityManager.INSTANCE.unRegisterReceiver(loc);

        return true;

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

        Block block = event.getBlock();

        if ( block != null && block.getType() == Material.JUKEBOX ) {

            String loc = Location2String(block.getLocation());

            BaseStorager gm = new BaseStorager(block.getLocation(), 0, getSingleSupplyCapacity(), getCapacity().getVoltage()) {

                @Override
                public Location getMainTransferLocation() {

                    return block.getLocation();
                }

                @Override
                public double getProvideMaxDistance() {

                    return 16;
                }

                @Override
                public void updateMachineHologram(GeneratorMachine generatorMachine) {

                    this.hologram.clearLines();

                    this.hologram.appendTextLine("§f存储电量: §c" + String.format("% .4f", this.getCapacity().getStorageCapacity()) + " §e§lSE ⚡");

                }

            };

            gm.registerHolograms(block.getLocation().clone().add(0.5, 1.55, 0.5));

            storages.put(loc, gm);
            ElectricityManager.INSTANCE.registerElectricity(block.getLocation(), (IReceiver) gm);

        }

        return false;

    }

    @Override
    public void onCrafted(PlayerData playerData) {

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

    }

    @Override
    public String onSave() {

        List<JsonObject> list = new ArrayList<>();

        for ( Map.Entry<String, BaseStorager> entry : storages.entrySet() ) {

            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("loc", NBTsUtil.Base64_Encode(entry.getKey()));
            jsonObject.addProperty("status", entry.getValue().getMachineStatus().name());
            jsonObject.add("capacity", new JsonParser().parse(new Gson().toJson(entry.getValue().getCapacity())));

            jsonObject.add("meta", new JsonParser().parse(new GsonBuilder().enableComplexMapKeySerialization().create().toJson(entry.getValue().getReceiverPathMap())));

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

            Location loc = String2Location(NBTsUtil.Base64_Decode(json.get("loc").getAsString()));

            if ( loc == null ) {
                continue;
            }

            Block block = loc.getBlock();

            if ( block == null || block.getType() != Material.JUKEBOX ) {
                continue;
            }

            MachineInfo.MachineStatus status = MachineInfo.MachineStatus.valueOf(json.get("status").getAsString());

            Capacity capacity = new Gson().fromJson(json.get("capacity").getAsJsonObject(), Capacity.class);

            BaseStorager gm = new BaseStorager(loc, capacity, getStorageCapacity(), getSingleSupplyCapacity(), getCapacity().getVoltage()) {

                @Override
                public Location getMainTransferLocation() {

                    return loc;
                }

                @Override
                public double getProvideMaxDistance() {

                    return 16;
                }

                @Override
                public void updateMachineHologram(GeneratorMachine generatorMachine) {

                    this.hologram.clearLines();
                    this.hologram.appendTextLine("§f存储电量: §c" + String.format("% .4f", this.getCapacity().getStorageCapacity()) + " §e§lSE ⚡");

                }

            };

            Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

            HashMap<String, Object> meta = new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(json.get("meta").getAsJsonObject(), type);

            gm.setMeta(meta);

            gm.setMachineStatus(status);
            gm.registerHolograms(loc.clone().add(0.5, 1.55, 0.5));

            storages.put(Location2String(loc), gm);
            ElectricityManager.INSTANCE.registerElectricity(loc, (IReceiver) gm);

        }

    }

}
