package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.trie;

import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import lombok.Data;

import java.util.Map;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.trie Package }
 *
 * @author TalexDreamSoul 22/07/20 下午 12:10 Project: TalexFrame
 */
@Data
public class TrieNode {

    private String path;
    private String segment;
    private Map<String, TrieNode> staticRoutes;

    private TrieNode dynamicRoute;

    private boolean isWildcard;

    private MethodAppReceiver receiver;

}
