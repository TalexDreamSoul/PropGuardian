package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.rank;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.event.WorldCoulJoinEvent;
import com.talexs.talexessential.modules.BaseModule;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RankModule extends BaseModule {
    public RankModule() {
        super("ranks");

        new BukkitRunnable() {
            @Override
            public void run() {

                new ArrayList<>(Bukkit.getOnlinePlayers()).forEach(player -> {
                    PlayerData pd = PlayerData.g(player);

                    WorldCoulJoinEvent joinEvent = new WorldCoulJoinEvent(player, player.getLocation(), pd.getDeathLocation());

                    Bukkit.getPluginManager().callEvent(joinEvent);

                    if ( joinEvent.isCancelled() ) {
                        PlayerUser user = bannedWorld(player, joinEvent.getFrom());
                        EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, 10);
                        if ( !economyResponse.transactionSuccess() ) {
                            user.errorActionBar("请勿非法进入世界，您已被强制击杀！（金币不足）");
                            player.setHealth(0);
                        } else
                            user.errorActionBar("请勿非法进入世界，您已被扣除 10 金币！")
                        ;
                    }
                });

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 20 * 15);
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }

    @EventHandler( priority = EventPriority.LOWEST )
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        WorldCoulJoinEvent joinEvent = new WorldCoulJoinEvent(player, event.getTo(), event.getFrom());

        Bukkit.getPluginManager().callEvent(joinEvent);

        if ( event.isCancelled() ) {
            bannedWorld(player, event.getFrom());
        }

    }

    @EventHandler
    public void onWorldReq(WorldCoulJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);

        World world = event.getTo().getWorld();

        Location from = event.getFrom();
        int anInt = pd.getInfo().getInt("Rank.Level", 0);

//        if ( world.getName().toLowerCase().contains("nether") && anInt < 3 ) {
//            player.teleport(from);
//
//            event.setCancelled(true);
//        }

        if ( world.getName().toLowerCase().contains("the_end") && anInt < 2 ) {
            player.teleport(from);

            event.setCancelled(true);
        }
    }

    public static PlayerUser bannedWorld(Player player, Location from) {
        PlayerUser user = new PlayerUser(player);

        user.delayRun(new PlayerDataRunnable() {
                    @Override
                    public void run() {
                        user.title("", "§e请提升你的 §c通行等级 §e!", 0, 80, 40)
                                .actionBar("§e你的 §c§l段位等级 §e过低, 无法进入此世界!")
                                .playSound(Sound.AMBIENT_UNDERWATER_ENTER, 1, .5f)
                                .playSound(Sound.BLOCK_CHERRY_LEAVES_STEP, 1, 1)
                                .playSound(Sound.ITEM_GOAT_HORN_SOUND_6, 1, 1);
                    }
                }, 5)
        ;

        return user;
    }
}
