package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.ClassReceiverManager;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.network.connection.IRequestReceiver;
import com.talexframe.frame.core.modules.network.connection.TRequest;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.utils.UrlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:08 Project: TalexFrame
 */
@Getter
@Slf4j
public class ClassAppReceiver extends FrameCreator implements IRequestReceiver {

    private static final ClassReceiverManager managerIns = ClassReceiverManager.INSTANCE;

    private final TApp tApp;
    private final Class<? extends TApp> ownClass;

    public ClassAppReceiver(TApp tApp, Class<? extends TApp> ownClass) {

        super("ClassAppReceiver", ownClass.getName());

        this.tApp = tApp;
        this.ownClass = ownClass;

        // NetworkManager.INSTANCE.getMap().put(UrlUtil.formatUrl(tApp.getDefaultPath()), this);

        this.scanForSubApp();

    }

    @Deprecated
    public Set<MethodAppReceiver> matchUrlSubReceivers(String url) {

        return null;

        // Set<MethodAppReceiver> list = new HashSet<>();
        //
        // this.map.entries().forEach((entry) -> {
        //
        //     if( UrlUtil.advancedUrlChecker(entry.getKey(), url) ) {
        //
        //         list.add( entry.getValue() );
        //
        //         log.debug("[解析层] 匹配到 - {} | @{}.{}", entry.getKey(), entry.getValue().getOwnClass().getName(), entry.getValue().getMethod().getName());
        //
        //     }
        //
        // });
        //
        // return list;

    }

    private void scanForSubApp() {

        for ( Method method : ownClass.getMethods()  ) {

            // 过滤掉没有注解的方法
            if ( !method.isAnnotationPresent(TRequest.class) ) continue;

            MethodAppReceiver receiver = new MethodAppReceiver(this.tApp, method);

            String value = UrlUtil.formatUrl(tApp.getDefaultPath()+ "/" + receiver.getTRequest().value());

            managerIns.getTireRouter().addRoute(receiver, value);

            log.debug("[ClassAppReceiver] 方法注册成功 - " + value);

        }

    }

}
