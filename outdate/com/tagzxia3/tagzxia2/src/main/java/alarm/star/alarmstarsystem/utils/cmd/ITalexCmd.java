package com.tagzxia3.tagzxia2.src.main.java.alarm.star.alarmstarsystem.utils.cmd;

import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.utils.cmd.TalexCmd;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public abstract class ITalexCmd extends Command {

    private final Class<?> SUB_CLASS;

    @Setter
    private String onlyConsole, onlyPlayer, noSubCommand, noPermission, subName;

    public ITalexCmd(Class<?> clz, String subName) {
        super("alarmstarsystem", "Talex - AlarmStarSystem", "/<command>", Arrays.asList("ass", "alarmstar"));
        SUB_CLASS = clz;
        onlyConsole = "&conly console use.";
        onlyPlayer = "&conly player use.";
        noSubCommand = "&cunknown subcommand";
        noPermission = "&cyou don't have permissions.";

        this.subName = subName;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if ( args.length < 2 ) return true;

        if ( args.length < 3 ) {
//            sendHelpMsg(sender);
            return false;
        }

        if (!Objects.equals(args[1], this.subName)) return false;

        Method method = getMethodByCommand(args[0]);
        if ( method == null ) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noSubCommand));
            return false;
        }

        if (check(sender, method.getAnnotation(TalexCmd.class))) {
            try {
                method.invoke(this, sender, label, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 检查权限、玩家、控制台
     *
     * @param sender
     * @param cmd
     * @return
     */
    private boolean check(CommandSender sender, TalexCmd cmd) {
        if (!sender.hasPermission(cmd.permission())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
            return false;
        } else if (cmd.cmdSender() == TalexCmd.CmdSender.PLAYER && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', onlyPlayer));
            return false;
        } else if (cmd.cmdSender() == TalexCmd.CmdSender.CONSOLE && sender instanceof Player) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', onlyConsole));
            return false;
        }
        return true;
    }

    /**
     * 获取处理子命令的method
     *
     * @param subCmd 请求的子命令
     * @return Method 处理请求命令的方法
     */
    private Method getMethodByCommand(String subCmd) {
        Method[] methods = SUB_CLASS.getMethods();
        for (Method method : methods) {
            TalexCmd cmd = method.getAnnotation(TalexCmd.class);
            if (cmd != null)
                if (cmd.IgnoreCase() ? subCmd.equals(cmd.value()) : subCmd.equalsIgnoreCase(cmd.value())) {
                    Parameter[] parameter = method.getParameters();
                    if (parameter.length == 3 && parameter[0].getType() == CommandSender.class && parameter[1].getType() == String.class && parameter[2].getType() == String[].class)
                        return method;
                    else
                        log.warn("found a Illegal sub command method in command " + getName() + " called: " + method.getName() + " in class: " + SUB_CLASS);
                }
        }
        return null;
    }

    /**
     * 发送帮助信息
     *
     * @return void   无返回值
     * @Param sender 被发送的对象
     */
    public abstract void sendHelpMsg(CommandSender sender);

    public boolean Register() {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            CommandMap map = (CommandMap) commandMap.get(Bukkit.getServer());
            map.register("alarmstarsystem", this);
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            log.warn("Error while register Command: " + this + " due to\n");
            log.warn(e.getLocalizedMessage());
        }
        return false;
    }
    public void register(){
        Register();
    }

    @SneakyThrows
    public void unregister() {
        Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMap.setAccessible(true);
        CommandMap map = (CommandMap) commandMap.get(Bukkit.getServer());
        unregister(map);
    }

    protected String buildString(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object tmp : objs) {
            sb.append(tmp);
        }
        return sb.toString();
    }

}
