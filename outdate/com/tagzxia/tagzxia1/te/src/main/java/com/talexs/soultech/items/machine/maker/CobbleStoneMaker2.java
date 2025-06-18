package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.items.machine.maker;//package com.talexs.soultech.items.machine.maker;
//
//import com.gmail.filoghost.holographicdisplays.api.Hologram;
//import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
//import lombok.SneakyThrows;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Particle;
//import org.bukkit.block.Block;
//import org.bukkit.configuration.file.YamlConfiguration;
//import org.bukkit.event.block.Action;
//import org.bukkit.event.block.BlockBreakEvent;
//import org.bukkit.event.block.BlockPlaceEvent;
//import org.bukkit.event.player.PlayerDropItemEvent;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.event.player.PlayerItemHeldEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.scheduler.BukkitRunnable;
//import com.talexs.talexessential.TalexEssential;
//import pubsher.talexsoultech.entity.PlayerData;
//import pubsher.talexsoultech.entity.PlayerDataRunnable;
//import pubsher.talexsoultech.talex.items.machine.MachineCore;
//import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
//import pubsher.talexsoultech.utils.LogUtil;
//import pubsher.talexsoultech.utils.NBTsUtil;
//import pubsher.talexsoultech.utils.block.TalexBlock;
//import pubsher.talexsoultech.utils.item.ItemBuilder;
//import pubsher.talexsoultech.utils.item.MachineBlockItem;
//import pubsher.talexsoultech.utils.item.MineCraftItem;
//import pubsher.talexsoultech.utils.item.TalexItem;
//
//import java.util.*;
//
//public class CobbleStoneMaker2 extends MachineBlockItem {
//
//    private Set<Block> placedPlaces = new HashSet<>();
//
//    public CobbleStoneMaker2() {
//
//        super("machine_cobblestone_maker_2", new ItemBuilder(Material.DROPPER).setName("§e刷石机 §bII")
//
//                .setLore("", "§b◈ §e数量: §cx3个 次", "§b◈ §e冷却: §cx5秒 次", "")
//
//
//                .toItemStack());
//
//    }
//
//    @Override
//    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
//
//    }
//
//    @Override
//    public void onClickedMachineItemBlock(PlayerData playerData, PlayerInteractEvent event) {
//
//        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK ) {
//            return;
//        }
//
//        Block block = event.getClickedBlock();
//
//        if ( block == null || !placedPlaces.contains(block) ) {
//            return;
//        }
//
//        playerData.actionBar("§c§l这台机器没有界面!").delayRun(new PlayerDataRunnable() {
//
//            @Override
//            public void run() {
//
//                playerData.closeInventory();
//
//            }
//        }, 3);
//
//    }
//
//    @Override
//    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {
//
//        return false;
//
//    }
//
////    private OnlinePlayerData owner;
//
//    @Override
//    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {
//
//    }
//
//    @Override
//    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {
//
//        Block block = event.getBlock();
//
//        if ( block == null ) {
//            return false;
//        }
//
////        this.owner = playerData;
//
//        placedPlaces.add(block);
//        playerData.actionBar("§a§l你放下了 §e§l刷石机 §bII");
//
//        return false;
//
//    }
//
//    @Override
//    public void onCrafted(PlayerData playerData) {
//
//    }
//
//    @Override
//    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {
//
//    }
//
//    /**
//     * 当放置的方块被破坏时
//     *
//     * @param playerData 玩家数据
//     * @param event      事件传递
//     *
//     * @return 返回真则从BlockManager中删除
//     */
//    @Override
//    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock tb, BlockBreakEvent event) {
//
//        return true;
//    }
//
//    public WorkBenchRecipe getRecipe() {
//
//        return new WorkBenchRecipe("st_cobblestone_maker_2", new TalexItem(super.getItemBuilder().toItemStack()))
//
//                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
//                .addRequired("machine_cobblestone_maker_1")
//                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
//                .addRequired("machine_cobblestone_maker_1")
//                .addRequired(MachineCore.INSTANCE)
//                .addRequired("machine_cobblestone_maker_1")
//                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
//                .addRequired("machine_cobblestone_maker_1")
//                .addRequired(new MineCraftItem(Material.GOLD_BLOCK))
//
//                ;
//
//    }
//
//    @Override
//    public String onSave() {
//
//        YamlConfiguration yaml = new YamlConfiguration();
//
//        List<String> list = new ArrayList<>();
//
//        for ( Block block : placedPlaces ) {
//
//            if ( block == null || block.getType() != Material.DROPPER ) {
//                continue;
//            }
//
//            String strLoc = NBTsUtil.Location2String(block.getLocation());
//            list.add(strLoc);
//
//        }
//
//        yaml.set("PlacedBlocks", list);
//
//        return yaml.saveToString();
//
//    }
//
//    @SneakyThrows
//    @Override
//    public void onLoad(String str) {
//
//        YamlConfiguration yaml = new YamlConfiguration();
//
//        yaml.loadFromString(str);
//
//        if ( yaml.contains("PlacedBlocks") ) {
//
//            for ( String key : new ArrayList<>(yaml.getStringList("PlacedBlocks")) ) {
//
//                Location loc = NBTsUtil.String2Location(key);
//
//                if ( loc == null || loc.getWorld() == null || loc.getBlock() == null ) {
//
//                    LogUtil.log("加载 " + key + " NULL 错误 - 已跳过!");
//                    continue;
//
//                }
//
//                if ( loc.getBlock().getType() != Material.DROPPER ) {
//
//                    LogUtil.log("加载 " + key + " TYPE ERROR 错误 - 已跳过!");
//                    continue;
//
//                }
//
//                placedPlaces.add(loc.getBlock());
//
//            }
//
//        }
//
//        Map<String, Integer> atomicMap = new HashMap<>(placedPlaces.size());
//        Map<String, Hologram> hologramMap = new HashMap<>(placedPlaces.size());
//
//        new BukkitRunnable() {
//
//            @Override
//            public void run() {
//
//                for ( Block block : new HashSet<>(placedPlaces) ) {
//
//                    Location loc = block.getLocation();
//                    String strLoc = NBTsUtil.Location2String(loc);
//
//                    new BukkitRunnable() {
//
//                        @Override
//                        public void run() {
//
//                            Hologram hologram = hologramMap.get(strLoc);
//
//                            if ( hologram == null ) {
//
//                                hologramMap.put(strLoc, HologramsAPI.createHologram(TalexEssential.getInstance(), block.getLocation().add(0.5, 2.25, 0.5)));
//
//                            }
//
//                            hologram = hologramMap.get(strLoc);
//
//                            if ( block.getType() != Material.DROPPER ) {
//
//                                hologram.clearLines();
//
//                                hologram.delete();
//
//                                hologramMap.remove(strLoc);
//                                atomicMap.remove(strLoc);
//
//                                placedPlaces.remove(block);
//
//                                return;
//
//                            }
//
//                            int number = atomicMap.getOrDefault(strLoc, 0);
//
//                            number++;
//
//                            hologram.clearLines();
//                            hologram.appendItemLine(new ItemStack(Material.COBBLESTONE));
//                            hologram.appendTextLine("§f产出: §e" + ( ( ( 10 - number ) / 2 ) + 1 ) + " §f秒后");
//
//                            hologramMap.put(strLoc, hologram);
//                            atomicMap.put(strLoc, number);
//
//                            if ( number >= 10 ) {
//
//                                atomicMap.remove(strLoc);
//
//                                block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.95, 0.5), 10, 0, 0, 0, 0.0001);
//
//                                block.getWorld().dropItem(block.getLocation().add(0.5, 1.25, 0.5), new ItemStack(Material.COBBLESTONE));
//                                block.getWorld().dropItem(block.getLocation().add(0.5, 1.25, 0.5), new ItemStack(Material.COBBLESTONE));
//                                block.getWorld().dropItem(block.getLocation().add(0.5, 1.25, 0.5), new ItemStack(Material.COBBLESTONE));
//
//                            }
//
//                        }
//                    }.runTask(TalexEssential.getInstance());
//
//                }
//
//            }
//
//        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 10, 10);
//
//    }
//
//}
