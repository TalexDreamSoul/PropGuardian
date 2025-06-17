package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.function.command.app;

import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.network.connection.TRequest;
import com.talexframe.frame.core.pojo.annotations.TCmd;
import com.talexframe.frame.core.pojo.wrapper.WrappedSender;
import com.talexframe.frame.core.talex.TFrame;

import java.lang.reflect.Method;

/**
 * <br /> {@link com.talexframe.frame.core.function.command.app Package }
 *
 * @author TalexDreamSoul
 * 2022/07/26 09:23:28 <br /> Project: TalexFrame <br />
 */
public class appCmd extends BaseCommand {

    public appCmd() {

        super(TFrame.tframe.getFrameSender(), "app", new String[]{"application","apps","应用件"}, "应用件帮助");
    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        return false;
    }

    @TCmd("list")
    public void onListApp(WrappedSender sender) {

        sender.sendMessage("%divider%");

        for( TApp c : this.tframe.getAppManager().getControllers().values() ) {

            Class<?> clz = c.getClass();

            sender.sendMessage("  @" + clz.getName());

            for( Method method : clz.getMethods() ) {

                TRequest tRequest = method.getAnnotation(TRequest.class);

                if( tRequest == null ) {

                    sender.sendMessage(" - " + method.getName() + " / ×");

                } else {

                    sender.sendMessage(" - " + method.getName() + " / " + tRequest.value());

                }

            }

        }

        sender.sendMessage("%divider%");

    }

}
