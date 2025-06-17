package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.resource;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.modules.BaseModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.kyori.adventure.util.TriState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.xml.sax.helpers.LocatorImpl;

import java.util.*;

@Getter
public class ResModule extends BaseModule {

    public static ResModule INS;

    public ResModule() {
        super("resource");
        INS = this;
    }

    @Override
    public void onEnable() {
        this.world = Bukkit.getWorld("resource");
        if ( this.world == null ) {
            genWorld();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                ResModule.this.run();
            }
        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 0, 20 * 60 * 5);
    }

    @Override
    protected boolean configurable() {
        return false;
    }

    private Map<String, List<String>> damageMapper = new HashMap<>();

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if ( player.getWorld() == world ) return;

        damageMapper.remove(player.getName());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if ( player.getWorld() != world ) return;

        PlayerData pd = PlayerData.g(player);
        com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes pr = pd.getPlayerRes();

        pr.addFailed();

        pr.getSlotMapper().values().forEach(slot -> {
            ItemStack stack = slot.getStack();
            if ( stack != null ) {
                player.getWorld().dropItemNaturally(player.getLocation(), stack);
            }
        });

        pr.clear();
        pr.save();
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if ( !(entity instanceof Player) ) return;

        Player player = (Player) entity;
        if ( player.getWorld() != world ) return;

        PlayerData pd = PlayerData.g(player);
        com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes pr = pd.getPlayerRes();

        Item item = event.getItem();

        try {

            ItemStack left = pr.mergeItem(item.getItemStack());
            event.setCancelled(true);

            if ( left != null ) {
                item.setItemStack(left);
            } else {
                item.remove();
                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
            }

        } catch ( RuntimeException e ) {

        }

    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if ( player.getWorld() != world ) return;

        if ( player.hasPermission("talex.res.bypass") ) return;
        
        event.setCancelled(true);
    }

