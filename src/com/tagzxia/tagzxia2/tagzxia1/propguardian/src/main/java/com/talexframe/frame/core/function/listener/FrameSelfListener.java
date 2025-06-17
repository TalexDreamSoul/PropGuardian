package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.listener;

import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.service.THandler;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorPreShutdownEvent;
import com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.modules.repository.TRepoPlus;
import com.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import com.talexframe.frame.core.talex.TFrame;

/**
 * <br /> {@link com.talexframe.frame.listener Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 17:10 <br /> Project: TalexFrame <br />
 */
public class FrameSelfListener extends FrameListener {

    final TFrame tframe = TFrame.tframe;

    public FrameSelfListener() {

        super("FRAME#SELF");

    }

    // @THandler
    // public void onMysqlConnected(DAOProcessorConnectedEvent<Mysql> event) {
    //
    //     for ( TRepo repository : tframe.getRepoManager().getRepositories().values() ) {
    //
    //         if ( repository instanceof TRepoPlus ) {
    //
    //             ( (TRepoPlus<?>) repository ).onInstall();
    //
    //         }
    //
    //     }
    //
    // }

    @THandler
    public void onMysqlPreShutdown(DAOProcessorPreShutdownEvent<Mysql> event) {

        for ( TRepo repository : tframe.getRepoManager().getRepositories().values() ) {

            if ( repository instanceof TRepoPlus ) {

                ( (TRepoPlus<?>) repository ).onUninstall();

            }

        }

    }

}
