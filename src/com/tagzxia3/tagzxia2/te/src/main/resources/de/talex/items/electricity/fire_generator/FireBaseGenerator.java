package com.tagzxia3.tagzxia2.te.src.main.resources.de.talex.items.electricity.fire_generator;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import com.talexs.talexessential.TalexEssential;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.function.generator.BaseGeneratorObject;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;
import pubsher.talexsoultech.talex.items.machine.MachineCore;
import pubsher.talexsoultech.talex.items.machine.MachineInfo;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.talex.managers.ElectricityManager;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author TalexDreamSoul
 */
public class FireBaseGenerator extends BaseGeneratorObject {

    private final HashMap<String, GeneratorMachine> furnaces = new HashMap<>(16);

    public FireBaseGenerator() {

        super("fire_generator", new ItemBuilder(Material.FURNACE).setName("§b火力发电机").setLore("", "§8> §f火焰! §c灼烧!"), 500, 10, 220);

    }

    public static int fuelTime(ItemStack itemstack) {

        if ( itemstack != null ) {

            Material type = itemstack.getType();

            if ( type.name().contains("WOODEN") ) {
                return 10;
            }
            if ( type.name().contains("WOOD") ) {
                return 100;
            }
            if ( type == Material.COAL ) {
                return 160;
            }
            if ( type == Material.COAL_BLOCK ) {
                return 1600;
            }

        }

        return 0;

    }

    @Override
    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("fire_generator", this)

                .addRequired("iron_wire")
                .addRequired("circuit_board")
                .addRequired("iron_wire")
                .addRequired("circuit_board")
                .addRequired(MachineCore.INSTANCE)
                .addRequired("circuit_board")
                .addRequired("iron_wire")
                .addRequired("circuit_board")
                .addRequired("iron_wire")

