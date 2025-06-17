package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.trie.TrieRouter;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:14 Project: TalexFrame
 */
@Slf4j
public class ClassReceiverManager {

    public static ClassReceiverManager INSTANCE = new ClassReceiverManager();

    @Getter
    private final TrieRouter tireRouter = new TrieRouter();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private ClassReceiverManager() {



    }

    @Getter
    private final Map<Class<?>, ClassAppReceiver> reqMap = new HashMap<>();

    @Getter
    private final Map<Class<?>, WebPlugin> pluginMap = new HashMap<>();

    public boolean registerReqReceiver(WebPlugin webPlugin, ClassAppReceiver receiver) {

        if( reqMap.containsKey(receiver.getOwnClass()) ) {

            return false;

        }

        reqMap.put(receiver.getOwnClass(), receiver);
        pluginMap.put(receiver.getOwnClass(), webPlugin);

        log.debug("[注册层] 注册 @" + receiver.getOwnClass().getName());

        return true;

    }

    public boolean unRegisterReqReceiver(Class<?> cls) {

        if( !reqMap.containsKey(cls) ) {

            return false;

        }

        reqMap.remove(cls);
        pluginMap.remove(cls);

        return true;

    }

}
