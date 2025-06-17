package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection;

import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.service.THandler;
import com.talexframe.frame.core.modules.event.events.app.AppPostRegisterEvent;
import com.talexframe.frame.core.modules.event.events.app.AppUnRegisteredEvent;
import com.talexframe.frame.core.modules.network.connection.app.ClassReceiverManager;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 01:15 Project: TalexFrame
 */
@Slf4j
public class NetworkListener extends FrameListener {

    private final ClassReceiverManager reqClsManager = ClassReceiverManager.INSTANCE;

    public NetworkListener() {

        super("NetworkListener");

    }

    @THandler
    public void onAppPostRegister(AppPostRegisterEvent event) {

        ClassAppReceiver reqClsReceiver = new ClassAppReceiver(event.getApp(), event.getApp().getClass());

        if( !reqClsManager.registerReqReceiver(event.getPlugin(), reqClsReceiver) ) {

            log.warn("[Application] Already registered: @{}", reqClsReceiver.getOwnClass());


        }

    }

    @THandler
    public void onAppUnRegister(AppUnRegisteredEvent event) {

       if( ! reqClsManager.unRegisterReqReceiver(event.getApp().getClass()) ) {

           log.warn("[Application] Frame error! # Please contact to the developer. Error class: @{} (Owner: {})", event.getApp().getClass(), event.getPlugin().getName());

       }

    }

}
