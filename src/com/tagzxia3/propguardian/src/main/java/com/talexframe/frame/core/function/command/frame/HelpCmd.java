package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.function.command.frame;

import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.talex.TFrame;

import java.util.Map;

/**
 * 帮助命令 <br /> {@link com.talexframe.frame.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class HelpCmd extends BaseCommand {

    public HelpCmd() {

        super(TFrame.tframe.getFrameSender(), "help", new String[] { "?", "？" }, "帮助命令");
    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        sender.sendMessage(DIVIDER);

        for ( Map.Entry<String, BaseCommand> entry : tframe.getCommandManager().getCommands().entrySet() ) {

            sender.sendMessage("> " + entry.getKey() + " #" + entry.getValue().getDescription());

        }

        sender.sendMessage(DIVIDER);

        return false;
    }

}
