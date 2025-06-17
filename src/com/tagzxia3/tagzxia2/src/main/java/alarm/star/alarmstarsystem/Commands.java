package com.tagzxia3.tagzxia2.src.main.java.alarm.star.alarmstarsystem;

import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.entity.PlayerData;
import alarm.star.alarmstarsystem.inventory.menu.PlayerShop;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.AlarmStarSystem;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if ( !(commandSender.hasPermission("alarmstarsystem.admin")) ) {
            return false;
        }

        if ( !(commandSender instanceof Player) ) {
            commandSender.sendMessage("§8[§cAlarm§eStar§7System§8] §e抱歉，你必须是一个玩家才能执行这个命令!");
            return false;
        }

        Player player = (Player) commandSender;
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        if ( args == null || args.length < 1 ) {
            return pd.templateMsgs("§e/" + label + " shop   §7#商店相关命令", "§e/" + label + " dummy   §7#警告值相关命令", "§e/" + label + " prison §7#监狱相关命令", "§e/" + label + " reload all §7#重载所有配置文件")
                    .sound(Sound.BLOCK_NOTE_PLING).actionBar("§a§l欢迎使用 §c§lAlarm§e§lStar §7§lSystem §a§l插件!").f();
        }

        String type = args[0].toLowerCase();
        String subType = args[1].toLowerCase();

        if ( type.equals("reload") && subType.equals("all") ) {
            if ( !AlarmStarSystem.getInstance().reloadAllConfig() ) {
                return pd.sound(Sound.BLOCK_ANVIL_FALL).actionBar("§c抱歉，重载配置文件时出现了错误!").f();
            }
            return pd.templateMsgs("§e所有配置文件已重载成功!").f();
        }

        // /ass dummy add player xxx
        if ( type.equals("dummy") ) {
            if ( args.length == 4 ) {
                Player target = Bukkit.getPlayer(args[2]);
                if ( target == null || !target.isOnline() ) {
                    return pd.sound(Sound.BLOCK_NOTE_PLING).actionBar("§e抱歉，玩家 §c" + args[2] + " §e不在线，无法完成这个命令!").f();
                }
                PlayerData tpd = PlayerDataConfig.getInstance().getPd(target);

                int amo;

                try {
                    amo = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    return pd.sound(Sound.BLOCK_ANVIL_FALL).actionBar("&c你输入的内容不是一个数字！").f();
                }

                switch (subType) {
                    case "add":
                        tpd.addWarningDummy(Math.abs(amo));
                        return pd.templateMsgs("§e警告值已添加成功!", "§7现在玩家 §b" + args[2] + " §7拥有警告值: §c" + pd.getWarningDummy()).f();
                    case "del":
                        tpd.addWarningDummy(Math.abs(amo) * -1);
                        return pd.templateMsgs("§e警告值已删除成功!", "§7现在玩家 §b" + args[2] + " §7拥有警告值: §c" + pd.getWarningDummy()).f();
                    case "set":
                        tpd.setWarningDummy(amo);
                        return pd.templateMsgs("§e警告值已设置成功!", "§7现在玩家 §b" + args[2] + " §7拥有警告值: §c" + pd.getWarningDummy()).f();
                }
            }

            return pd.sound(Sound.BLOCK_NOTE_FLUTE)
                    .templateMsgs(
                            "§e/" + label + " dummy add <玩家> <数值> §7# 为玩家添加警告值.",
                            "§e/" + label + " dummy del <玩家> <数值> §7# 为玩家减少警告值.",
                            "§e/" + label + " dummy set <玩家> <数值> §7# 为玩家设置警告值."
                    ).f();
        }

        // /ass prison add/release player
        if ( type.equals("prison") ) {
            if ( args.length == 3 ) {

                Player target = Bukkit.getPlayer(args[2]);
                if ( target == null || !target.isOnline() ) {
                    return pd.sound(Sound.BLOCK_NOTE_PLING).actionBar("§e抱歉，玩家 §c" + args[2] + " §e不在线，无法完成这个命令!").f();
                }
                PlayerData tpd = PlayerDataConfig.getInstance().getPd(target);

                switch ( subType ) {
                    case "add":
                        tpd.setPrison(true);
                        return pd.actionBar("§7现在 §c" + target.getName() + " §7已经被加入监狱!").f();
                    case "release":
                        tpd.setPrison(false);
                        return pd.actionBar("§7现在 §e" + target.getName() + " §7已经被释放出监狱!").f();
                }

            }
//            switch ( subType ) {
//                case "list":
//                    new PlayerInPrison((Player) commandSender).open();
//                    return false;
//                case "leave": {
//                    AlarmStar as = (AlarmStar) AlarmStarConfig.getInstance(AlarmStarConfig.class).get(String.valueOf(tpd.getStarCount()));
//                    if ( as == null ) {
//                        throw new RuntimeException("服务器配置文件出错，玩家无法正常离开监狱！");
//                    }
//
//                    EconomyResponse er = AlarmStarSystem.getEcon().withdrawPlayer(player, as.getPay());
//
//                    if ( er.transactionSuccess() ) {
//                        playerData.setPrison(false);
//                    } else {
//                        player.sendMessage("§c你的钱余额不足以保释!");
//                        player.getWorld().strikeLightningEffect(player.getLocation());
//                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1, 1);
//                    }
//                    return false;
//                }
//            }


            return pd.templateMsgs(
                    "§e/" + label + " prison add <玩家> §7# 将玩家加入监",
                    "§e/" + label + " prison release <玩家> §7# 将玩家从监狱中释放"
            ).sound(Sound.BLOCK_NOTE_FLUTE).f();
        }

        // /ass shop create/edit player
        if ( type.equals("shop") ) {
            if ( args.length == 3) {

                switch ( subType ) {
                    case "create":
                        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, args[2]);
                        npc.spawn(player.getLocation());
//                        ShopAssistant sa = npc.getOrAddTrait(ShopAssistant.class);

//                        sa.setShopOwner(args[2]);
                        return pd.sound(Sound.BLOCK_NOTE_PLING).actionBar("§7玩家 §b" + args[2] + " §7的 §e商店NPC §7已创建完毕.").f();
                    case "edit":
                        new PlayerShop(player, args[2]).open();
                        return pd.actionBar("§7已为您打开玩家 §b" + args[2] + " §7的 §e商店§7 .").f();
                }

            }

            return pd.templateMsgs(
                    "§e/" + label + " shop create <玩家> §7#创建某个玩家的商店NPC",
                    "§e/" + label + " shop edit <玩家> §7#直接编辑某个玩家的商店"
            ).f();
        }

        pd.sound(Sound.BLOCK_NOTE_BASS).actionBar("§c§l未知的命令!");

        return false;

    }

}