//    @EventHandler
//    public void onDamaged(EntityDamageByEntityEvent event) {
//        Entity entity = event.getEntity();
//        Entity damager = event.getDamager();
//        if ( !(entity instanceof Player) || !(damager instanceof Player) ) return;
//
//        Player player = (Player) entity;
//        Player target = (Player) damager;
//
//        if ( player.getWorld() != world ) return;
//
//        int level = player.getLevel();
//        int targetLevel = target.getLevel();
//
//        List<String> sets = damageMapper.getOrDefault(player.getName(), new ArrayList<>());
//
////        System.out.println("level: " + level + ", targetLevel: " + targetLevel);
////        System.out.println(sets);
////        System.out.println(sets.contains(player.getName()));
////        System.out.println(damageMapper);
//
//        if ( targetLevel - level > 10 ) {
//            if ( !sets.contains(target.getName()) ) {
//                // 无法攻击
//                target.sendActionBar("§c你无法攻击比你等级低的玩家!");
//                target.playSound(target, Sound.BLOCK_ANVIL_HIT, 1, 1);
//                event.setCancelled(true);
//                return;
//            }
//
//        }
//
//        double damage = event.getDamage();
//        if ( damage >= player.getMaxHealth() ) {
//            target.sendActionBar("§c暴击秒杀伤害是不允许的!");
//            target.playSound(target, Sound.BLOCK_ANVIL_LAND, 1, 1);
//            event.setCancelled(true);
//            return;
//        }
//
//        if ( !sets.contains(player.getName()) )
//            sets.add(target.getName());
//        damageMapper.put(player.getName(), sets);
//    }

    private long lastUpd;

    private World world;

    private void genWorld() {
        if ( this.world != null ) {
            this.world.getPlayers().forEach(player -> {
                PlayerData pd = PlayerData.g(player);
                PlayerRes pr = pd.getPlayerRes();

                if ( pr.saveLoc != null )
                    player.teleport(pr.saveLoc);
            });

            TalexEssential.getInstance().log("[Resource] Unload old world resource ...");
            Bukkit.unloadWorld("resource", false);
        }

        lastUpd = System.currentTimeMillis();
//        WorldCreator wc = new WorldCreator("resource");
//
//        wc.environment(World.Environment.NORMAL);
//        wc.generateStructures(true);
//        wc.keepSpawnLoaded(TriState.FALSE);
//        wc.type(WorldType.NORMAL);
//
//        wc.generator(new ResChunkGenerator());

        this.world = Bukkit.createWorld(new WorldCreator("resource")
                .environment(World.Environment.NORMAL)
                .generateStructures(true)
                .keepSpawnLoaded(TriState.FALSE)
                .type(WorldType.NORMAL)
                .seed(System.currentTimeMillis())
        );

        this.world.setDifficulty(Difficulty.HARD);
        this.world.setGameRule(GameRule.KEEP_INVENTORY, true);

        TalexEssential.getInstance().log("[Resource] World resource generated.");
    }

    private void run() {
        long now = System.currentTimeMillis();
        if (now - lastUpd < 1000 * 60 * 60 * 24) {
            return;
        }

        // check if time equals to 早上凌晨4:00
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        if (calendar.get(Calendar.HOUR_OF_DAY) != 4) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                genWorld();
            }
        }.runTask(TalexEssential.getInstance());
    }

    private class ResChunkGenerator extends ChunkGenerator {

        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            TalexEssential.getInstance().log("[Resource] Generating chunk data ... (" + x + ", " + z + ")");

            ChunkData chunkData = createChunkData(world);

            int randomX = random.nextInt(16);
            int randomZ = random.nextInt(16);

            Location loc = new Location(world, randomX, 0, randomZ);
            Material type = chunkData.getBlockData(randomX, 0, randomZ).getMaterial();

            Object[] array = Arrays.stream(OreData.values()).filter(o -> o.material == type).toArray();
            if ( array.length < 1 ) return chunkData;

            OreData oreData = (OreData) array[0];//OreData.values()[random.nextInt(OreData.values().length)];
            if ( random.nextDouble() < oreData.getChance() && random.nextDouble() < 0.25 ) {
                // 以当前位置开始遍历生成矿脉
                genOreVein(chunkData, random, loc, oreData);
            }

            return chunkData;
        }

        private void genOreVein(ChunkData chunkData, Random random, Location mLoc, OreData oreData) {
            List<Location> locs = new ArrayList<>();

            int amo = 0;
            int size = (int) (oreData.getValuable() * 10 + 10);

            // 根据概率计算是否折半 或者 加倍
            size = (int) (size * (random.nextDouble() < 0.5 ? 2 : 0.5F));

            size = Math.min(size, 100);

            do {
                // 通过给定的点获取六个方位的点
                locs.addAll(getSixLocs(mLoc));

                Location loc = locs.get(0);
                locs.remove(0);

                BlockData blockData = chunkData.getBlockData(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                if ( blockData.getMaterial() != Material.STONE ) continue;
                chunkData.setBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), oreData.getMaterial());
//                if ( loc.getBlock().getType() != Material.STONE ) continue;
//                loc.getBlock().setType(oreData.getMaterial());
                amo+=1;

            } while( !locs.isEmpty() && amo < size );

        }

        private Set<Location> getSixLocs(Location mLoc) {
            Set<Location> locs = new HashSet<>();

            locs.add(mLoc.clone().add(1, 0, 0));
            locs.add(mLoc.clone().add(-1, 0, 0));
            locs.add(mLoc.clone().add(0, 1, 0));
            locs.add(mLoc.clone().add(0, -1, 0));
            locs.add(mLoc.clone().add(0, 0, 1));
            locs.add(mLoc.clone().add(0, 0, -1));

            return locs;
        }

    }

    @AllArgsConstructor
    @Getter
    private enum OreData {
        COAL(Material.COAL_ORE, 0.5, 1),
        IRON(Material.IRON_ORE, 0.1, 10),
        GOLD(Material.GOLD_ORE, 0.08, 30),
        DIAMOND(Material.DIAMOND_ORE, 0.05, 50),
        EMERALD(Material.EMERALD_ORE, 0.01, 100),
        REDSTONE(Material.REDSTONE_ORE, 0.3, 5),
        LAPIS(Material.LAPIS_ORE, 0.3, 5),
        ;

        private Material material;
        private double chance;
        private double valuable;
    }
}
