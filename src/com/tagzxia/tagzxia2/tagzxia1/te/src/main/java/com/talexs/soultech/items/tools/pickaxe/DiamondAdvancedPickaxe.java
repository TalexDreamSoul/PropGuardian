package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import top.zoyn.particlelib.pobject.Ray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiamondAdvancedPickaxe extends BaseToolItem {
    public DiamondAdvancedPickaxe() {
        super("st_diamond_advanced_pickaxe_plus", new ItemBuilder(Material.DIAMOND_PICKAXE).setName("&f高级钻石镐")
                .setLore(
                        "",
                        "&8| &7高级压缩简并态钻石镐",
                        "&8| &7奇异比压缩等离子材料",
                        "",
                        "&8| &7主动技 &e虚尽空易",
                        "&8|   &e蹲下右键 &7以切换附魔",
                        "&8|",
                        "&8| &7主动技 &e地岩狱锁",
                        "&8|   &7在地狱挖掘地狱岩时引发连锁",
                        "",
                        "&8| &7主动技 &e花髋血蔷",
                        "&8|   &7挖掘矿物时都会自动寻找相邻矿物",
                        ""
                )
                .addEnchant(Enchantment.DIG_SPEED, 8)
                .addEnchant(Enchantment.SILK_TOUCH, 8)
                .addEnchant(Enchantment.DURABILITY, 12)
                .addEnchant(Enchantment.MENDING, 1)
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequired("st_diamond_strengthen_pickaxe_plus")
                .addRequired("st_harden_core")
                .addRequired("st_diamond_strengthen_pickaxe_plus")
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_netherite_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_compressed_netherite_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ( !player.isSneaking() || event.getAction() != Action.RIGHT_CLICK_AIR ) return;

        PlayerInventory inventory = player.getInventory();

        ItemStack stack = inventory.getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();
        if ( meta == null ) return;

        if ( meta.hasEnchant(Enchantment.SILK_TOUCH) ) {
            meta.removeEnchant(Enchantment.SILK_TOUCH);
            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 8, true);
        } else {
            meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            meta.addEnchant(Enchantment.SILK_TOUCH, 8, true);
        }

        meta.setUnbreakable(false);

        stack.setItemMeta(meta);

        player.damageItemStack(stack, 20);

        inventory.setItemInMainHand(stack);

        event.setCancelled(true);

        new PlayerUser(event.getPlayer()).infoActionBar("模式已切换!").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {
        new PlayerUser(event.getPlayer()).infoActionBar("蹲下右键以切换模式!");
    }

    public static List<Location> generateCubeBorder(Location center, int radius) {
        List<Location> borderLocations = new ArrayList<>();

        World world = center.getWorld();
        int minX = center.getBlockX() - radius;
        int minY = center.getBlockY() - radius;
        int minZ = center.getBlockZ() - radius;
        int maxX = center.getBlockX() + radius;
        int maxY = center.getBlockY() + radius;
        int maxZ = center.getBlockZ() + radius;

        generateCubeBorders(borderLocations, world, minX, minY, minZ, maxX, maxY, maxZ);

        return borderLocations;
    }

    private static void generateCubeBorders(List<Location> borderLocations, World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                addLocationToBorder(borderLocations, world, x, y, minZ);
                addLocationToBorder(borderLocations, world, x, y, maxZ);
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                addLocationToBorder(borderLocations, world, x, minY, z);
                addLocationToBorder(borderLocations, world, x, maxY, z);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ + 1; z < maxZ; z++) {
                addLocationToBorder(borderLocations, world, minX, y, z);
                addLocationToBorder(borderLocations, world, maxX, y, z);
            }
        }
    }

    private static void addLocationToBorder(List<Location> borderLocations, World world, int x, int y, int z) {
        Location borderLocation = new Location(world, x + 0.5, y + 0.5, z + 0.5);
        borderLocations.add(borderLocation);
    }

    private void onBreakOre(Block block) {
        Material type = block.getType();

        List<Ray> rays = new ArrayList<>();

        int radius = 0;
        int amo = 0;

        while ( amo < 9 && radius < 4 ) {

            List<Location> locations = generateCubeBorder(block.getLocation(), radius);

            for (Location location : locations) {

                if ( amo > 8 ) break;

                Block b = block.getWorld().getBlockAt(location);
                if ( b.getType() == type ) {

                    amo += 1;

                    // draw line
                    Ray ray = new Ray(block.getLocation().add(0.5, 1.5, 0.5), b.getLocation().getDirection(), 16, 0.2);

                    ray.setPeriod(20);
                    ray.setParticle(Particle.DRIP_LAVA);

                    rays.add(ray);
                }

            }

            radius += 1;

        }


//        while ( amo < 9 && radius < 8 ) {
//
//            Location corner = block.getLocation().add(0.5, 0.5, 0.5);
//
//            // 搜寻半径为r的方块
//            for (int i = -radius; i < radius; i++ ) {
//                Block b = block.getRelative(i, 0, 0);
//                if ( b.getType() == type ) {
//                    amo += 1;
//
//                    // draw line
//                    Ray ray = new Ray(block.getLocation().add(0.5, 1.5, 0.5), b.getLocation().getDirection(), 16, 0.2);
//
//                    ray.setPeriod(20);
//                    ray.setParticle(Particle.BLOCK_DUST);
//
//                    rays.add(ray);
//
//                    break;
//                }
//            }
//
//            radius += 1;
//
//        }

//        for ( int x = -1; x <= 4; x++ ) {
//            for ( int y = -1; y <= 4; y++ ) {
//                for ( int z = -1; z <= 4; z++ ) {
//
//                    if ( amo > 9 ) {
//                        return;
//                    }
//
//                    Block b = block.getRelative(x, y, z);
//                    if ( b.getType() == type ) {
//
//                        amo+=1;
//
//                        // draw line
//                        Ray ray = new Ray(block.getLocation().add(0.5, 1.5, 0.5), b.getLocation().getDirection(), 16, 0.2);
//
//                        ray.setPeriod(20);
//                        ray.setParticle(Particle.BLOCK_DUST);
//
//                        rays.add(ray);
//
//                    }
//                }
//            }
//        }

        long c = System.currentTimeMillis();

        new BukkitRunnable() {

            @Override
            public void run() {
                long diff = System.currentTimeMillis() - c;

                if ( diff >= 8000 ) {
                    rays.clear();

                    cancel();
                    return;
                }

                rays.forEach(Ray::show);

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 10);

    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {
        Block block = event.getBlock();

        World world = block.getWorld();

        if ( block.getType().name().contains("ORE") ) {

            new PlayerUser(event.getPlayer())
                    .playSound(Sound.BLOCK_NOTE_BLOCK_CHIME)
                    .infoActionBar("花髋血蔷 已触发...");

            onBreakOre(block);

            return super.useItemBreakBlock(playerData, event);
        }

        if ( !(world.getName().contains("nether")) ) {

            return super.useItemBreakBlock(playerData, event);

        }

        if ( block.getType() != Material.NETHERRACK ) return super.useItemBreakBlock(playerData, event);

        for ( int x = -1; x <= 3; x++ ) {
            for ( int y = -1; y <= 3; y++ ) {
                for ( int z = -1; z <= 3; z++ ) {
                    Block b = block.getRelative(x, y, z);
                    if ( b.getType() == Material.NETHERRACK ) {

                        BlockBreakEvent _event = new BlockBreakEvent(b, event.getPlayer());

                        if ( !_event.isCancelled() )
                            b.breakNaturally(event.getPlayer().getItemInHand());
                    }
                }
            }
        }

        return super.useItemBreakBlock(playerData, event);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(45));
    }
}
