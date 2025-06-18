package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.function.command;

import cn.hutool.core.util.StrUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.BaseCommand;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.CommandReader;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.pojo.annotations.TCmd;
import com.talexframe.frame.core.pojo.wrapper.WrappedSender;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 命令管理器 <br /> {@link com.talexframe.frame.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 23:26 <br /> Project: TalexFrame <br />
 */
@Getter
public class CommandManager {

    public static CommandManager INSTANCE;
    private final CommandReader commandReader;
    private final Map<String, BaseCommand> commands = new HashMap<>(16);

    @SneakyThrows
    private CommandManager() {

        this.commandReader = new CommandReader(this);

        this.commandReader.run();

    }

    public static void initial() {

        if ( INSTANCE != null ) {
            return;
        }

        INSTANCE = new CommandManager();

    }

    /**
     * 处理命令 - 内部接口请勿调用 （可以用来发配假装指令） dispatch
     *
     * @param wholeCmd 完整的命令
     */
    public void processCommand(ISender sender, String wholeCmd) {

        List<String> list = new ArrayList<>(Arrays.asList(wholeCmd.split(" ")));

        String label = list.get(0);
        list.remove(0);

        String[] args = list.toArray(new String[0]);

        boolean match = false;

        for ( Map.Entry<String, BaseCommand> entry : new HashSet<>(this.commands.entrySet()) ) {

            String cmdLabel = entry.getKey();
            BaseCommand cmd = entry.getValue();

            if ( cmd.getLabel().equalsIgnoreCase(label) || cmdLabel.equalsIgnoreCase(label) || Arrays.stream(cmd.getAlias()).anyMatch(s -> s.equalsIgnoreCase(label)) ) {

                match = true;

                try {

                    this.processCmdClass(sender, cmd, wholeCmd, label, args);

                } catch ( InvocationTargetException | IllegalAccessException e ) {

                    TFrame.tframe.getFrameSender().errorConsoleMessage("在执行命令扫描器的时候出现问题: " + label + " @" + cmd.getClass(), e);

                }

                if ( cmd.executeCommand(sender, wholeCmd, label, args) ) {
                    break;
                }

            }

        }

        if ( !match ) {

            sender.sendMessage("未知的命令: " + wholeCmd);

        }

    }

    private void processCmdClass(ISender sender, BaseCommand cmd, String wholeCmd, String inputLabel, String[] args) throws InvocationTargetException, IllegalAccessException {

        for ( Method method : cmd.getClass().getMethods() ) {

            TCmd tc = method.getAnnotation(TCmd.class);

            if ( tc == null ) {
                continue;
            }

            Parameter[] parameters = method.getParameters();

            if ( parameters == null || parameters.length == 0 ) {
                continue;
            }

            String value = tc.value();

            if ( tc.completedMatch() ) {

                if ( tc.matchCase() ) {

                    if ( value.equals(wholeCmd) ) {

                        invoke(cmd, method, parameters[0], sender, inputLabel);

                    }

                } else {

                    if ( value.equalsIgnoreCase(wholeCmd) ) {

                        invoke(cmd, method, parameters[0], sender, inputLabel);

                    }

                }

                continue;

            }

            if ( tc.ignoreContent() ) {

                int len = parameters.length - 1;

                if ( args.length == len ) {

                    invoke(cmd, method, parameters[0], sender, inputLabel, args, tc.nullAutoComplete());

                }

            }

            /*
                如果 参数只有一个即匹配的content 或者content是空 则匹配默认的方法 即不检测任何的方法
             */
            if ( parameters.length == 1 && args.length == 0 && StrUtil.isBlankIfStr(value) ) {
                invoke(cmd, method, parameters[0], sender, inputLabel);
                continue;
            }

            if ( args.length < 1 ) {
                continue;
            }

            List<String> newArgs = new ArrayList<>(Arrays.asList(args));
            newArgs.remove(0);

            boolean matched;

            if ( tc.matchCase() ) {

                matched = args[0].equals(value);

            } else {
                matched = args[0].equalsIgnoreCase(value);
            }

            if ( !matched ) {
                continue;
            }

            this.invoke(cmd, method, parameters[0], sender, inputLabel, newArgs.toArray(new String[0]), tc.nullAutoComplete());

        }

    }

    private void invoke(BaseCommand cmd, Method method, Parameter paramFirst, ISender sender, String label) throws InvocationTargetException, IllegalAccessException {

        if ( paramFirst.getType() == WrappedSender.class ) {

            method.invoke(cmd, new WrappedSender(sender, label, null));

        } else {
            method.invoke(cmd, sender);
        }

    }

    private void invoke(BaseCommand cmd, Method method, Parameter paramFirst, ISender sender, String label, String[] args, boolean autoComplete) throws InvocationTargetException, IllegalAccessException {

        List<Object> params = new ArrayList<>();

        if ( paramFirst.getType() == WrappedSender.class ) {

            params.add(new WrappedSender(sender, label, args));

        } else {

            params.add(sender);

        }

        List<String> list = new ArrayList<>(Arrays.asList(args));

        if ( args.length < method.getParameterCount() - 1 ) {

            if ( !autoComplete ) {

                sender.sendMessage("你需要额外的 " + ( method.getParameterCount() - args.length - 1 ) + " 个参数来使用这个命令.");
                return;

            }

            for ( int i = 0; i < method.getParameterCount() - 1; ++i ) {

                list.add(null);

            }

        }

        params.addAll(list);

        method.invoke(cmd, params.toArray());

    }

    /**
     * 设置指令的执行器
     *
     * @param label   指令Label (标识符）
     * @param command 执行器
     *
     * @return 返回是否成功 返回假一般代表已有此命令 （可以通过 namespace 解决）
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public boolean setCommandExecutor(String label, BaseCommand command) {

        if ( !label.equals(command.getLabel()) ) {

            throw new RuntimeException("Label标识符与内部CommandLabel标识符不一致!");

        }

        if ( this.commands.containsKey(label) ) {
            return false;
        }

        this.commands.put(label, command);

        return true;

    }

    /**
     *
     * 删除指令的执行器
     *
     * @param label 指令标识符
     * @return 返回是否成功 返回假一般代表没有此命令
     */
    public boolean removeCommandExecutor(String label) {

        return this.commands.remove(label) != null;

    }

}
