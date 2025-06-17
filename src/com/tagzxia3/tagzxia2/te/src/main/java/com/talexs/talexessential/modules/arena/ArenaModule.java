package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.arena;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.utils.NBTsUtil;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import top.zoyn.particlelib.pobject.Cube;
import top.zoyn.particlelib.pobject.Ray;
import top.zoyn.particlelib.utils.matrix.Matrixs;

import java.text.SimpleDateFormat;
import java.util.*;

public class ArenaModule extends BaseModule {
    public ArenaModule() {
        super("arena");
    }

    static Map<String, Cube> genParticleCubes = new HashMap<>();

    private void refresh() {
        long diff = System.currentTimeMillis() - startTime;

        // 计算开启多少分钟了
        long min = (diff / 1000 / 60) + 1;

        // 每开启1分钟就在 locs 里取一个点
        int index = (int) (min % locs.size());
        if ( index >= 3 ) index = 3;

        // 取出点
        List<Location> thisLocs = locs.subList(0, index + 1);

        thisLocs.forEach(loc -> {
            genMonster((int) min, loc);
            genParticle(loc);

            playChunkParticle(loc);
        });

        genMonster((int) min, center);
        genParticle(center);

        Location location = activePlayer.getLocation();

        if ( min > 20 ) {
            genMonster((int) min, location);
        }

        if ( Math.random() < .001325 ) {
            activePlayer.getWorld().createExplosion(location.add(0, -.25, 0), 2.5f, false, false);
        }

        if ( Math.random() < .000725 ) {

            Random random = new Random();
            activePlayer.teleport(center.clone().add(random.nextInt(8), random.nextInt(5), random.nextInt(8)));

        }

        playChunkParticle(location);

        if ( Math.random() < .0325 && activePlayer.isFlying() && !activePlayer.isOnGround() ) {

            Vector velocity = activePlayer.getVelocity();

            velocity.multiply(-.75f).setY(-1.5);

            activePlayer.setVelocity(velocity);

        }

        if ( Math.random() < .00225 ) {

            Vector velocity = activePlayer.getVelocity();

            velocity.multiply(1.75f).setY(1.45);

            activePlayer.setVelocity(velocity);

        }

        if ( Math.random() < .00325 ) {

            activePlayer.clearActivePotionEffects();

            new PlayerUser(activePlayer).triggerDarkness()
                    .triggerEffect(PotionEffectType.SLOW);

        }

        if ( Math.random() < .00725 + (min * 0.0125) ) {

            activePlayer.setSaturation(0);

        }

        if ( location.getWorld() != center.getWorld() || location.distance(center) > 64 ) {

           activePlayer.teleport(center);

        }

        if ( Math.random() < 0.00175 + (min * 0.0125) ) {

            activePlayer.damage(activePlayer.getHealth() * .35);

        }

        String format = new SimpleDateFormat("mm:ss").format(new Date(diff));

        activePlayer.sendActionBar("§e§l已坚持: §c§l" + format);
    }

    private void playChunkParticle(Location loc) {

        Random random = new Random();

        int amo = random.nextInt(100);

        for ( int i = 0; i < amo; ++i ) {

            Location main = loc.clone().add(random.nextInt(12) * ( random.nextBoolean() ? -1 : 1 ), random.nextInt(12) - 3, random.nextInt(12) * ( random.nextBoolean() ? -1 : 1 ));

            if ( random.nextDouble() < 0.025 ) {

                Ray ray = new Ray(main, loc.clone().add(random.nextInt(3), random.nextInt(3), random.nextInt(3)).getDirection(), 10, 0.2);

                ray.setPeriod(40);
                ray.setParticle(particles[random.nextInt(particles.length - 1)]);

                ray.show();

            }

            main.getWorld().spawnParticle(Particle.CLOUD, main, 0);
            main.getWorld().spawnParticle(Particle.CRIT_MAGIC, main, 1, 0, 0, 0, 0.0001);

            if ( random.nextDouble() < 0.25 ) {

                main.getWorld().spawnParticle(Particle.LAVA, main, 3, 0, 0, 0, 0.0001);

            }

            if ( random.nextDouble() < 0.00275 ) {

                Cube cube = new Cube(main.getBlock().getLocation().clone().add(0.1, 0.1, 0.1), main.getBlock().getLocation().clone().add(2.9, 2.9, 2.9));
                cube.setPeriod(20L)
                        .setColor(Color.GRAY)
                        .show();

                main.getWorld().strikeLightning(main);

            }

        }

    }