                ;

    }

    @Override
    public void onClickedMachineItemBlock(PlayerData playerData, PlayerInteractEvent event) {

        Block block = event.getClickedBlock();

        if ( block != null && ( block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE ) ) {

            GeneratorMachine gm = furnaces.get(Location2String(block.getLocation()));

            if ( gm == null ) {
                return;
            }

            String str = String.valueOf(gm.getMeta().get("owner"));

            boolean lock = (Boolean) gm.getMeta().getOrDefault("lock", false);

            if ( lock && !playerData.getName().equalsIgnoreCase(str) && !playerData.getPlayer().hasPermission("talex.soultech.admin") ) {

                playerData.actionBar("§c你不是它的主人!").playSound(Sound.BLOCK_ANVIL_LAND, 1.1F, 1.1F);

                event.setCancelled(true);

                return;

            }

            if ( playerData.getPlayer().isSneaking() ) {

                event.setCancelled(true);

                gm.getMeta().put("lock", !lock);

                if ( !lock ) {
                    playerData.actionBar("§a现在 §b火力发电机 §a私有了!").playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.1F, 1.1F);
                } else {
                    playerData.actionBar("§c现在 §b火力发电机 §c公有了!").playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.1F, 1.1F);
                }

            }

        }

    }


    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock tb, BlockBreakEvent event) {

        Block block = event.getBlock();

        if ( block != null && ( block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE ) ) {

            GeneratorMachine gm = furnaces.get(Location2String(block.getLocation()));

            if ( gm == null ) {
                return false;
            }

            String str = String.valueOf(gm.getMeta().get("owner"));

            boolean lock = (Boolean) gm.getMeta().getOrDefault("lock", false);

            if ( lock && !playerData.getName().equalsIgnoreCase(str) && !playerData.getPlayer().hasPermission("talex.soultech.admin") ) {

                playerData.actionBar("§c你不是它的主人!").playSound(Sound.BLOCK_ANVIL_LAND, 1.1F, 1.1F);

            }

        }

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

        if ( block != null && ( block.getType() == Material.FURNACE || block.getType() == Material.BURNING_FURNACE ) ) {

            String loc = Location2String(block.getLocation());

            GeneratorMachine gm = new GeneratorMachine(block.getLocation(), 0, getSingleSupplyCapacity(), getCapacity().getVoltage()) {

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

                    this.hologram.appendTextLine("§f当前状态: §r" + getMachineStatus().getDisplayName());
                    this.hologram.appendTextLine("§f存储电量: §c" + String.format("% .3f", this.getCapacity().getStorageCapacity()) + " §e§lSE ⚡");

                }

            };

            gm.registerHolograms(block.getLocation().clone().add(0.5, 1.75, 0.5));

            furnaces.put(loc, gm);
            ElectricityManager.INSTANCE.registerElectricity(block.getLocation(), gm);

            gm.getMeta().put("owner", playerData.getName());

            playerData.actionBar("§f你放下了 §b火力发电机 §f, SHIFT 右键 可上锁!");

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

        for ( Map.Entry<String, GeneratorMachine> entry : furnaces.entrySet() ) {

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

            MachineInfo.MachineStatus status = MachineInfo.MachineStatus.valueOf(json.get("status").getAsString());

            Capacity capacity = new Gson().fromJson(json.get("capacity").getAsJsonObject(), Capacity.class);

            GeneratorMachine gm = new GeneratorMachine(loc, capacity, getStorageCapacity(), getSingleSupplyCapacity(), getCapacity().getVoltage()) {

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
                    this.hologram.appendTextLine("§f当前状态: §r" + status.getDisplayName());
                    this.hologram.appendTextLine("§f存储电量: §c" + String.format("% .3f", this.getCapacity().getStorageCapacity()) + " §e§lSE ⚡");

                }

            };

            Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

            HashMap<String, Object> meta = new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(json.get("meta").getAsJsonObject(), type);

            gm.setMeta(meta);

            gm.setMachineStatus(status);
            gm.registerHolograms(loc.clone().add(0.5, 1.75, 0.5));

            furnaces.put(Location2String(loc), gm);
            ElectricityManager.INSTANCE.registerElectricity(loc, gm);

        }

        new BukkitRunnable() {

            @Override
            public void run() {

                for ( Map.Entry<String, GeneratorMachine> entry : new HashSet<>(furnaces.entrySet()) ) {

                    Location loc = String2Location(entry.getKey());

                    if ( loc == null ) {

                        entry.getValue().unRegisterHolograms();

                        furnaces.remove(entry.getKey());

                        continue;

                    }

                    Block block = loc.getBlock();

                    if ( block == null || ( block.getType() != Material.FURNACE && block.getType() != Material.BURNING_FURNACE ) ) {
                        entry.getValue().unRegisterHolograms();
                        furnaces.remove(entry.getKey());
                        assert block != null;
                        ElectricityManager.INSTANCE.unRegisterElectricity(block.getLocation(), entry.getValue());
                        continue;
                    }

                    new BukkitRunnable() {

                        /**
                         * When an object implementing interface <code>Runnable</code> is used
                         * to create a thread, starting the thread causes the object's
                         * <code>run</code> method to be called in that separately executing
                         * thread.
                         * <p>
                         * The general contract of the method <code>run</code> is that it may
                         * take any action whatsoever.
                         *
                         * @see Thread#run()
                         */
                        @Override
                        public void run() {

                            try {

//                                loc.getChunk().load();
//                                loc.getChunk().un

                                onRun(loc, entry);

                            } catch ( Exception ignored ) {

                                ignored.printStackTrace();

                            }

                        }
                    }.runTask(TalexEssential.getInstance());

                }

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 0, 2);

    }

    private void onRun(Location loc, Map.Entry<String, GeneratorMachine> entry) throws Exception {

        GeneratorMachine machine = entry.getValue();

        int timer = (Integer) machine.getMeta().getOrDefault("timer", 0);

        timer++;

        if ( timer >= 10 ) {

            machine.updateHologram();
            timer = 0;

        }

        machine.getMeta().put("timer", timer);

        if ( machine.getCapacity().getStorageCapacity() >= getStorageCapacity() ) {
            return;
        }

        Block block = loc.getBlock();

        Class<?> CraftFurnaceClass = Class.forName("org.bukkit.craftbukkit.v1_12_R1.block.CraftFurnace");

        Object FurnaceObject = block.getState();

        Method getFurnaceBurnTime = CraftFurnaceClass.getMethod("getBurnTime");
        Method setFurnaceBurnTime = CraftFurnaceClass.getMethod("setBurnTime", Short.TYPE);
        Method getInventory = CraftFurnaceClass.getMethod("getInventory");
        Method setCustomName = CraftFurnaceClass.getMethod("setCustomName", String.class);
        Method updateFurnace = CraftFurnaceClass.getMethod("update", Boolean.TYPE, Boolean.TYPE);

        setCustomName.invoke(FurnaceObject, "§b火力发电机 §7- §r" + machine.getMachineStatus().getDisplayName());

        short j = (Short) getFurnaceBurnTime.invoke(FurnaceObject);

        FurnaceInventory inv = (FurnaceInventory) getInventory.invoke(FurnaceObject);

        if ( inv.getSmelting() != null && inv.getSmelting().getType() != Material.AIR ) {

            Random random = new Random();

            loc.getWorld().dropItem(loc.clone().add(random.nextInt(3), random.nextInt(3), random.nextInt(3)), inv.getSmelting());

            inv.setSmelting(null);

            return;

        }

        int burn = (Integer) machine.getMeta().getOrDefault("burn", 0);

        ItemStack fuel = inv.getFuel();

        if ( fuel == null && burn <= 0 ) {
            machine.setMachineStatus(MachineInfo.MachineStatus.NEED_STH);
            return;
        }

        int time = fuelTime(fuel);

        if ( time == 0 || burn <= 0 ) {

            return;

        }

        val addon = 300 / time;

        if ( j >= 300 ) {

            machine.setMachineStatus(MachineInfo.MachineStatus.PREPARING);

            setFurnaceBurnTime.invoke(FurnaceObject, (short) 0);
            updateFurnace.invoke(FurnaceObject, true, false);

            return;

        }

        if ( j <= 0 ) {

            j = (short) ( j + 10 );
            setFurnaceBurnTime.invoke(FurnaceObject, j);
            updateFurnace.invoke(FurnaceObject, true, false);

            inv = (FurnaceInventory) getInventory.invoke(FurnaceObject);

            fuel = inv.getFuel();

            if ( fuel == null ) {
                return;
            }

            fuel.setAmount(fuel.getAmount() - 1);

            inv.setFuel(fuel);

            machine.getMeta().put("burn", time + burn);

            return;

        }

        machine.setMachineStatus(MachineInfo.MachineStatus.RUNNING);

        j = (short) ( j + addon );
        machine.getMeta().put("burn", burn - addon);
        setFurnaceBurnTime.invoke(FurnaceObject, j);
        updateFurnace.invoke(FurnaceObject, true, false);

        machine.getCapacity().addStorageCapacity(new Capacity(( (double) time / 2500 ), machine.getCapacity().getVoltage()));

        loc.getWorld().spawnParticle(Particle.CLOUD, loc.clone().add(0.5, 0.9, 0.5), 3, 0, 0, 0, 0.001);
        loc.getWorld().spawnParticle(Particle.FLAME, loc.clone().add(0.5, 0.8, 0.5), 5, 0, 0, 0, 0.01);
        loc.getWorld().spawnParticle(Particle.BARRIER, loc.clone().add(0.5, 0.85, 0.5), 1, 0, 0, 0, 0.1);

    }

}
