package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.command.frame;

import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.talex.TFrame;
import com.talexframe.launcher.Launcher;

/**
 * 帮助命令 <br /> {@link com.talexframe.frame.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class RestartCmd extends BaseCommand {

    public RestartCmd() {

        super(TFrame.tframe.getFrameSender(), "restart", new String[] {}, "重启系统");
    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        Launcher.restart = true;

        tframe.shutdown();

        return false;

    }

}
