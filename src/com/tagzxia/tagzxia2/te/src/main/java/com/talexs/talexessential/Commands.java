package com.tagzxia2.te.src.main.java.com.talexs.talexessential;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.arena.ArenaModule;
import com.talexs.talexessential.modules.attract.AttractGUI;
import com.talexs.talexessential.modules.common.RandomTeleportModule;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.guider.TeleportProfile;
import com.talexs.talexessential.modules.rank.RankGUI;
import com.talexs.talexessential.modules.resource.ResGUI;
import com.talexs.talexessential.modules.resource.ResModule;
import com.talexs.talexessential.modules.shop.BuyShopMenu;
import com.talexs.talexessential.modules.shop.SellShopMenu;
import com.talexs.talexessential.modules.souvenir.SouvenirMenu;
import com.talexs.talexessential.modules.trade.TradeMenu;
import com.talexs.talexessential.modules.trade.TradeObject;
import com.talexs.talexessential.modules.union.UnionData;
import com.talexs.talexessential.modules.union.UnionMember;
import com.talexs.talexessential.modules.union.UnionMembersGUI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import top.zoyn.particlelib.pobject.*;
import top.zoyn.particlelib.utils.matrix.Matrixs;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) return false;

        Player player = (Player) sender;

        if (args != null) {

            if ( args.length == 1 && args[0].equalsIgnoreCase("attract") ) {
                new AttractGUI(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("souvenir") ) {
                new SouvenirMenu(player, 0).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("resource") ) {
                new ResGUI(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("randomtp") ) {
                RandomTeleportModule.triggerTeleport(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("arena") ) {
                if (ArenaModule.activePlayer != null ) {
                    new PlayerUser(player).errorActionBar("竞技场已经开启！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }
                ArenaModule.start(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("teleport") ) {
                new TeleportProfile(player).openForPlayer(player);
            } else  if ( args.length >= 1 && args[0].equalsIgnoreCase("soultech") ) {
                if ( args.length == 2 && args[1].equalsIgnoreCase("guide")) {
                    PlayerData pd = PlayerData.g(player);
                    pd.getPlayerSoul().addGuideToPlayer(player);
                }
            } else if ( args.length == 2 && args[0].equalsIgnoreCase("trade") ) {
                // te trade xxx
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if ( target == null || !target.isOnline() ) {
                    new PlayerUser(player).errorActionBar("对方不在线，无法完成交易！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                new TradeMenu(player, new TradeObject().setTarget(target)).open();

            } else  if ( args.length >= 3 && args[0].equalsIgnoreCase("union") ) {
                String target = args[2];
                UnionData ud = UnionMembersGUI.unionInvitations.get(target);
                if ( ud == null ) {
                    new PlayerUser(player).errorActionBar("您没有收到过请求！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                UnionMembersGUI.unionInvitations.remove(target);
                Player tp = Bukkit.getPlayer(target);
                if ( tp == null || !tp.isOnline() ) {
                    new PlayerUser(player).errorActionBar("请确保邀请你的玩家在线！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    return false;
                }

                if ( args[1].equalsIgnoreCase("agree")) {
                    UnionMember unionMember = new UnionMember(ud, player);
                    ud.getMembers().add(unionMember);
                    unionMember.save();
                    PlayerData pd = PlayerData.g(player);

                    pd.getInfo().set("Union.UUID", ud.getUuid().toString());
                    pd.getInfo().set("Union.name", ud.getName());

                    ud.addLog(player.getName() + " 加入了联盟！(" + target + ")");

                    ud.save();

                    new PlayerUser(player).infoActionBar("你同意了对方的请求！").playSound(Sound.ENTITY_WANDERING_TRADER_YES, 1, 1);
                    new PlayerUser(tp).infoActionBar("对方同意了您的请求！").playSound(Sound.ENTITY_VILLAGER_YES, 1, 1);
                } else if ( args[1].equalsIgnoreCase("disagree")) {
                    new PlayerUser(tp).errorActionBar("对方拒绝了您的请求！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    new PlayerUser(player).infoActionBar("你拒绝了对方的请求！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    ud.addLog(player.getName() + " 拒绝了联盟邀请！(" + target + ")");
                    ud.save();
                } else if ( args[1].equalsIgnoreCase("ignore")) {
                    new PlayerUser(player).infoActionBar("你忽略了对方的请求！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                    ud.addLog(player.getName() + " 忽略了联盟邀请！(" + target + ")");
                    ud.save();
                }

                return false;
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("rank") ) {
                new RankGUI(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("profile") ) {
                new PlayerProfile(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("sellshop") ) {
                new SellShopMenu(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("buyshop") ) {
                new BuyShopMenu(player).openForPlayer(player);
            } else  if ( args.length == 1 && args[0].equalsIgnoreCase("back") ) {

                PlayerData pd = PlayerData.g(player);
                Location lastDeathLocation = pd.getDeathLocation();
                if ( lastDeathLocation == null ) {
                    player.sendActionBar("§c您没有上个地点记录！");
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_FLUTE, .9f,1);
                    return false;
                }

                if ( lastDeathLocation.getWorld() == ResModule.INS.getWorld() ) {
                    player.sendActionBar("§c您无法这么做！");
                    player.playSound(player, Sound.ENTITY_VILLAGER_HURT, 1,1);
                    return false;
                }

                pd.setDeathLocation(player.getLocation());
                player.teleport(lastDeathLocation);
                player.sendActionBar("§a您已返回上个地点！");
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1,1);

                int rankLevel = pd.getInfo().getInt("Rank.Level", 0);

                if ( rankLevel < 7 ) return false;

                Circle circle = new Circle(player.getLocation(), 1);
                Polygon polygon = new Polygon(3, player.getLocation());
                Polygon polygon1 = new Polygon(3, player.getLocation());

                polygon1.addMatrix(Matrixs.rotateAroundYAxis(180));

                new EffectGroup(circle, polygon, polygon1)
                        .setColor(Color.PURPLE)
                        .setPeriod(40)
                        .scale(1.5 + (rankLevel - 7))
                        .show();

            }

        }

        return false;
    }
}
