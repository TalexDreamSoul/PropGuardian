package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:51 Project: TalexFrame
 */
@Getter
@Slf4j
@Deprecated
public class NetworkManager {

    public static final NetworkManager INSTANCE = new NetworkManager();

    private final Multimap<String, ClassAppReceiver> map = ArrayListMultimap.create();

    private NetworkManager() {



    }

    public Set<ClassAppReceiver> matchUrlReceivers(String path) {

        log.debug("[解析层] Request path: " + path);

        // 类不支持路径变量

        Set<ClassAppReceiver> collection = new HashSet<>();

        map.entries().forEach((entry) -> {

            if( path.startsWith(entry.getKey()) ) {

                log.debug("[解析层] Addon entry: {}", entry);

                collection.add(entry.getValue());

            }

        });

        return collection;

    }

}
