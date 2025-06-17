package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.function.command.frame;

import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.function.command.ISender;
import com.talexframe.frame.core.modules.plugins.core.PluginInfo;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.annotations.TCmd;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talexframe.frame.core.pojo.wrapper.WrappedSender;
import com.talexframe.frame.core.talex.TFrame;

/**
 * 帮助命令 <br /> {@link com.talexframe.frame.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class PluginCmd extends BaseCommand {

    public PluginCmd() {

        super(TFrame.tframe.getFrameSender(), "plugin", new String[] { "plugins", "plugman" }, "插件命令");
    }

    @TCmd
    public void onDefault(WrappedSender sender) {

        sender.sendMessage(DIVIDER);
        sender.sendMessage("/%label% #Plugin 帮助");
        sender.sendMessage("/%label% list #Plugin 插件列表");
        sender.sendMessage("/%label% load <pluginName> #加载一个插件");
        sender.sendMessage("/%label% unload <pluginName> #卸载一个插件");
        sender.sendMessage("/%label% reload <pluginName> #重载一个插件");
        sender.sendMessage("%divider%");

    }

    @TCmd( "list" )
    public void onListPlugin(WrappedSender sender) {

        sender.sendMessage("%divider%");

        for ( WebPlugin plugin : tframe.getPluginManager().getPluginHashMap().values() ) {

            PluginInfo info = plugin.getInfo();

            sender.sendMessage(" " + plugin.getName() + " (" + info.getPluginVersion() + " @" + info.getSupportVersion() + ")");
            sender.sendMessage("       -- by " + info.getPluginAuthor());
            sender.sendMessage("       ");
            sender.sendMessage("   " + info.getDescription());
            sender.sendMessage("");
            sender.sendMessage("       @" + info.getWebsite());
            sender.sendMessage("");

        }

        sender.sendMessage("%divider%");

    }

    @TCmd( "load" )
    public void onLoadPlugin(ISender sender, String loadPlugin) {

        tframe.getPluginManager().loadPlugin(loadPlugin);

    }

    @TCmd( "unload" )
    public void onUnloadPlugin(ISender sender, String loadPlugin) {

        sender.sendMessage("[插件] 正在卸载插件 " + loadPlugin);

        if ( tframe.getPluginManager().unloadPlugin(loadPlugin) ) {

            sender.sendMessage("[插件] " + loadPlugin + " 已卸载!");

        } else {

            if ( sender instanceof FrameSender ) {

                ( (FrameSender) sender ).errorConsoleMessage("[插件] " + loadPlugin + " 卸载失败!");

            } else {
                sender.sendMessage("[插件] " + loadPlugin + " 卸载失败!");
            }

        }

    }

    @TCmd( "reload" )
    public void onReloadPlugin(ISender sender, String loadPlugin) {

        sender.sendMessage("[插件] 正在重载插件 " + loadPlugin);

        if ( !tframe.getPluginManager().unloadPlugin(loadPlugin) ) {

            if ( sender instanceof FrameSender ) {

                ( (FrameSender) sender ).errorConsoleMessage("[插件] " + loadPlugin + " 重载失败! (无法卸载)");

            } else {
                sender.sendMessage("[插件] " + loadPlugin + " 重载失败! (无法卸载)");
            }

            return;

        }

        tframe.getPluginManager().loadPlugin(loadPlugin);

    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        return false;
    }

}
