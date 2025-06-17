package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.function.command;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.ICommand;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:56 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class BaseCommand extends FrameCreator implements ICommand {

    public static final String DIVIDER = "---------------------------";
    protected final TFrame tframe = TFrame.tframe;
    private final FrameCreator owner;
    private final String label;
    private final String[] alias;
    private final String description;
    protected FrameSender frameSender = tframe.getFrameSender();

    /**
     * 指令 init
     *
     * @param creator     指令拥有者
     * @param label       指令Label
     * @param alias       指令别称
     * @param description 指令描述
     */
    public BaseCommand(FrameCreator creator, String label, String[] alias, String description) {

        super("COMMAND", label);

        this.owner = creator;

        this.label = label;
        this.alias = alias;
        this.description = description;

    }

    /**
     * 当扫描到这个类但是没有任何相关匹配的时候，执行操作 （@Override 重写）
     *
     * @param sender 命令发出者
     */
    @Deprecated
    public void onDisMatched(ISender sender) {

        sender.sendMessage("命令参数错误!");

    }

    /**
     * 减少冗杂代码
     */
    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        return false;
    }

}