    private void genMonster(int min, Location loc) {
        // 根据分钟决定召唤的怪物，每分钟多一种
//        int index = min % willEntities.length;
        int start = Math.min(min / 2, 15);
        List<EntityType> thisEntities = Arrays.asList(willEntities).subList(start, Math.min(start + 5, 20));

        // 根据坚持时间决定召唤几率，初始0.125 每分钟增加0.05 | 并且每.5分钟增加一只数量
        double rate = 0.25 + ((double) min / 2) * 0.05;
        if ( Math.random() < rate ) {
            Random random = new Random();

            for ( int i = 0;i < random.nextInt(5); ++i ) {

                EntityType entityType = thisEntities.get(random.nextInt(thisEntities.size()));

                Entity entity = loc.clone().add(random.nextInt(3), random.nextInt(2), random.nextInt(3)).getWorld().spawnEntity(loc, entityType);

                LivingEntity le = (LivingEntity) entity;

                if ( min > 10 ) {

                    AttributeInstance attribute = le.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);

                    if ( attribute != null )
                        attribute.setBaseValue(attribute.getDefaultValue() + .00725 + (min * 0.0525));

                }

                if ( min > 15 ) {

                    AttributeInstance attribute = le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                    if ( attribute != null )
                        attribute.setBaseValue(attribute.getBaseValue() + .000125 + (min * 0.00325));

                }

            }

        }

        if ( Math.random() < .00475 ) {
            activePlayer.teleport(loc);
        }

