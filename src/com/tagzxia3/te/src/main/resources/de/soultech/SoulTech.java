package com.tagzxia3.te.src.main.resources.de.soultech;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.item.MachineBlockItem;
import de.talex.electricity.function.ElectricityAchiever;
import de.talex.machine.BaseMachine;
import de.talex.machine.advanced_workbench.AdvancedWorkBench;
import de.talex.machine.break_hammer.BreakHammerMachine;
import de.talex.machine.compress_machine.Compressor;
import de.talex.machine.furnace_cauldron.FurnaceCauldronMachine;
import de.talex.machine.griddle.GriddleMachine;
import de.talex.managers.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * @author TalexDreamSoul
 */
@Getter
public class SoulTech {

    @Getter
    private static SoulTech instance;

    private final TalexEssential plugin;

    private final ElectricityManager electricityManager = ElectricityManager.INSTANCE;
    private MachineManager machineManager;
    private BlockManager blockManager;
    private ProtectorManager protectorManager;
    @Getter
    private CategoryManager categoryManager;

    private SoulTech(TalexEssential plugin) {

        this.plugin = plugin;

    }

    public static void init(TalexEssential plugin) {

        instance = new SoulTech(plugin);

        instance.enable();

    }

    public void enable() {

//        mysqlManager.joinTable(
//
//                new SqlTableBuilder().setTableName("soul_tech_system")
//
//                        .addTableParam(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("st_id").setType("VARCHAR(64)"))
//                        .addTableParam(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(true).setSubParamName("st_key").setType("VARCHAR(32)"))
//                        .addTableParam(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(true).setSubParamName("st_value").setType("VARCHAR(256)"))
//
//        );

        this.categoryManager = new CategoryManager(this);
        this.categoryManager.enable();

        this.machineManager = new MachineManager(this);
        this.blockManager = new BlockManager(this);
        this.protectorManager = new ProtectorManager(this);

        new AdvancedWorkBench();
        new BreakHammerMachine();
        new Compressor();
        new FurnaceCauldronMachine();
        new GriddleMachine();

        initBase();

    }

    @SneakyThrows
    private void initBase() {

        getBlockManager().loadAllFromFile(plugin.getDataFolder() + "/caches/block_caches.yml");

        File file = new File(plugin.getDataFolder() + "/caches/SoulTechItems.yml");

        if ( !file.exists() ) {
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load(file);

        if ( yaml.contains("MachineBlockItems") ) {

            for ( String key : new HashSet<>(Objects.requireNonNull(yaml.getConfigurationSection("MachineBlockItems")).getKeys(false)) ) {

                String ID = yaml.getString("MachineBlockItems." + key + ".ID");
                String cls = yaml.getString("MachineBlockItems." + key + ".class");

                try {

                    Class<?> clz = Class.forName(cls);

                    if ( clz == MachineBlockItem.class ) {

                        plugin.getLogger().info("无法加载类: " + cls + " | 无法加载 MachineBlockItem.class 仅能加载之类 - 请检查! " + ID + " @" + key);

                        continue;

                    }

                    String str = NBTsUtil.Base64_Decode(Objects.requireNonNull(yaml.getString("MachineBlockItems." + ID + ".save")));

                    MachineBlockItem mbi = (MachineBlockItem) clz.newInstance();

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

                            mbi.onLoad(str);

                        }
                    }.runTaskLater(plugin, 5);

                } catch ( ClassNotFoundException e ) {

                    plugin.getLogger().info("无法找到类: " + cls + " | 无法加载 物品 " + ID + " @" + key);

                }

            }

        }

        ElectricityAchiever.getInstance();

        loadMachines();

    }

    @SneakyThrows
    private void loadMachines() {

        File file = new File(plugin.getDataFolder() + "/caches/Machines.yml");

        if ( !file.exists() ) {
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load(file);

        for ( Map.Entry<String, BaseMachine> item : getMachineManager().getMachinesClone() ) {

            item.getValue().onLoad(NBTsUtil.Base64_Decode(yaml.getString("Machines." + item.getKey() + ".data", "")));

//            plugin.log("&7[&5灵魂&b科技&7] &8[存储] &e" + item.getKey() + " &7机器加载完毕!");

        }

    }

}
