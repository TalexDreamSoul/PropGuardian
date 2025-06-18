package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.function.command.frame;

import cn.hutool.system.*;
import com.talexframe.frame.core.function.command.BaseCommand;
import com.talexframe.frame.core.pojo.annotations.TCmd;
import com.talexframe.frame.core.pojo.wrapper.WrappedSender;
import com.talexframe.frame.core.talex.TFrame;

/**
 * 帮助命令 <br /> {@link com.talexframe.frame.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class InfoCmd extends BaseCommand {

    public InfoCmd() {

        super(TFrame.tframe.getFrameSender(), "info", new String[] { "information", "i" }, "信息命令");
    }

    @TCmd
    public void onDefault(WrappedSender sender) {

        sender.sendMessage(DIVIDER);
        sender.sendMessage("/%label% #Info 帮助");
        sender.sendMessage("/%label% version #查询当前系统版本");
        sender.sendMessage("/%label% platform #查询当前平台信息");
        sender.sendMessage("/%label% details #查询当前系统消耗");
        sender.sendMessage("%divider%");

    }

    @TCmd( "details" )
    public void onDetails(WrappedSender sender) {

        sender.sendMessage( DIVIDER );

        JvmSpecInfo spec = SystemUtil.getJvmSpecInfo();
        sender.sendMessage("JvmSpecification: ");
        sender.sendMessage("  --> name: " + spec.getName());
        sender.sendMessage("  --> version: " + spec.getVersion());
        sender.sendMessage("  --> vendor: " + spec.getVendor());

        JavaSpecInfo specInfo = SystemUtil.getJavaSpecInfo();
        sender.sendMessage("JavaSpecification: ");
        sender.sendMessage("  --> name: " + specInfo.getName());
        sender.sendMessage("  --> version: " + specInfo.getVersion());
        sender.sendMessage("  --> vendor: " + specInfo.getVendor());

        UserInfo userInfo = SystemUtil.getUserInfo();
        sender.sendMessage("UserInfo: ");
        // output Name HomeDir CurrentDir TempDir Language Country
        sender.sendMessage("  --> name: " + userInfo.getName());
        sender.sendMessage("  --> homeDir: " + userInfo.getHomeDir());
        sender.sendMessage("  --> currentDir: " + userInfo.getCurrentDir());
        sender.sendMessage("  --> tempDir: " + userInfo.getTempDir());
        sender.sendMessage("  --> language: " + userInfo.getLanguage());
        sender.sendMessage("  --> country: " + userInfo.getCountry());

        HostInfo hostInfo = SystemUtil.getHostInfo();
        sender.sendMessage("HostInfo: ");
        sender.sendMessage("  --> hostName: " + hostInfo.getName());
        sender.sendMessage("  --> hostIp: " + hostInfo.getAddress());

        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        // output MaxMemory TotalMemory FreeMemory UsableMemory
        sender.sendMessage("RuntimeInfo: " + (runtimeInfo.getUsableMemory() + "/" + runtimeInfo.getMaxMemory()) +
                " (OS: " + runtimeInfo.getFreeMemory() + "/" + runtimeInfo.getTotalMemory() + ")");

        sender.sendMessage( DIVIDER );

    }

    @TCmd( "platform" )
    public void onPlatform(WrappedSender sender) {

        sender.sendMessage( DIVIDER );

        JvmInfo jvmInfo = SystemUtil.getJvmInfo();
        sender.sendMessage("JvmImplementation: ");
        sender.sendMessage("  --> name: " + jvmInfo.getName());
        sender.sendMessage("  --> version: " + jvmInfo.getVersion());
        sender.sendMessage("  --> vendor: " + jvmInfo.getVendor());
        sender.sendMessage("  --> info: " + jvmInfo.getInfo());

        JavaInfo javaInfo = SystemUtil.getJavaInfo();
        sender.sendMessage("JavaImplementation: ");
        sender.sendMessage("  --> version: " + javaInfo.getVersion() + " (" + javaInfo.getVersionInt() + ", " + javaInfo.getVersionFloat() + ")");
        sender.sendMessage("  --> vendor: " + javaInfo.getVendor());
        sender.sendMessage("  --> vendor-url: " + javaInfo.getVendorURL());

        JavaRuntimeInfo runtimeInfo = SystemUtil.getJavaRuntimeInfo();
        sender.sendMessage("JavaRuntime: ");
        sender.sendMessage("  --> name: " + runtimeInfo.getName());
        sender.sendMessage("  --> version: " + runtimeInfo.getVersion());
        sender.sendMessage("  --> home: " + runtimeInfo.getHomeDir());
        sender.sendMessage("  --> endorsed-dirs: " + runtimeInfo.getEndorsedDirs());
        sender.sendMessage("  --> app-path: " + runtimeInfo.getClassPath());
        sender.sendMessage("  --> app-version: " + runtimeInfo.getClassVersion());
        sender.sendMessage("  --> library-path: " + runtimeInfo.getLibraryPath());
        sender.sendMessage("  --> protocol-packages: " + runtimeInfo.getProtocolPackages());

        OsInfo os = SystemUtil.getOsInfo();
        sender.sendMessage("OS Info: ");
        sender.sendMessage("  --> name: " + os.getName());
        // output OSArch OSName OSVersion FileSeparator PathSeparator LineSeparator
        sender.sendMessage("  --> arch: " + os.getArch());
        sender.sendMessage("  --> version: " + os.getVersion());
        sender.sendMessage("  --> file-separator: " + os.getFileSeparator());
        sender.sendMessage("  --> path-separator: " + os.getPathSeparator());
        // sender.sendMessage("  --> line-separator: " + os.getLineSeparator());

        sender.sendMessage( DIVIDER );

    }

    @TCmd( "version" )
    public void onVersion(WrappedSender sender) {

        sender.sendMessage( DIVIDER );
        sender.sendMessage("当前系统版本: " + tframe.getVersion());
        sender.sendMessage("当前核心版本: " + tframe.getVersionE().getVersion() + " (" + tframe.getVersionE().name() + ")");
        sender.sendMessage( DIVIDER );

    }

    private void processStr(WrappedSender sender, String str) {

        for( String s : str.split("\n") ) {

            sender.sendMessage("  " + s);

        }

    }

}