        if ( Math.random() < .000325 ) {

            Random random = new Random();
            for ( int i =0; i < random.nextInt(3 + min); ++i ) {
                activePlayer.getWorld().strikeLightning(activePlayer.getLocation().add(random.nextInt(3), random.nextInt(3), random.nextInt(3)));
            }

        }
    }

    private void genParticle(Location loc) {
        Cube cube = genParticleCubes.getOrDefault(NBTsUtil.Location2String(loc), new Cube(loc.clone().add(.1, 1.4, .1), loc.clone().add(.9, .9, .9)));

        cube.setColor(Color.PURPLE);

        cube.addMatrix(Matrixs.rotateAroundXAxis(4));
        cube.addMatrix(Matrixs.rotateAroundZAxis(4));
        cube.addMatrix(Matrixs.rotateAroundYAxis(2));

        genParticleCubes.put(NBTsUtil.Location2String(loc), cube);

        cube.show();

        if ( Math.random() < .0025 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 1);

        }

        if ( Math.random() < .00125 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        }

        if ( Math.random() < .0125 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

        }

        if ( Math.random() < .00325 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);

        }

        if ( Math.random() < .0008125 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);

        }

        if ( Math.random() < .000203125 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);

        }

        if ( Math.random() < .05078125 ) {

            activePlayer.playSound(activePlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1, 1);

        }
    }

    private static Location center;

    private static List<Location> locs = new ArrayList<>();

    public static Player activePlayer;

    public static long startTime;

    public static boolean begin = false, preBegin = false;

    private static EntityType[] willEntities = new EntityType[]
            {

                    EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER, EntityType.WITCH,
                    EntityType.ZOMBIE_VILLAGER, EntityType.CAVE_SPIDER, EntityType.DROWNED, EntityType.VEX, EntityType.CAVE_SPIDER,
                    EntityType.PHANTOM, EntityType.EVOKER, EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.BLAZE,
                    EntityType.ALLAY, EntityType.AXOLOTL, EntityType.SLIME, EntityType.WITHER_SKELETON, EntityType.SKELETON_HORSE

            };
    private static Particle particles[] = new Particle[] {

            Particle.FLAME, Particle.CRIT, Particle.LAVA, Particle.CRIT_MAGIC, Particle.CRIT_MAGIC

    };

    public static void start(Player player) {
        activePlayer = player;
        startTime = System.currentTimeMillis();

        begin = true;
        preBegin = true;

        player.teleport(center.clone().add(0, 1.5, 0));
        player.setGameMode(GameMode.SPECTATOR);

        PlayerUser playerUser = new PlayerUser(player);

        playerUser.errorActionBar("竞技场即将开启！")
                .playSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1)
                .playSound(Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1)
                .title("&c&l3", "&8- &a竞技场即将开启 &8-", 0, 20, 10)
                .delayRun(new PlayerDataRunnable() {
                    @Override
                    public void run() {
                        playerUser.title("&c&l3", "&8- &a竞技场即将开启 &8-", 0, 20, 10)
                                .playSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1)
                                .playSound(Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1)
                                .delayRun(new PlayerDataRunnable() {
                                    @Override
                                    public void run() {
                                        playerUser.title("&c&l2", "&8- &a竞技场即将开启 &8-", 0, 20, 10)
                                                .playSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1)
                                                .playSound(Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1)
                                                .delayRun(new PlayerDataRunnable() {
                                                    @Override
                                                    public void run() {
                                                        playerUser.title("&c&l1", "&8- &a竞技场即将开启 &8-", 0, 20, 10)
                                                                .playSound(Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1)
                                                                .playSound(Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1)
                                                                .delayRun(new PlayerDataRunnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        playerUser.title("&c&lGO", "&8- &a竞技场挑战开启 &8-", 0, 20, 30)
                                                                                .playSound(Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1)
                                                                                .playSound(Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1)
                                                                        ;

                                                                        preBegin = false;
                                                                        player.teleport(center.clone().add(0, 1.5, 0));
                                                                        player.setGameMode(GameMode.ADVENTURE);
                                                                    }
                                                                }, 20);
                                                    }
                                                }, 20);
                                    }
                                }, 20)
                        ;
                    }
                }, 20)
        ;
    }


    private static void end() {
        center.getWorld().getNearbyEntities(center, 48, 48, 48).forEach(entity -> {
            if ( !(entity instanceof Player) && Arrays.stream(willEntities).anyMatch(type -> type.equals(entity.getType())) ) {
                entity.remove();
            }
        });

        locs.forEach(loc -> loc.getWorld().createExplosion(center, 5.5f, false, false));

        PlayerData pd = PlayerData.g(activePlayer);
        long lastDiff = pd.getInfo().getLong("Arena.Monster.time", 0);

        long diff = System.currentTimeMillis() - startTime;
        String format = new SimpleDateFormat("mm:ss:SSS").format(new Date(diff));

        int amo = pd.getInfo().getInt("Arena.Monster.amo", 0) + 1;
        pd.getInfo().set("Arena.Monster.time", Math.max(diff, lastDiff));

        pd.getInfo().set("Arena.Monster.amo", amo);

        Bukkit.broadcastMessage("§7[§b怪物竞技场§7] §e" + activePlayer.getName() + " §c在第 §3§l" + amo + " §c场怪物竞技场中坚持时间 §2§l" + format + " §c！");

        begin = false;
        activePlayer = null;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if ( begin && event.getPlayer().equals(activePlayer) ) {
            if ( preBegin ) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if ( begin && event.getEntity().equals(activePlayer) ) {
            end();
        }
    }

    @EventHandler
    public void onOut(PlayerQuitEvent event) {
        if ( begin && event.getPlayer().equals(activePlayer) ) {
            end();
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if ( begin && event.getPlayer().equals(activePlayer) ) {
            if ( event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL ) {
                event.setCancelled(true);
            } else if ( event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT ) {
                event.setCancelled(true);
            } else if ( event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN ) {

            } else end();
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        if ( begin && event.getPlayer().equals(activePlayer) ) {
            if ( event.getItem() instanceof ExperienceOrb ) return;
            event.setCancelled(true);

            event.getItem().remove();
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if ( !begin || !event.getEntity().equals(activePlayer) ) return;

        Entity entity = event.getDamager();

        long diff = System.currentTimeMillis() - startTime;
        int min = (int) (diff / 1000 / 60);

        if ( min >= 5 ) {
            double damage = event.getDamage();
            double health = activePlayer.getHealth();

            event.setDamage(Math.min(damage * (1 - (health / 20) * ((double) (min - 5) / 10)), damage * 1.5));
        }
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event) {
        if ( begin && event.getPlayer().equals(activePlayer) ) {
            if ( !activePlayer.hasPermission("talex.essential.arena.bypass") ) event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        center = NBTsUtil.getLocation(Objects.requireNonNull(yaml.getString("Settings.spawn"))).add(.5, .5, .5);

        yaml.getStringList("Settings.locs").forEach(loc -> {
            Location location = NBTsUtil.getLocation(loc);
            locs.add(location);
//            assert location != null;
//            Cube cube = new Cube(location.subtract(3, 3, 3), location.add(3, 3, 3));
//            cube.setColor(Color.PURPLE)
//                    .addMatrix(Matrixs.rotateAroundXAxis(45))
//                    .addMatrix(Matrixs.rotateAroundZAxis(45));

//            genParticleCubes.put(loc, cube);
        });

        new BukkitRunnable() {
            @Override
            public void run() {

                if ( activePlayer != null && begin && !preBegin )
                    refresh();

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 10);
    }
}
