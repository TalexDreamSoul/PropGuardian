package com.tagzxia3.te.src.main.java.com.talexs.talexessential.utils;

import com.talexs.talexessential.GlobalListener;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import top.zoyn.particlelib.pobject.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationUtil {

    public static ItemStack getHead(String value) {

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        UUID hashAsId = UUID.nameUUIDFromBytes(value.getBytes());
        return Bukkit.getUnsafe().modifyItemStack(skull,
                "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + value + "\"}]}}}"
        );

    }

    public static Location standardLocation(Location loc) {

        Location newLoc = loc.clone();

        newLoc.setX(loc.getBlockX() + 0.5);
        newLoc.setY(loc.getBlockY());
        newLoc.setZ(loc.getBlockZ() + 0.5);
        newLoc.setPitch(0);
        float yaw = loc.getYaw();
        if ( yaw > -45 || yaw < -315 ) {
            yaw = 0;
        } else if ( yaw > -135 && yaw < -45 ) {
            yaw = -90f;
        } else if ( yaw > -225 && yaw < -135 ) {
            yaw = -180f;
        } else if ( yaw > -315 && yaw < -225 ) {
            yaw = -270f;
        }

        newLoc.setYaw(yaw);

        return newLoc;
    }

    // 判断 loc是否在x和y的中间
    public static boolean hasRegion(Location location, Location loc1, Location loc2) {

        double x1 = loc1.getX();
        double x2 = loc2.getX();
        double y1 = loc1.getY();
        double y2 = loc2.getY();
        double z1 = loc1.getZ();
        double z2 = loc2.getZ();
        double minY = Math.min(y1, y2);
        double maxY = Math.max(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxZ = Math.max(z1, z2);
        double minX = Math.min(x1, x2);
        double maxX = Math.max(x1, x2);
        if ( location.getWorld().equals(loc1.getWorld()) && location.getWorld().equals(loc2.getWorld()) ) {
            if ( location.getX() > minX && location.getX() < maxX ) {
                if ( location.getY() > minY && location.getY() < maxY ) {
                    return location.getZ() > minZ && location.getZ() < maxZ;
                }
            }
        }

        return false;
    }

    public static List<Block> getRangeBlocks(Location center, int radiusX, int radiusY, int radiusZ) {

        List<Block> block = new ArrayList<>();

        for ( int x = -radiusX; x < radiusX; ++x ) {

            for ( int y = -radiusY; y < radiusY; ++y ) {

                for ( int z = -radiusZ; z < radiusZ; ++z ) {

                    block.add(center.clone().add(x, y, z).getBlock());

                }

            }

        }

        return block;

    }

    public static List<Block> getRangeBlocks(Location mid, int rangeX, int rangeY) {

        int x, y, z;
        x = mid.getBlockX();
        y = mid.getBlockY();
        z = mid.getBlockZ();
        Location minLoc = new Location(mid.getWorld(), x - rangeX, y, z - rangeX);
        Location maxLoc = new Location(mid.getWorld(), x + rangeX, y + rangeY, z + rangeX);
        return getBlocks(minLoc, maxLoc);
    }

    // 获取两个点中间的所有点
    public static List<Location> getLocations(Location start, Location end) {

        if ( end.getWorld() != start.getWorld() ) {
            return new ArrayList<>();
        }
        List<Location> locations = new ArrayList<>();
        for ( int x = start.getBlockX(); x <= end.getBlockX(); x++ ) {
            for ( int y = start.getBlockY(); y <= end.getBlockY(); y++ ) {
                for ( int z = start.getBlockZ(); z <= end.getBlockZ(); z++ ) {
                    locations.add(new Location(start.getWorld(), x, y, z));
                }
            }
        }
        return locations;
    }

    public static @NotNull List<Block> getBlocks(Block start, Block end) {

        List<Block> blocks = new ArrayList<>();
        for ( Location loc : getLocations(start.getLocation(), end.getLocation()) ) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }

    public static List<Block> getBlocks(Location start, Location end) {

        List<Block> blocks = new ArrayList<>();
        for ( Location loc : getLocations(start, end) ) {
            blocks.add(loc.getBlock());
        }
        return blocks;
    }

    public static List<Entity> getNearbyEntities(Location where, double range, EntityType type) {

        List<Entity> found = new ArrayList<>();

        for ( Entity entity : where.getWorld().getEntities() ) {
            if ( getDistance(entity.getLocation(), where) <= range ) {
                if ( entity.getType() == type && !entity.isDead() ) {
                    found.add(entity);
                }
            }
        }
        return found;
    }

    public static List<Entity> getNearbyEntities(Location loc, int range) {

        List<Entity> entities = new ArrayList<>();

        for ( Entity entity : loc.getWorld().getEntities() ) {
            if ( isInBorder(loc, entity.getLocation(), range) ) {
                entities.add(entity);
            }
        }

        return entities;
    }

    public static boolean isInBorder(Location loc, Location loc2, int range) {

        int var4 = loc.getBlockX();
        int var5 = loc.getBlockZ();
        int var6 = loc2.getBlockX();
        int var7 = loc2.getBlockZ();
        return var6 < var4 + range && var7 < var5 + range && var6 > var4 - range && var7 > var5 - range;
    }

    public static double getDistance(Location loc1, Location loc2) {

        return Math.abs(loc1.getX() - loc2.getX()) + Math.abs(loc1.getY() - loc2.getY())
                + Math.abs(loc1.getZ() - loc2.getZ());
    }

    public static boolean isInBorder(Location center, Location notCenter, double range) {

        double x = center.getX(), z = center.getZ();
        double x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();

        return x1 < ( x + range ) && z1 < ( z + range ) && x1 > ( x - range ) && z1 > ( z - range );
    }

    public static Location getLocation(Location location, int x, int y, int z) {

        Location loc = location.getBlock().getLocation();
        loc.add(x, y, z);
        return loc;
    }

    public static Location getLocationYaw(Location location, double X, double Y, double Z) {

        Location loc = location;
        double radians = Math.toRadians(location.getYaw());
        double x = Math.cos(radians) * X;
        double z = Math.sin(radians) * X;
        loc.add(x, Y, z);
        loc.setPitch(0.0F);
        return loc;
    }

    public static Vector getPosition(Location location1, Location location2) {

        double X = location1.getX() - location2.getX();
        double Y = location1.getY() - location2.getY();
        double Z = location1.getZ() - location2.getZ();
        return new Vector(X, Y, Z);
    }

    public static Vector getPosition(Location location1, Location location2, double Y) {

        double X = location1.getX() - location2.getX();
        double Z = location1.getZ() - location2.getZ();
        return new Vector(X, Y, Z);
    }

    public static List<Player> getLocationPlayers(Location location) {

        List<Player> players = new ArrayList<Player>();
        for ( Player player : Bukkit.getOnlinePlayers() ) {
            if ( player.getWorld() == location.getWorld() && (int) location.getX() == (int) player.getLocation().getX() && (int) location.getY() == (int) player.getLocation().getY() && (int) location.getZ() == (int) player.getLocation().getZ() ) {
                players.add(player);
            }
        }
        return players;
    }

    public static int itemSize(Location var1, Material var2) {

        int var3 = 0;
        for ( Entity entity : getNearbyEntities(var1, 5) ) {
            if ( entity instanceof Item ) {
                Item item = (Item) entity;
                if ( item.getItemStack().getType() == var2 ) {
                    var3 += item.getItemStack().getAmount();
                }
            }
        }

        return var3;
    }

    public static Location[] fixShafts(Location loc1, Location loc2) {
        double x1 = loc1.getBlockX();
        double y1 = loc1.getBlockY();
        double z1 = loc1.getBlockZ();

        double x2 = loc2.getBlockX();
        double y2 = loc2.getBlockY();
        double z2 = loc2.getBlockZ();

        double maxX1 = Math.max(x1, x2);
        double maxY1 = Math.max(y1, y2);
        double maxZ1 = Math.max(z1, z2);

        double minX1 = Math.min(x1, x2);
        double minY1 = Math.min(y1, y2);
        double minZ1 = Math.min(z1, z2);

        // 获得这个矩形的外12棱（24个点）
        Location pos1 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, minZ1 - .1);
        Location pos2 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, minZ1 - .1);

        Location pos3 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, minZ1 - .1);
        Location pos4 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, minZ1 - .1);

        Location pos5 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, maxZ1 + 1.1);
        Location pos6 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, maxZ1 + 1.1);

        Location pos7 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, maxZ1 + 1.1);
        Location pos8 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, maxZ1 + 1.1);

        Location pos9 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, minZ1 - .1);
        Location pos10 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, maxZ1 + 1.1);

        Location pos11 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, minZ1 - .1);
        Location pos12 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, minZ1 - .1);

        Location pos13 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, minZ1 - .1);
        Location pos14 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, maxZ1 + 1.1);

        Location pos15 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, minZ1 - .1);
        Location pos16 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, maxZ1 + 1.1);

        Location pos17 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, minZ1 - .1);
        Location pos18 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, maxZ1 + 1.1);

        Location pos19 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, minZ1 - .1);
        Location pos20 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, minZ1 - .1);

        Location pos21 = new Location(loc1.getWorld(), minX1 - .1, maxY1 + 1.1, maxZ1 + 1.1);
        Location pos22 = new Location(loc1.getWorld(), minX1 - .1, minY1 - .1, maxZ1 + 1.1);

        Location pos23 = new Location(loc1.getWorld(), maxX1 + 1.1, maxY1 + 1.1, maxZ1 + 1.1);
        Location pos24 = new Location(loc1.getWorld(), maxX1 + 1.1, minY1 - .1, maxZ1 + 1.1);

        return new Location[] {
                pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9, pos10, pos11, pos12,
                pos13, pos14, pos15, pos16, pos17, pos18, pos19, pos20, pos21, pos22, pos23, pos24
        };
    }

    public static Line[] locs2Lines(Location[] locs) {
        if ( locs.length %2 != 0 ) throw new RuntimeException("Location must be even");

        Line[] lines = new Line[locs.length / 2];
        for ( int i = 0; i < locs.length; i += 2 ) {
            lines[i / 2] = new Line(locs[i], locs[i + 1]);
        }

        return lines;
    }

    public static void playerTeleport(Player player, Location location, int seconds) {
        PlayerUser user = new PlayerUser(player);

        final int[] leftSecs = {seconds};
        final boolean[] doMove = {false};

//        FilledCircle filledCircle = new FilledCircle(player.getLocation(), 3, 30);
//        filledCircle.playWithTime(seconds * 20L, 100);

        PlayerDataRunnable RUN = new PlayerDataRunnable() {
            @Override
            public void run() {
                if ( !player.isOnline() ) {
                    cancel();
                    return;
                }

                if ( doMove[0]) {
                    cancel();
                    user.errorActionBar("您已移动，传送取消！").playSound(Sound.BLOCK_ANVIL_BREAK, 1, 1);
                    return;
                }

                if ( leftSecs[0] <= 0 ) {
                    cancel();
                    player.teleport(location);
                    player.setFallDistance(0);
                    player.setVelocity(new Vector(0, 0, 0));
                    user.infoActionBar("传送成功！").playSound(Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                    return;
                }

                leftSecs[0] -= 1;
                user.actionBar("&7传送倒计时 &b&l" + (leftSecs[0] + 1)).title("", "&7倒计时 &b&l" + (leftSecs[0] + 1), 0, 20, 10)
                        .playSound(Sound.BLOCK_NOTE_BLOCK_PLING, .5f, 1f);

                player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getEyeLocation(), 5);

            }
        };

        GlobalListener.moveCheckers.put(player.getName(), () -> doMove[0] = true);

        user.delayRunTimer(RUN, 0, 20L);
    }

}