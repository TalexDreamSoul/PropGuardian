package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.function.command.frame;

import com.talexframe.frame.TalexFrameApplication;
import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.talex.TFrame;

/**
 * 帮助命令 <br /> {@link com.talexframe.frame.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class StopCmd extends BaseCommand {

    public StopCmd() {

        super(TFrame.tframe.getFrameSender(), "stop", new String[] {}, "关闭系统");
    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        if ( TalexFrameApplication.context != null ) {

            TalexFrameApplication.context.stop();

        }

        return false;

    }

}
