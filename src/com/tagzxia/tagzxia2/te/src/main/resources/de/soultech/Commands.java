package com.tagzxia2.te.src.main.resources.de.soultech;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.environment.blood_moon.BloodMoonCreator;
import pubsher.talexsoultech.talex.items.GuideBookItem;
import pubsher.talexsoultech.talex.items.GuiderBookItem;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @author TalexDreamSoul
 */
public class Commands implements CommandExecutor {

    HashMap<String, Long> cd = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ( args.length < 1 ) {
            return false;
        }

        if ( sender instanceof Player ) {

            Player player = (Player) sender;

            PlayerData playerData = TalexEssential.getInstance().getBaseTalex().getPlayerManager().get(player.getName());

            if ( "guide".equalsIgnoreCase(args[0]) ) {

                if ( System.currentTimeMillis() - cd.getOrDefault(playerData.getName(), (long) -1) <= 60000 ) {

                    return false;

                }

                cd.put(playerData.getName(), System.currentTimeMillis());

                if ( playerData.isFirstUse() ) {
                    new GuideBookItem(playerData);
                } else {
                    new GuiderBookItem(playerData);
                }

            }

            if ( sender.hasPermission("talex.soultech.admin") ) {

                if ( args[0].equalsIgnoreCase("blood-moon") ) {

                    new BloodMoonCreator(player.getWorld()).setStartTime(System.currentTimeMillis() - 300000).start();

                }

            }

//            if(sender.hasPermission("talex.soultech.admin")) {
//
//                if(args[0].equalsIgnoreCase("cap-search")) {
//
//                    player.sendMessage("正在准备遍历周围 5 格的电力系统状况!!");
//                    player.sendMessage("--> 本次电力模拟使用 栅栏 作为 IWire!");
//                    player.sendMessage("--> 本次电力模拟使用 熔炉 作为 IGenerator!");
//                    player.sendMessage("--> 本次电力模拟使用 工作台 作为 IReceiver!");
//
//                    player.sendMessage("----------------------------------------");
//
//                    PathAlgorithm pathAlgorithm = new PathAlgorithm("Test_Example#" + System.currentTimeMillis(), new MCTransferInvention() {
//
//                        @Override
//                        public Location getMainTransferLocation() {
//                            return player.getLocation();
//                        }
//
//                        @Override
//                        public double getProvideMaxDistance() {
//                            return 30;
//                        }
//
//                        @Override
//                        public Capacity provideCapacity(Capacity willCapacity) {
//                            return new Capacity(0, willCapacity.getVoltage()).provideStorageCapacity(willCapacity);
//                        }
//
//                        @Override
//                        public boolean canProvideCapacity(Capacity willCapacity) {
//                            return new Capacity(0, willCapacity.getVoltage()).canProvideCapacity(willCapacity);
//                        }
//
//                    }) {
//                        @Override
//                        public void onAlgorithmic() {
//
//                            new BukkitRunnable() {
//                                @Override
//                                public void run() {
//
//                                    for(RouterPath rp : getPaths()) {
//
//                                        TalexLocation tl = getTLByLoc(rp.getTo());
//
//                                        for(TalexLocation loc = tl;loc != null;loc = loc.getFather()) {
//
//                                            ParticleUtil.StraightLine(tl.getLoc(), tl.getFather().getLoc(), Particle.FLAME);
//
//                                        }
//
//                                    }
//
//                                }
//
//                            }.runTask(TalexEssential.getInstance());
//
//                        }
//
//                        @Override
//                        public IPower getLocationBlockInstance(Location loc) {
//
//                            if(loc == null) return null;
//
//                            Block block = loc.getBlock();
//
//                            if(block == null) return null;
//
//                            if(block.getType() == Material.FURNACE) {
//
//                                return new GeneratorObject(0, 10, 220);
//
//                            }
//
//                            if(block.getType() == Material.getMaterial(101)) {
//
//                                return new WireObject(new Capacity(0, 220), 1);
//
//                            }
//
//                            if(block.getType() == Material.WORKBENCH) {
//
//                                return new DeviceObject(new Capacity(0, 220));
//
//                            }
//
//                            return null;
//
//                        }
//
//                        @Override
//                        public boolean checkIsPowerAndReceiver(Location loc) {
//
//                            Block block = loc.getBlock();
//
//                            if(block == null) return false;
//
//                            if(block.getType() == Material.getMaterial(101)) {
//
//                                return true;
//
//                            }
//
//                            return block.getType() == Material.WORKBENCH;
//
//                        }
//                    };
//
//
//                    return false;
//
//                }
//
//            }

            if ( sender.hasPermission("talex.soultech.debug") ) {

                if ( args[0].equalsIgnoreCase("item-nbts") ) {

                    ItemStack stack = player.getItemInHand();

                    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
                    NBTTagCompound tag = nmsItem.getTag();

                    if ( tag == null ) {

                        player.sendMessage("无 NBT !");
                        return false;

                    }

                    for ( String str : new HashSet<>(tag.c()) ) {

                        player.sendMessage("Key: " + str + " | Value: " + tag.getString(str));

                    }

                    player.sendMessage("NBT 遍历完毕!");

                    return false;

                }

            }

        }

        if ( !sender.hasPermission("talex.soultech.admin") ) {
            return false;
        }

        if ( args[0].equalsIgnoreCase("datalist") ) {

            sender.sendMessage("当前已加载数据玩家表: " + TalexEssential.getInstance().getBaseTalex().getPlayerManager().keySet());

        }

        return false;

    }

}
