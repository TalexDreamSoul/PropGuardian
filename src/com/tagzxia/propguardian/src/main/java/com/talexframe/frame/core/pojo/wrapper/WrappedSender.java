package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.wrapper;

import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import lombok.Getter;

import java.util.Locale;

/**
 * 包装发送类 -> 支持变量解析 %% <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 13:01 <br /> Project: TalexFrame <br />
 */
@Getter
public class WrappedSender implements ISender {

    private final ISender sender;
    private final String label;
    private final String[] args;

    public WrappedSender(ISender sender, String label, String[] args) {

        this.sender = sender;
        this.label = label;
        this.args = args;

    }

    @Override
    public void sendMessage(String... message) {

        for ( String msg : message ) {

            String m = msg;

            int index = msg.indexOf("%");
            int nextIndex = msg.indexOf("%", index + 1);

            while ( nextIndex != -1 ) {

                String var = msg.substring(index, nextIndex + 1);

                if ( var.toLowerCase(Locale.ROOT).startsWith("%args_") ) {

                    String var2 = var.substring(5, var.length() - 1);

                    try {

                        int ind = Integer.parseInt(var2);

                        m = m.replace(var, args[ind]);

                    } catch ( NumberFormatException ignore ) {

                    }

                }

                if ( var.equalsIgnoreCase("%label%") ) {
                    m = m.replace(var, this.label);
                }
                if ( var.equalsIgnoreCase("%divider%") ) {
                    m = m.replace(var, BaseCommand.DIVIDER);
                }


                index = m.indexOf("%", nextIndex);
                nextIndex = m.indexOf("%", index + 1);

            }

            this.sender.sendMessage(m);

        }

    }

}
