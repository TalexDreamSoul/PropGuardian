package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.launcher;

import com.talexframe.frame.TalexFrameApplication;
import com.talexframe.frame.core.talex.TFrame;
import com.talexframe.launcher.guide.FirstGuide;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * <br /> {@link com.talexframe.launcher Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 上午 12:09 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class Launcher {

    public static boolean restart = false;
    public static String[] g_args;

    public static void main(String[] args) {

        log.info("[TalexFrame] Launcher start ...");

        g_args = args;

        // 如果文件不存在则启动第一次向导

        File file = TFrame.getMainFile();

        if ( !new File(file, "/config/guided.talexs").exists() ) {

            new FirstGuide();

        }

        // print args
        log.info("[TalexFrame] Launcher args: " + args.toString());

        // 进入启动 - 先启动 SpringBoot 整个应用
        TalexFrameApplication.start(args);

    }

}
