package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.environment.blood_moon;

import com.talexs.talexessential.data.PlayerData;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.talexs.talexessential.TalexEssential;
import org.bukkit.util.Vector;
import top.zoyn.particlelib.pobject.Cube;
import top.zoyn.particlelib.pobject.Ray;

import java.util.Random;

/**
 * <br /> {@link pubsher.talexsoultech.talex.environment.blood_moon }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 20:20 <br /> Project: TalexSoulTech <br />
 */
@Data
@Accessors( chain = true )
public class BloodMoonCreator {

    public static boolean start = false;

    private World world;

    private boolean stop = false;

    private long startTime = System.currentTimeMillis();

    private EntityType[] willEntities = new EntityType[]
            {

                    EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER, EntityType.WITCH,
                    EntityType.ZOMBIE_VILLAGER, EntityType.CAVE_SPIDER, EntityType.DROWNED, EntityType.VEX, EntityType.CAVE_SPIDER,
                    EntityType.PHANTOM, EntityType.EVOKER

            };
    private Particle particles[] = new Particle[] {

            Particle.FLAME, Particle.FALLING_DUST, Particle.CRIT, Particle.LAVA, Particle.CRIT_MAGIC, Particle.CRIT_MAGIC

    };

    public BloodMoonCreator(World world) {

        this.world = world;

    }

    public void start() {
        if ( start ) return;

        start = true;

        broadcastMessages();

        runner();

    }

    private void broadcastMessages() {

        for ( Player player : world.getPlayers() ) {

            player.sendTitle("§4§l℘", "§c异血猩红素 §b已爆发!", 12, 50, 20);
            player.sendActionBar("§4§l℘");

            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);

        }

    }

    private void broadcastMessagesEnd() {

        for ( Player player : world.getPlayers() ) {

            player.sendTitle("§8§l℘", "§c异血猩红素 §b已被抑制!", 15, 50, 40);
            player.sendActionBar("§8§l℘");

            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0f, 1.0f);

        }

        start = false;

    }

    private void runner() {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( stop ) {

                    broadcastMessagesEnd();

                    cancel();
                    return;

                }

                if ( System.currentTimeMillis() - startTime > 300000 ) {

                    stop = true;

                }

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        t_run();

                    }

                }.runTask(TalexEssential.getInstance());

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 50, 10);

    }

    private void t_run() {

        Random random = new Random();

        for ( Player player : world.getPlayers() ) {

            Location location = player.getLocation();
            if ( !location.getWorld().getName().equalsIgnoreCase("world") ) continue;

            PlayerData pd = PlayerData.g(player);

            if ( !pd.getPlayerSoul().doCategoryUnlock("st_magic") ) continue;

            if ( player.getGameMode() != GameMode.SURVIVAL ) continue;

            player.sendActionBar("§4§l℘");

            player.sendTitle("§4§l℘", "§c猩红素印记", 0, 20, 10);

            if ( random.nextBoolean() ) {

                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.75f, 1.0f);
                player.playSound(location, Sound.BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON, 0.255f, 1.0f);
                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BIT, 0.255f, 1.0f);

            }

            if ( random.nextBoolean() ) {

                player.playSound(location, Sound.ENTITY_HORSE_SADDLE, 0.65f, 1.0f);
                player.playSound(location, Sound.ENTITY_ZOMBIE_HORSE_AMBIENT, 0.45f, 1.0f);
                player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.45f, 1.0f);

            }

            if ( random.nextBoolean() ) {

                player.playSound(location, Sound.BLOCK_NOTE_BLOCK_BELL, 0.35f, 1.0f);
                player.playSound(location, Sound.ENTITY_BLAZE_BURN, 0.45f, 1.0f);
                player.playSound(location, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 0.45f, 1.0f);

            }

            if ( random.nextBoolean() ) {

                player.playSound(location, Sound.ENTITY_PARROT_IMITATE_CREEPER, 0.25f, 1.0f);
                player.playSound(location, Sound.ENTITY_WOLF_SHAKE, 0.85f, 1.0f);
                player.playSound(location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.85f, 1.0f);

            }

            playChunkParticle(location);

            if ( random.nextDouble() < 0.25 ) {

                int amo = random.nextInt(10);

                for ( int i = 0; i < amo; ++i ) {

                    Location loc = location.add(random.nextInt(8) * ( random.nextBoolean() ? -1 : 1 ), random.nextInt(8), random.nextInt(8) * ( random.nextBoolean() ? -1 : 1 ));

                    loc.getWorld().spawnEntity(loc, willEntities[random.nextInt(willEntities.length - 1)]);

                }

            }

            if ( random.nextDouble() < .00725 && player.isFlying() && !player.isOnGround() ) {

                Vector velocity = player.getVelocity();

                velocity.multiply(-.75f).setY(-1.5);

                player.setVelocity(velocity);

            }

            if ( random.nextDouble() < .00125 ) {
                player.getWorld().createExplosion(location.add(0, 1.25, 0), 14.5f, false, false);
            }

            if ( random.nextDouble() < .0725 ) {

                player.teleport(location.add(random.nextInt(8), random.nextInt(8), random.nextInt(8)));

            }

            if ( random.nextDouble() < .00725 && (player.isInWaterOrBubbleColumn() || player.isInLava()) ) {

                Vector velocity = player.getVelocity();

                velocity.multiply(1.25f).setY(2.75);

                player.setVelocity(velocity);

                player.damage(player.getMaxHealth() * .5);

                player.setFoodLevel(player.getFoodLevel() - 2);

            }

        }

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

            if ( random.nextDouble() < 0.01275 ) {

                Cube cube = new Cube(main.getBlock().getLocation().clone().add(0.1, 0.1, 0.1), main.getBlock().getLocation().clone().add(.9, .9, .9));
                cube.setPeriod(20L)
                        .setColor(Color.GRAY)
                        .show();

                main.getWorld().strikeLightning(main);

            }

        }

    }

}
