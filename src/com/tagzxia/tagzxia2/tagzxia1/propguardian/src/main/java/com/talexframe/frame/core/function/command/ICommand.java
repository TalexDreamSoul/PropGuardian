package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.command;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.ISender;

/**
 * Command接口 -> 命令处理器 <br /> {@link com.talexframe.frame.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:35 <br /> Project: TalexFrame <br />
 */
public interface ICommand {

    /**
     * 执行命令处理
     *
     * @param sender       命令的发出者
     * @param wholeCommand 完整的命令
     * @param matchedLabel 匹配的命令Label
     * @param args         命令的参数 (没有Label)
     *
     * @return 返回真则阻止继续，返回假的继续 (即返回真后后续的命令执行器都不会再执行）
     */
    boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args);

}
