package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm.cmd;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.gui.PlayerRealmControlGUI;
import com.talexs.talexessential.modules.realm.RealmArea;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.modules.realm.gui.RealmListMenu;
import com.talexs.talexessential.modules.realm.gui.RealmSetsMenu;
import com.talexs.talexessential.utils.LocationUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import top.zoyn.particlelib.pobject.Cube;
import top.zoyn.particlelib.pobject.EffectGroup;
import top.zoyn.particlelib.utils.matrix.Matrixs;

public class RealmCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) return false;

        Player player = (Player) sender;

        if (args != null) {

            if ( args.length == 1 && args[0].equalsIgnoreCase("list") ) {
                new RealmListMenu().open(player);
            }

            if ( args.length == 2 && args[0].equalsIgnoreCase("flag") ) {
                PlayerUser user = new PlayerUser(player);

                String realmName = args[1];
                PlayerRealm pr = RealmModule.getRealmByName(realmName);
                if ( pr == null ) {
                    user.errorActionBar("没有这个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                if ( !pr.getServerName().equalsIgnoreCase(RealmModule.getServerName()) ) {
                    user.errorActionBar("这个领域不在这个服务器！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                new RealmSetsMenu(pr).open(player);
            }

            if ( args.length == 2 && args[0].equalsIgnoreCase("set") ) {
                PlayerUser user = new PlayerUser(player);

                String realmName = args[1];
                PlayerRealm pr = RealmModule.getRealmByName(realmName);
                if ( pr == null ) {
                    user.errorActionBar("没有这个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                if ( !pr.getServerName().equalsIgnoreCase(RealmModule.getServerName()) ) {
                    user.errorActionBar("这个领域不在这个服务器！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                new PlayerRealmControlGUI(player, pr).open();
            }

            // res tp xxx
            if ( args.length == 2 && args[0].equalsIgnoreCase("tp") ) {
                PlayerUser user = new PlayerUser(player);

                String realmName = args[1];
                PlayerRealm pr = RealmModule.getRealmByName(realmName);
                if ( pr == null ) {
                    user.errorActionBar("没有这个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                if ( !pr.getServerName().equalsIgnoreCase(RealmModule.getServerName()) ) {
                    user.errorActionBar("这个领域不在这个服务器！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                if ( !pr.allowPlayerTeleport(player) ) {
                    user.errorActionBar("您没有权限进入这个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                Location teleportLocation = pr.getTeleportLocation();

                int cd = 5;

                if ( player.hasPermission("te.realm.teleport.admin") || player.hasPermission("te.vip.pro") ) cd = 0;
                else if ( player.hasPermission("te.vip.plus") ) cd = 3;

                LocationUtil.playerTeleport(player, teleportLocation, cd);
            }

        }

        return false;
    }
}
