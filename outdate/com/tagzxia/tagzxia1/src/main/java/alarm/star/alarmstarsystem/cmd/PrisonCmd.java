package com.tagzxia.tagzxia1.src.main.java.alarm.star.alarmstarsystem.cmd;

import alarm.star.alarmstarsystem.utils.cmd.ITalexCmd;
import alarm.star.alarmstarsystem.utils.cmd.TalexCmd;
import org.bukkit.command.CommandSender;

public class PrisonCmd extends ITalexCmd {

    public PrisonCmd() {
        super(PrisonCmd.class, "prison");
    }

    @TalexCmd(
            value = "list"
    )
    public void onListPrisonPlayers(CommandSender sender, String label, String[] args) {

    }

    @Override
    public void sendHelpMsg(CommandSender sender) {

    }
}
