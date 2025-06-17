package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.common;

import cn.hutool.core.date.DateUtil;
import com.talexs.talexessential.GlobalListener;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.IPlayerUser;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.utils.StringUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RandomTeleportModule extends BaseModule {
    public RandomTeleportModule() {
        super("random-teleport");
    }

    private class RandomTeleportCmd implements CommandExecutor {

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if ( !(commandSender instanceof Player) ) return false;

            Player player = (Player) commandSender;

            RandomTeleportModule.triggerTeleport(player);

            return false;
        }
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(Bukkit.getPluginCommand("randomteleports")).setExecutor(new RandomTeleportCmd());

        new BukkitRunnable() {
            @Override
            public void run() {
                String date = DateUtil.formatDate(DateUtil.date());
                List<Player> players = new ArrayList<>(TalexEssential.getInstance().getServer().getOnlinePlayers());

                for ( Player player : players ) {
                    PlayerData pd = PlayerData.g(player.getName());

                    if ( pd == null ) continue;

                    PlayerUser user = new PlayerUser(player);

                    refreshPlayers(date, pd, user, player);
                }
            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 20);
    }

    @Override
    protected boolean configurable() {
        return false;
    }

    private void refreshPlayers(String date, PlayerData pd, PlayerUser user, Player player) {
        int dayCount = pd.getInfo().getInt("RandomTeleport." + date + ".Count", 1);
        int maxCount = playerTeleportMaxCount(player);
        if ( dayCount >= maxCount ) return;

        int time = pd.getInfo().getInt("RandomTeleport." + date + ".Time", 0);

        int countTime = playerTeleportCountTime(player);

        if ( time < countTime ) {
            pd.getInfo().set("RandomTeleport." + date + ".Time", time + 1);

            return;
        }

        dayCount += 1;

        pd.getInfo().set("RandomTeleport." + date + ".Time", 0);
        pd.getInfo().set("RandomTeleport." + date + ".Count", dayCount);

        String progressBar = StringUtil.generateProgressString((double) dayCount / maxCount, maxCount, "&b| ", "&7| ");

        user
                .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.2f, .75f)
                .actionBar(
                "&2随机传送次数已更新 &8[ " + progressBar + "&8] &a" + dayCount + "&7/&a" + maxCount
        );

    }

    private int playerTeleportMaxCount(Player player) {
        if ( player.hasPermission("te.randomteleport.admin") ) return 10;
        if ( player.hasPermission("te.pro") ) return 8;
        if ( player.hasPermission("te.plus") ) return 5;
        else return 3;
    }

    private int playerTeleportCountTime(Player player) {
        if ( player.hasPermission("te.randomteleport.admin") ) return 15;
        else return 600;
    }

    static int RANDOM_RADIUS = 10000;

    public static void triggerTeleport(Player player) {
        OnlinePlayerData pd = PlayerData.g(player);
        IPlayerUser user = pd.getUser();

        String date = DateUtil.formatDate(DateUtil.date());
        int dayCount = pd.getInfo().getInt("RandomTeleport." + date + ".Count", 1);

        if ( dayCount <= 0 ) {
            user
                    .playSound(Sound.BLOCK_ANVIL_FALL, 1, 1)
                    .errorActionBar("请等待随机传送次数蓄能！");
            return;
        }

        if ( !user.autoCoolDown("randomTeleport", 15 * 1000) ) {
            user
                    .playSound(Sound.BLOCK_ANVIL_FALL, 1, 1)
                    .errorActionBar("随机传送冷却中，请稍后再试！");
            return;
        }

        Location loc = player.getLocation();

        World world = loc.getWorld();

        if ( world.getName().equalsIgnoreCase("Spawn") ) return;

        AtomicBoolean doMove = new AtomicBoolean(false);

        GlobalListener.moveCheckers.put(player.getName(), () -> doMove.set(true));

        // define random radius
        Random random = new Random();

        final int[] x = {0};

        user
                .triggerDarkness().delayRunTimer(new PlayerDataRunnable() {
            @Override
            public void run() {
                if ( doMove.get() ) {
                    cancel();

                    user
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                            .errorActionBar("你移动了，传送取消！")
                            .title("", "&c传送取消",0, 20, 0);

                    return;
                }

                x[0] += 1;

                user
                        .triggerDarkness()
                        .playSound(Sound.BLOCK_NOTE_BLOCK_SNARE, 1, .5f)
                        .title("&7&ki", "&7正在选点，请勿移动...",0, 20, 0);

                if ( x[0] < 5 ) return;

                int x = random.nextInt(RANDOM_RADIUS) * (random.nextBoolean() ? 1 : -1);
                int z = random.nextInt(RANDOM_RADIUS) * (random.nextBoolean() ? 1 : -1);

                Location newLoc = loc.set(x, loc.getY(), z);

                newLoc = newLoc.getWorld().getHighestBlockAt(newLoc).getLocation();

                // exclude some biomes
                if ( checkLoc(newLoc) == null ) {

                    boolean access = false;

                    for ( int i = 0; i <= 10; ++i ) {

                        newLoc.setY(random.nextInt(235));
                        if ( checkLoc(newLoc) != null ) {
                            access = true;
                            break;
                        }

                    }

                    if ( !access ) return;

                }

                cancel();

                int dayCount = pd.getInfo().getInt("RandomTeleport." + date + ".Count", 1);
                pd.getInfo().set("RandomTeleport." + date + ".Count", dayCount - 1);

                user
                        .infoActionBar("传送完成，请注意安全！")
                        .playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1)
                        .title("&e查询完成", "&7正在传送...",0, 20, 0);

                player.teleport(newLoc.add(0, 1.25, 0));

            }
        }, 0, 4);

    }

    private static Location checkLoc(Location baseLoc) {
        Location loc = baseLoc.clone();

        World world = loc.getWorld();

        if ( world.getName().contains("end") ) {

            Location belowLoc = loc.clone().add(0, -1, 0);

            if ( belowLoc.getBlock().getType() != Material.END_STONE ) {
                return null;
            }

            return loc;

        }

        // exclude some biomes
        if ( loc.getBlock().getBiome().name().contains("OCEAN") ) {
            return null;
        }

        if ( loc.getBlock().getType() == Material.AIR ) {
            return null;
        }

        if ( loc.getBlock().getType() == Material.WATER ) {
            return null;
        }

        if ( loc.getBlock().getType() == Material.LAVA ) {
            return null;
        }

        if ( loc.getBlock().getType() == Material.BEDROCK ) {
            return null;
        }

        if (RealmModule.getRealmByLoc(loc) != null) {
            return null;
        }

        Location belowLoc = loc.clone().add(0, -1, 0);

        if ( belowLoc.getBlock().getType() == Material.AIR ) {
            return null;
        }

        if ( belowLoc.getBlock().getType() == Material.WATER ) {
            return null;
        }

        if ( belowLoc.getBlock().getType() == Material.LAVA ) {
            return null;
        }

        if ( belowLoc.getBlock().getType() == Material.BEDROCK ) {
            return null;
        }

        Location upperLoc = loc.clone().add(0, 1, 0);

        if ( upperLoc.getBlock().getType() != Material.AIR ) {
            return null;
        }

        if ( upperLoc.getBlock().getType() == Material.WATER ) {
            return null;
        }

        if ( upperLoc.getBlock().getType() == Material.LAVA ) {
            return null;
        }

        if ( upperLoc.getBlock().getType() == Material.BEDROCK ) {
            return null;
        }

        return loc;
    }

}
