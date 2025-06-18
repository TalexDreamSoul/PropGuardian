package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.utils;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.utils.MathUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.talexs.talexessential.TalexEssential;
import top.zoyn.particlelib.utils.coordinate.PlayerBackCoordinate;

import java.util.Random;
import java.util.Set;

public class ParticleUtil {

    public static void StraightLine(Location from, Location to, Particle particle, double delay) {

        double distance = from.distance(to);
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double z = to.getZ() - from.getZ();
        double perX = x / distance;
        double perY = y / distance;
        double perZ = z / distance;

        for ( double a = 0; a < distance; a = a + 0.2 ) {

            Location loc = from.clone().add(perX * ( a + 0.2 ), perY * ( a + 0.2 ), perZ * ( a + 0.2 ));
            new BukkitRunnable() {

                @Override
                public void run() {

                    loc.getWorld().spawnParticle(particle, loc, 1, 0, 0, 0, 0.01);

                }
            }.runTaskLater(TalexEssential.getInstance(), (long) ( delay * a ));

        }

    }

    public static void StraightLine(Location from, Location to, Particle particle) {

        double distance = from.distance(to);
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double z = to.getZ() - from.getZ();
        double perX = x / distance;
        double perY = y / distance;
        double perZ = z / distance;

        for ( double a = 0; a < distance; a = a + 0.2 ) {

            Location loc = from.clone().add(perX * ( a + 0.2 ), perY * ( a + 0.2 ), perZ * ( a + 0.2 ));
            loc.getWorld().spawnParticle(particle, loc, 1, 0, 0, 0, 0.0001);

        }

    }

    public static void CircleJumpUp(Player player, Particle particle, int amplitude) {

        final int[] times = { 0 };
        Random random = new Random();

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( player == null || !player.isOnline() ) {

                    return;

                }
                if ( times[0] >= 360 ) {

                    cancel();

                }

                int finalDegree = 360 - times[0];
                Location location = player.getLocation();
                double radians = Math.toRadians(finalDegree);
                double x = Math.cos(radians);
                double y = Math.sin(radians);

                location.getWorld().spawnParticle(particle, location.add(x, random.nextInt(amplitude), y), 1, 0, 0, 0, 0.000001);
                location.subtract(x, 0, y);

                times[0]++;

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 0, 1);

       /* for (int degree = 0; degree < 360; degree++) {

            if(player == null || !player.isOnline()){

                return;

            }
            int finalDegree = degree;
            new BukkitRunnable() {
                @Override
                public void run() {

                    Location location = player.getLocation();
                    double radians = Math.toRadians(finalDegree);
                    double x = Math.cos(radians);
                    double y = Math.sin(radians);
                    location.add(x, perLevelUp * times[0], y);
                    location.getWorld().spawnParticle(particle,location, 1,0,0,0,0.01);
                    location.subtract(x, 0, y);
                    times[0]++;

                }
            }.runTaskLater(TalexParticles.getInstance(),times[0] * 5);

        }*/

    }

    public static void CircleUp(Player player, double levelUpAmount, Particle particle) {

        double perLevelUp = levelUpAmount / 360;
        final int[] times = { 0 };

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( player == null || !player.isOnline() ) {

                    return;

                }
                if ( times[0] >= 360 ) {

                    cancel();

                }
                int finalDegree = 360 - times[0];
                Location location = player.getLocation();
                double radians = Math.toRadians(finalDegree);
                double x = Math.cos(radians);
                double y = Math.sin(radians);
                location.add(x, perLevelUp * times[0], y);
                location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0.000001);
                location.subtract(x, 0, y);

                times[0]++;

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 0, 1);

       /* for (int degree = 0; degree < 360; degree++) {

            if(player == null || !player.isOnline()){

                return;

            }
            int finalDegree = degree;
            new BukkitRunnable() {
                @Override
                public void run() {

                    Location location = player.getLocation();
                    double radians = Math.toRadians(finalDegree);
                    double x = Math.cos(radians);
                    double y = Math.sin(radians);
                    location.add(x, perLevelUp * times[0], y);
                    location.getWorld().spawnParticle(particle,location, 1,0,0,0,0.01);
                    location.subtract(x, 0, y);
                    times[0]++;

                }
            }.runTaskLater(TalexParticles.getInstance(),times[0] * 5);

        }*/

    }

    public static void Circle(Location location, Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double extra, double width, double length) {

        Set<Location> locs = MathUtil.findOval(location.clone(), width, length);

        for ( Location loc : locs ) {

            loc.getWorld().spawnParticle(particle, loc, amount, offsetX, offsetY, offsetZ, extra);

        }


    }

    public static void Circle(Location location, Particle particle, int amount, double width, double length) {

        Circle(location, particle, amount, 0.1, 0.1, 0.1, 0, width, length);

    }

    public static void Circle(Location location, Particle particle, double width, double length) {

        Circle(location, particle, 1, width, length);

    }

}
